package de.uni_passau.fim.se2.sa.sign;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignInterpreter;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
// Implemented with the assistance of an LLM
public class SignAnalysisImpl implements SignAnalysis {

  @Override
  public SortedSetMultimap<Integer, AnalysisResult> analyse(
      final String pClassName, final String pMethodName) throws AnalyzerException, IOException {

    // Load the class using ASM
    ClassReader classReader = new ClassReader(pClassName);
    ClassNode classNode = new ClassNode();
    classReader.accept(classNode, 0);

    // Find the method node by name and descriptor
    MethodNode methodNode = null;
    for (MethodNode m : classNode.methods) {
      if ((m.name + ":" + m.desc).equals(pMethodName)) {
        methodNode = m;
        break;
      }
    }
    if (methodNode == null) {
      throw new IllegalArgumentException("Method not found: " + pMethodName);
    }

    // Prepare a map of all methods in the class 
    HashMap<String, MethodNode> methods = new HashMap<>();
    for (MethodNode m : classNode.methods) {
      methods.put(m.name + ":" + m.desc, m);
    }

    // Run the sign analysis using our interpreter
    SignInterpreter interpreter = new SignInterpreter(pClassName, methods);
    Analyzer<SignValue> analyzer = new Analyzer<>(interpreter);
    Frame<SignValue>[] frames = analyzer.analyze(pClassName, methodNode);

    // Pair each instruction with its frame
    List<Pair<AbstractInsnNode, Frame<SignValue>>> pairs = new java.util.ArrayList<>();
    int i = 0;
    for (AbstractInsnNode insn = methodNode.instructions.getFirst(); insn != null; insn = insn.getNext(), i++) {
      pairs.add(new Pair<>(insn, frames[i]));
    }

    return extractAnalysisResults(pairs);
  }

  /**
   * Extracts the analysis results from the given pairs of instructions and frames.
   *
   * <p>The result is a {@link SortedSetMultimap} that maps line numbers to the analysis results.
   * For each line number, there can be multiple analysis results.  The method expects a list of
   * pairs of instructions and frames.  The instructions are expected to be in the same order as
   * they are in the method.  The frames are expected to be the frames that are computed for the
   * instructions.  The method will extract the analysis results from the frames and map them to
   * the line numbers of the instructions.
   *
   * @param pPairs The pairs of instructions and frames.
   * @return The analysis results.
   */
  private SortedSetMultimap<Integer, AnalysisResult> extractAnalysisResults(
      final List<Pair<AbstractInsnNode, Frame<SignValue>>> pPairs) {
    final SortedSetMultimap<Integer, AnalysisResult> result = TreeMultimap.create();
    int lineNumber = -1;

    for (final Pair<AbstractInsnNode, Frame<SignValue>> pair : pPairs) {
      final AbstractInsnNode instruction = pair.key();
      final Frame<SignValue> frame = pair.value();
      if (instruction instanceof LineNumberNode lineNumberNode) {
        lineNumber = lineNumberNode.line;
      }

      if (isDivByZero(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.DIVISION_BY_ZERO);
      } else if (isMaybeDivByZero(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.MAYBE_DIVISION_BY_ZERO);
      }

      if (isNegativeArrayIndex(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.NEGATIVE_ARRAY_INDEX);
      } else if (isMaybeNegativeArrayIndex(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.MAYBE_NEGATIVE_ARRAY_INDEX);
      }
    }

    return result;
  }

  private boolean isDivByZero(final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    if (pInstruction.getOpcode() == org.objectweb.asm.Opcodes.IDIV && pFrame != null) {
      SignValue divisor = pFrame.getStack(pFrame.getStackSize() - 1);
      return SignValue.isZero(divisor);
    }
    return false;
  }

  private boolean isMaybeDivByZero(final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    if (pInstruction.getOpcode() == org.objectweb.asm.Opcodes.IDIV && pFrame != null) {
      SignValue divisor = pFrame.getStack(pFrame.getStackSize() - 1);
      return SignValue.isMaybeZero(divisor) && !SignValue.isZero(divisor);
    }
    return false;
  }

  private boolean isNegativeArrayIndex(final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    if (pFrame == null) return false;
    int opcode = pInstruction.getOpcode();
    switch (opcode) {
        case org.objectweb.asm.Opcodes.IALOAD:
        case org.objectweb.asm.Opcodes.IASTORE:
        case org.objectweb.asm.Opcodes.LALOAD:
        case org.objectweb.asm.Opcodes.LASTORE:
        case org.objectweb.asm.Opcodes.DALOAD:
        case org.objectweb.asm.Opcodes.DASTORE:
        case org.objectweb.asm.Opcodes.FALOAD:
        case org.objectweb.asm.Opcodes.FASTORE:
        case org.objectweb.asm.Opcodes.AALOAD:
        case org.objectweb.asm.Opcodes.AASTORE:
        case org.objectweb.asm.Opcodes.BALOAD:
        case org.objectweb.asm.Opcodes.BASTORE:
        case org.objectweb.asm.Opcodes.CALOAD:
        case org.objectweb.asm.Opcodes.CASTORE:
        case org.objectweb.asm.Opcodes.SALOAD:
        case org.objectweb.asm.Opcodes.SASTORE:
            SignValue index = pFrame.getStack(pFrame.getStackSize() - 1);
            return SignValue.isNegative(index);
        default:
            return false;
    }
  }

  private boolean isMaybeNegativeArrayIndex(final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    if (pFrame == null) return false;
    int opcode = pInstruction.getOpcode();
    switch (opcode) {
        case org.objectweb.asm.Opcodes.IALOAD:
        case org.objectweb.asm.Opcodes.IASTORE:
        case org.objectweb.asm.Opcodes.LALOAD:
        case org.objectweb.asm.Opcodes.LASTORE:
        case org.objectweb.asm.Opcodes.DALOAD:
        case org.objectweb.asm.Opcodes.DASTORE:
        case org.objectweb.asm.Opcodes.FALOAD:
        case org.objectweb.asm.Opcodes.FASTORE:
        case org.objectweb.asm.Opcodes.AALOAD:
        case org.objectweb.asm.Opcodes.AASTORE:
        case org.objectweb.asm.Opcodes.BALOAD:
        case org.objectweb.asm.Opcodes.BASTORE:
        case org.objectweb.asm.Opcodes.CALOAD:
        case org.objectweb.asm.Opcodes.CASTORE:
        case org.objectweb.asm.Opcodes.SALOAD:
        case org.objectweb.asm.Opcodes.SASTORE:
            SignValue index = pFrame.getStack(pFrame.getStackSize() - 1);
            return SignValue.isMaybeNegative(index) && !SignValue.isNegative(index);
        default:
            return false;
    }
  }



  private record Pair<K, V>(K key, V value) {
    @Override
    public String toString() {
      return "Pair{key=" + key + ", value=" + value + '}';
    }
  }
}
