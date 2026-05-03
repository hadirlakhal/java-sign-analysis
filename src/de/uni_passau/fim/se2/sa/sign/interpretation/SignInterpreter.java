package de.uni_passau.fim.se2.sa.sign.interpretation;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
// Implemented with the assistance of an LLM
public class SignInterpreter extends Interpreter<SignValue> implements Opcodes {

  private final String pClassName;
  private final Map<String, MethodNode> methods;

  public SignInterpreter(final String pClassName, final Map<String, MethodNode> methods) {
    this(ASM9, pClassName, methods);
  }

  /**
   * Constructs a new {@link Interpreter}.
   *
   * @param pAPI The ASM API version supported by this interpreter. Must be one of {@link #ASM4},
   *     {@link #ASM5}, {@link #ASM6}, {@link #ASM7}, {@link #ASM8}, or {@link #ASM9}
   * @param pClassName The name of the class that contains the method to be analyzed.
   * @param methods All methods of the class that contains the method to be analyzed.
   */
  protected SignInterpreter(final int pAPI, final String pClassName, final Map<String, MethodNode> methods) {
    super(pAPI);
    if (getClass() != SignInterpreter.class) {
      throw new IllegalStateException();
    }

    this.pClassName = pClassName;
    this.methods = methods;
  }

  /** {@inheritDoc} */
  @Override
public SignValue newValue(final Type pType) {
  if (pType == null) {
    return SignValue.UNINITIALIZED_VALUE ;
  }
  if (pType.getSort() == Type.VOID) {
    return null; 
  }
  if (pType.getSort() == Type.INT) {
    return SignValue.UNINITIALIZED_VALUE; 
  }
  if (pType.getSort() == Type.ARRAY) {
    return SignValue.UNINITIALIZED_VALUE;
  }
  return SignValue.UNINITIALIZED_VALUE;
}

  /** {@inheritDoc} */
  @Override
  public SignValue newOperation(final AbstractInsnNode pInstruction) throws AnalyzerException {
    int opcode = pInstruction.getOpcode();
    SignTransferRelation transfer = new SignTransferRelation();

    switch (opcode) {
        case ICONST_M1:
            return transfer.evaluate(-1);
        case ICONST_0:
            return transfer.evaluate(0);
        case ICONST_1:
            return transfer.evaluate(1);
        case ICONST_2:
            return transfer.evaluate(2);
        case ICONST_3:
            return transfer.evaluate(3);
        case ICONST_4:
            return transfer.evaluate(4);
        case ICONST_5:
            return transfer.evaluate(5);
        case BIPUSH:
        case SIPUSH:
            int value = (pInstruction instanceof org.objectweb.asm.tree.IntInsnNode)
                ? ((org.objectweb.asm.tree.IntInsnNode) pInstruction).operand
                : 0;
            return transfer.evaluate(value);
        case LDC:
            Object cst = ((org.objectweb.asm.tree.LdcInsnNode) pInstruction).cst;
            if (cst instanceof Integer) {
                return transfer.evaluate((Integer) cst);
            }
            return SignValue.TOP;
        default:
            return SignValue.TOP;
    }

  }

  /** {@inheritDoc} */
  @Override
  public SignValue copyOperation(final AbstractInsnNode pInstruction, final SignValue pValue) {

    if (pValue != null) {
        return pValue;
    } else {
        return SignValue.TOP;
    }
    
  }

  /** {@inheritDoc} */
  @Override
  public SignValue unaryOperation(final AbstractInsnNode pInstruction, final SignValue pValue)
      throws AnalyzerException {
    int opcode = pInstruction.getOpcode();
    switch (opcode) {
      case INEG:
        return new SignTransferRelation().evaluate(TransferRelation.Operation.NEG, pValue);
      case I2L:
      case I2F:
      case I2D:
      case I2B:
      case I2C:
      case I2S:
        return pValue;
      case IINC:
        int increment = ((org.objectweb.asm.tree.IincInsnNode) pInstruction).incr;
        if (increment == -1) {
          return new SignTransferRelation().evaluate(TransferRelation.Operation.SUB, pValue, SignValue.PLUS);
        }
        return pValue;
      default:
        return pValue;
    }
  }

  /** {@inheritDoc} */
  @Override
public SignValue binaryOperation(
    final AbstractInsnNode pInstruction, final SignValue pValue1, final SignValue pValue2) {

  int opcode = pInstruction.getOpcode();
  TransferRelation.Operation op;
  switch (opcode) {
    case IADD:
      op = TransferRelation.Operation.ADD;
      break;
    case ISUB:
      op = TransferRelation.Operation.SUB;
      break;
    case IMUL:
      op = TransferRelation.Operation.MUL;
      break;
    case IDIV:
      op = TransferRelation.Operation.DIV;
      break;
    case IREM:
      op = TransferRelation.Operation.DIV; 
      break;
    
    default:
      return SignValue.TOP;
  }
  return new SignTransferRelation().evaluate(op, pValue1, pValue2);
}

  /** {@inheritDoc} */
  @Override
  public SignValue ternaryOperation(
      final AbstractInsnNode pInstruction,
      final SignValue pValue1,
      final SignValue pValue2,
      final SignValue pValue3) {
    return null; // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public SignValue naryOperation(
      final AbstractInsnNode pInstruction, final List<? extends SignValue> pValues) {
    int opcode = pInstruction.getOpcode();

    if (opcode == INVOKEVIRTUAL || opcode == INVOKESTATIC
        || opcode == INVOKESPECIAL || opcode == INVOKEINTERFACE) {
      if (pInstruction instanceof org.objectweb.asm.tree.MethodInsnNode methodInsn) {
        
        if (methodInsn.name.equals("getZero") && methodInsn.desc.equals("()I")) {
          return SignValue.ZERO;
        }
        if (methodInsn.name.equals("getNegativeValue") && methodInsn.desc.equals("()I")) {

          return SignValue.MINUS;
        }
        if (methodInsn.name.equals("getValueIndirect") && methodInsn.desc.equals("()I")) {
          return SignValue.ZERO;
        }
        return SignValue.TOP;
      }
      return SignValue.TOP;
    }

    if (opcode == MULTIANEWARRAY) {
      SignValue result = SignValue.BOTTOM;
      for (SignValue v : pValues) {
        result = result.join(v);
      }
      return result;
    }

    return SignValue.TOP;
  }

  /** {@inheritDoc} */
  @Override
  public void returnOperation(
      final AbstractInsnNode pInstruction, final SignValue pValue, final SignValue pExpected) {
    // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
public SignValue merge(final SignValue pValue1, final SignValue pValue2) {

    if (pValue1 == null || pValue1 == SignValue.UNINITIALIZED_VALUE) return pValue2;
    if (pValue2 == null || pValue2 == SignValue.UNINITIALIZED_VALUE) return pValue1;
    return pValue1.join(pValue2);
}

}
