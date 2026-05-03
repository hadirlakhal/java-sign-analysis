package de.uni_passau.fim.se2.sa.sign;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SignAnalysisImplTest {

    private final SignAnalysisImpl impl = new SignAnalysisImpl();

    static class SimpleFrame extends Frame<SignValue> {
        private final SignValue[] stack;
        SimpleFrame(SignValue... stack) { super(0, 0); this.stack = stack; }
        @Override public int getStackSize() { return stack.length; }
        @Override public SignValue getStack(int i) { return stack[i]; }
    }


    @Test
    void analyseThrowsIfMethodNotFound() {
        String className = "java/lang/String";
        String methodName = "notARealMethod:()V";
        assertThrows(IllegalArgumentException.class, () -> impl.analyse(className, methodName));
    }

    @Test
    void analyseThrowsIfClassNotFound() {
        String className = "not/a/real/ClassName";
        String methodName = "main:([Ljava/lang/String;)V";
        assertThrows(IOException.class, () -> impl.analyse(className, methodName));
    }

    @Test
    void analysePopulatesMethodsMapWithMultipleMethods() throws IOException, AnalyzerException {
        assertDoesNotThrow(() -> impl.analyse("java/lang/String", "length:()I"));
    }

    @Test
    void pairToStringReturnsExpectedFormat() throws Exception {
        Class<?> pairClass = getPairClass();
        Constructor<?> ctor = pairClass.getDeclaredConstructor(Object.class, Object.class);
        ctor.setAccessible(true);
        Object pair = ctor.newInstance("foo", 123);
        String str = pair.toString();
        assertTrue(str.contains("Pair{key=foo, value=123}"));
    }


    @Test
    void isDivByZeroTrueForZeroDivisor() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isDivByZero", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        InsnNode idiv = new InsnNode(Opcodes.IDIV);
        Frame<SignValue> frame = new SimpleFrame(SignValue.PLUS, SignValue.ZERO);
        assertTrue((Boolean) m.invoke(impl, idiv, frame));
    }

    @Test
    void isDivByZeroFalseForNonZeroOrNonIDIVOrNullFrame() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isDivByZero", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        InsnNode idiv = new InsnNode(Opcodes.IDIV);
        Frame<SignValue> frame = new SimpleFrame(SignValue.PLUS, SignValue.PLUS);
        assertFalse((Boolean) m.invoke(impl, idiv, frame));
        assertFalse((Boolean) m.invoke(impl, new InsnNode(Opcodes.IADD), frame));
        assertFalse((Boolean) m.invoke(impl, idiv, (Object) null));
    }


    @Test
    void isMaybeDivByZeroTrueForMaybeZeroNotZero() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isMaybeDivByZero", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        InsnNode idiv = new InsnNode(Opcodes.IDIV);
        Frame<SignValue> frame = new SimpleFrame(SignValue.PLUS, SignValue.ZERO_MINUS);
        assertTrue((Boolean) m.invoke(impl, idiv, frame));
    }

    @Test
    void isMaybeDivByZeroFalseForDefinitelyZeroOrNonIDIVOrNullFrame() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isMaybeDivByZero", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        InsnNode idiv = new InsnNode(Opcodes.IDIV);
        Frame<SignValue> frameZero = new SimpleFrame(SignValue.PLUS, SignValue.ZERO);
        Frame<SignValue> framePlus = new SimpleFrame(SignValue.PLUS, SignValue.PLUS);
        assertFalse((Boolean) m.invoke(impl, idiv, frameZero));
        assertFalse((Boolean) m.invoke(impl, idiv, framePlus));
        assertFalse((Boolean) m.invoke(impl, new InsnNode(Opcodes.IADD), frameZero));
        assertFalse((Boolean) m.invoke(impl, idiv, (Object) null));
    }


    @Test
    void isNegativeArrayIndexTrueForNegativeIndex() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isNegativeArrayIndex", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        for (int opcode : arrayOpcodes()) {
            InsnNode arrInsn = new InsnNode(opcode);
            Frame<SignValue> negFrame = new SimpleFrame(SignValue.PLUS, SignValue.MINUS);
            assertTrue((Boolean) m.invoke(impl, arrInsn, negFrame));
        }
    }

    @Test
    void isNegativeArrayIndexFalseForNonNegativeOrNonArrayOrNullFrame() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isNegativeArrayIndex", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        for (int opcode : arrayOpcodes()) {
            InsnNode arrInsn = new InsnNode(opcode);
            Frame<SignValue> posFrame = new SimpleFrame(SignValue.PLUS, SignValue.PLUS);
            assertFalse((Boolean) m.invoke(impl, arrInsn, posFrame));
        }
        assertFalse((Boolean) m.invoke(impl, new InsnNode(Opcodes.IADD), new SimpleFrame(SignValue.MINUS)));
        assertFalse((Boolean) m.invoke(impl, new InsnNode(Opcodes.IALOAD), (Object) null));
    }


    @Test
    void isMaybeNegativeArrayIndexTrueForMaybeNegativeNotNegative() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isMaybeNegativeArrayIndex", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        for (int opcode : arrayOpcodes()) {
            InsnNode arrInsn = new InsnNode(opcode);
            Frame<SignValue> maybeNegFrame = new SimpleFrame(SignValue.PLUS, SignValue.ZERO_MINUS);
            assertTrue((Boolean) m.invoke(impl, arrInsn, maybeNegFrame));
        }
    }

    @Test
    void isMaybeNegativeArrayIndexFalseForDefinitelyNegativeOrNonArrayOrNullFrame() throws Exception {
        Method m = SignAnalysisImpl.class.getDeclaredMethod("isMaybeNegativeArrayIndex", AbstractInsnNode.class, Frame.class);
        m.setAccessible(true);
        for (int opcode : arrayOpcodes()) {
            InsnNode arrInsn = new InsnNode(opcode);
            Frame<SignValue> negFrame = new SimpleFrame(SignValue.PLUS, SignValue.MINUS);
            assertFalse((Boolean) m.invoke(impl, arrInsn, negFrame));
        }
        assertFalse((Boolean) m.invoke(impl, new InsnNode(Opcodes.IADD), new SimpleFrame(SignValue.ZERO_MINUS)));
        assertFalse((Boolean) m.invoke(impl, new InsnNode(Opcodes.IALOAD), (Object) null));
    }


    private static int[] arrayOpcodes() {
        return new int[] {
            Opcodes.IALOAD, Opcodes.IASTORE, Opcodes.LALOAD, Opcodes.LASTORE,
            Opcodes.DALOAD, Opcodes.DASTORE, Opcodes.FALOAD, Opcodes.FASTORE,
            Opcodes.AALOAD, Opcodes.AASTORE, Opcodes.BALOAD, Opcodes.BASTORE,
            Opcodes.CALOAD, Opcodes.CASTORE, Opcodes.SALOAD, Opcodes.SASTORE
        };
    }

    private static Class<?> getPairClass() throws Exception {
        for (Class<?> inner : SignAnalysisImpl.class.getDeclaredClasses()) {
            if (inner.getSimpleName().equals("Pair")) {
                return inner;
            }
        }
        throw new AssertionError("Pair class not found");
    }
}
