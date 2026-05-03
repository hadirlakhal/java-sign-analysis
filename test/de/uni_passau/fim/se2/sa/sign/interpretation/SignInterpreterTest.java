package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SignInterpreterTest {

    private final SignInterpreter interpreter = new SignInterpreter("Dummy", Collections.emptyMap());

    @Test
    void newValueCoversAllBranches() {
        assertNull(interpreter.newValue(Type.VOID_TYPE));
        assertEquals(SignValue.UNINITIALIZED_VALUE, interpreter.newValue(Type.INT_TYPE));
        assertEquals(SignValue.UNINITIALIZED_VALUE, interpreter.newValue(Type.getType("[I")));
        assertEquals(SignValue.UNINITIALIZED_VALUE, interpreter.newValue(null));
    }

    @Test
    void newOperationCoversAllBranches() throws AnalyzerException {
        assertEquals(SignValue.MINUS, interpreter.newOperation(new InsnNode(Opcodes.ICONST_M1)));
        assertEquals(SignValue.ZERO, interpreter.newOperation(new InsnNode(Opcodes.ICONST_0)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new InsnNode(Opcodes.ICONST_1)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new InsnNode(Opcodes.ICONST_2)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new InsnNode(Opcodes.ICONST_3)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new InsnNode(Opcodes.ICONST_4)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new InsnNode(Opcodes.ICONST_5)));
        assertEquals(SignValue.MINUS, interpreter.newOperation(new IntInsnNode(Opcodes.BIPUSH, -7)));
        assertEquals(SignValue.ZERO, interpreter.newOperation(new IntInsnNode(Opcodes.BIPUSH, 0)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new IntInsnNode(Opcodes.BIPUSH, 5)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new IntInsnNode(Opcodes.SIPUSH, 123)));
        assertEquals(SignValue.ZERO, interpreter.newOperation(new LdcInsnNode(0)));
        assertEquals(SignValue.PLUS, interpreter.newOperation(new LdcInsnNode(42)));
        assertEquals(SignValue.MINUS, interpreter.newOperation(new LdcInsnNode(-42)));
        assertEquals(SignValue.TOP, interpreter.newOperation(new LdcInsnNode("not an int")));
        assertEquals(SignValue.TOP, interpreter.newOperation(new InsnNode(Opcodes.NOP)));
    }

    @Test
    void copyOperationCoversAllBranches() {
        assertEquals(SignValue.PLUS, interpreter.copyOperation(null, SignValue.PLUS));
        assertEquals(SignValue.MINUS, interpreter.copyOperation(null, SignValue.MINUS));
        assertEquals(SignValue.TOP, interpreter.copyOperation(null, null));
    }

    @Test
    void unaryOperationCoversAllBranches() throws AnalyzerException {
        assertEquals(SignValue.MINUS, interpreter.unaryOperation(new InsnNode(Opcodes.INEG), SignValue.PLUS));
        assertEquals(SignValue.PLUS, interpreter.unaryOperation(new InsnNode(Opcodes.INEG), SignValue.MINUS));
        assertEquals(SignValue.ZERO, interpreter.unaryOperation(new InsnNode(Opcodes.INEG), SignValue.ZERO));
        for (int op : new int[]{Opcodes.I2L, Opcodes.I2F, Opcodes.I2D, Opcodes.I2B, Opcodes.I2C, Opcodes.I2S}) {
            assertEquals(SignValue.PLUS, interpreter.unaryOperation(new InsnNode(op), SignValue.PLUS));
        }
        IincInsnNode dec = new IincInsnNode(0, -1);
        assertEquals(
            new SignTransferRelation().evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.PLUS),
            interpreter.unaryOperation(dec, SignValue.PLUS)
        );
        IincInsnNode inc = new IincInsnNode(0, 1);
        assertEquals(SignValue.PLUS, interpreter.unaryOperation(inc, SignValue.PLUS));
        assertEquals(SignValue.PLUS, interpreter.unaryOperation(new InsnNode(Opcodes.NOP), SignValue.PLUS));
    }


    @Test
    void ternaryOperationAlwaysReturnsNull() {
        assertNull(interpreter.ternaryOperation(
                new InsnNode(Opcodes.IADD), SignValue.PLUS, SignValue.ZERO, SignValue.MINUS));
    }

    @Test
    void naryOperationCoversInvokeBranches() {
        MethodInsnNode getZero = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "getZero", "()I", false);
        assertEquals(SignValue.ZERO, interpreter.naryOperation(getZero, List.of()));
        MethodInsnNode getNeg = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "getNegativeValue", "()I", false);
        assertEquals(SignValue.MINUS, interpreter.naryOperation(getNeg, List.of()));
        MethodInsnNode getValueIndirect = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "getValueIndirect", "()I", false);
        assertEquals(SignValue.ZERO, interpreter.naryOperation(getValueIndirect, List.of()));
        MethodInsnNode unknown = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "unknown", "()I", false);
        assertEquals(SignValue.TOP, interpreter.naryOperation(unknown, List.of()));
        assertEquals(SignValue.TOP, interpreter.naryOperation(new InsnNode(Opcodes.INVOKESTATIC), List.of()));
    }

    @Test
    void naryOperationMultiArrayAllBottom() {
        assertEquals(SignValue.BOTTOM, interpreter.naryOperation(
                new InsnNode(Opcodes.MULTIANEWARRAY),
                List.of(SignValue.BOTTOM, SignValue.BOTTOM)));
    }

    @Test
    void naryOperationMultiArrayMixed() {
        assertEquals(SignValue.PLUS, interpreter.naryOperation(
                new InsnNode(Opcodes.MULTIANEWARRAY),
                List.of(SignValue.BOTTOM, SignValue.PLUS)));
        assertEquals(SignValue.MINUS, interpreter.naryOperation(
                new InsnNode(Opcodes.MULTIANEWARRAY),
                List.of(SignValue.BOTTOM, SignValue.MINUS)));
        assertEquals(SignValue.PLUS_MINUS, interpreter.naryOperation(
                new InsnNode(Opcodes.MULTIANEWARRAY),
                List.of(SignValue.PLUS, SignValue.MINUS)));
        assertEquals(SignValue.TOP, interpreter.naryOperation(
                new InsnNode(Opcodes.MULTIANEWARRAY),
                List.of(SignValue.PLUS, SignValue.ZERO, SignValue.MINUS)));
    }

    @Test
    void naryOperationDefaultReturnsTop() {
        assertEquals(SignValue.TOP, interpreter.naryOperation(new InsnNode(Opcodes.NOP), List.of()));
    }

    @Test
    void returnOperationDoesNothing() {
        interpreter.returnOperation(
            new InsnNode(Opcodes.RETURN),
            SignValue.PLUS,
            SignValue.PLUS
        );
        interpreter.returnOperation(
            new InsnNode(Opcodes.IRETURN),
            SignValue.MINUS,
            SignValue.ZERO
        );
        interpreter.returnOperation(
            null,
            null,
            null
        );
    }

    @Test
    void mergeNullCases() {
        assertEquals(SignValue.PLUS, interpreter.merge(null, SignValue.PLUS));
        assertEquals(SignValue.MINUS, interpreter.merge(null, SignValue.MINUS));
        assertEquals(SignValue.PLUS, interpreter.merge(SignValue.PLUS, null));
        assertEquals(SignValue.MINUS, interpreter.merge(SignValue.MINUS, null));
    }

    @Test
    void mergeUninitializedCases() {
        assertEquals(SignValue.PLUS, interpreter.merge(SignValue.UNINITIALIZED_VALUE, SignValue.PLUS));
        assertEquals(SignValue.MINUS, interpreter.merge(SignValue.UNINITIALIZED_VALUE, SignValue.MINUS));
        assertEquals(SignValue.PLUS, interpreter.merge(SignValue.PLUS, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.MINUS, interpreter.merge(SignValue.MINUS, SignValue.UNINITIALIZED_VALUE));
    }


    @Test
    void mergeJoinCases() {
        assertEquals(SignValue.PLUS_MINUS, interpreter.merge(SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.TOP, interpreter.merge(SignValue.PLUS_MINUS, SignValue.ZERO));
        assertEquals(SignValue.ZERO_MINUS, interpreter.merge(SignValue.ZERO, SignValue.MINUS));
    }

        @Test
    void naryOperationCoversGetZeroBranch() {
        MethodInsnNode getZero = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "getZero", "()I", false);
        assertEquals(SignValue.ZERO, interpreter.naryOperation(getZero, List.of()));
    }
    
    @Test
    void naryOperationCoversGetNegativeValueBranch() {
        MethodInsnNode getNeg = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "getNegativeValue", "()I", false);
        assertEquals(SignValue.MINUS, interpreter.naryOperation(getNeg, List.of()));
    }
    
    @Test
    void naryOperationCoversGetValueIndirectBranch() {
        MethodInsnNode getValueIndirect = new MethodInsnNode(Opcodes.INVOKESTATIC, "Dummy", "getValueIndirect", "()I", false);
        assertEquals(SignValue.ZERO, interpreter.naryOperation(getValueIndirect, List.of()));
    }

}