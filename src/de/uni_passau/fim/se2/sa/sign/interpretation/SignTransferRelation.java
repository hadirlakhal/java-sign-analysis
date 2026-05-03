package de.uni_passau.fim.se2.sa.sign.interpretation;

import com.google.common.base.Preconditions;

public class SignTransferRelation implements TransferRelation {
// Implemented with the assistance of an LLM
  @Override
    public SignValue evaluate(final int pValue) {
    if (pValue > 0) {
        return SignValue.PLUS;
    } else if (pValue < 0) {
        return SignValue.MINUS;
    } else {
        return SignValue.ZERO;
    }
    
}

  @Override
  public SignValue evaluate(final Operation pOperation, final SignValue pValue) {
    Preconditions.checkState(pOperation == Operation.NEG);
    Preconditions.checkNotNull(pValue);

    switch (pValue) {
      case PLUS:
        return SignValue.MINUS;
      case MINUS:
        return SignValue.PLUS;
      case ZERO:
        return SignValue.ZERO;
      case ZERO_MINUS:
        return SignValue.ZERO_PLUS;
      case ZERO_PLUS:
        return SignValue.ZERO_MINUS;
      case PLUS_MINUS:
        return SignValue.PLUS_MINUS;
      case TOP:
        return SignValue.TOP;
      case BOTTOM:
        return SignValue.BOTTOM;
      case UNINITIALIZED_VALUE:
        return SignValue.UNINITIALIZED_VALUE;
      default:
        throw new IllegalArgumentException("Unknown SignValue: " + pValue);
    }
  }

  @Override
  public SignValue evaluate(
      final Operation pOperation, final SignValue pLHS, final SignValue pRHS) {
    Preconditions.checkState(
      pOperation == Operation.ADD
        || pOperation == Operation.SUB
        || pOperation == Operation.MUL
        || pOperation == Operation.DIV);
  Preconditions.checkNotNull(pLHS);
  Preconditions.checkNotNull(pRHS);
  
  

    // Addition
    if (pOperation == Operation.ADD) {

    if (pLHS == SignValue.BOTTOM && pRHS == SignValue.BOTTOM) return SignValue.BOTTOM;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.MINUS)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.PLUS)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.ZERO_MINUS)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.TOP && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.TOP)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.UNINITIALIZED_VALUE)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.ZERO_PLUS)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.BOTTOM) ||
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.PLUS_MINUS)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.ZERO && pRHS == SignValue.BOTTOM) || 
    (pLHS == SignValue.BOTTOM && pRHS == SignValue.ZERO)) return SignValue.BOTTOM;

    if ((pLHS == SignValue.ZERO && pRHS == SignValue.UNINITIALIZED_VALUE) ||
    (pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.ZERO)) return SignValue.TOP;  
    
    if (pLHS == SignValue.ZERO) return pRHS;

    if (pRHS == SignValue.ZERO) return pLHS;

    if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) return SignValue.PLUS;

    if (pLHS == SignValue.MINUS && pRHS == SignValue.MINUS) return SignValue.MINUS;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.MINUS) ||
        (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS) ||
        (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS) ||
        (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.MINUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_PLUS) ||
        (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.MINUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_PLUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO_MINUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO_PLUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS) ||
        (pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_MINUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_PLUS) ||
        (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS)) {
        return SignValue.PLUS;
    }

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.MINUS)) {
        return SignValue.MINUS;
    }

    if ((pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS_MINUS)) {
        return SignValue.TOP;
    }

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_PLUS)) {
        return SignValue.ZERO_PLUS;
    }

    if ((pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_MINUS)) {
        return SignValue.ZERO_MINUS;
    }

    if ((pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO) ||
        (pLHS == SignValue.ZERO && pRHS == SignValue.PLUS_MINUS)) {
        return SignValue.PLUS_MINUS;
    }
    
    return SignValue.TOP;
}
    // Subtraction
    if (pOperation == Operation.SUB) {

    if ((pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.ZERO) ||
    (pLHS == SignValue.ZERO && pRHS == SignValue.UNINITIALIZED_VALUE)) return SignValue.TOP;

    if (pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.UNINITIALIZED_VALUE) return SignValue.TOP;

    if (pLHS == SignValue.BOTTOM || pRHS == SignValue.BOTTOM) return SignValue.BOTTOM;

    if (pLHS == SignValue.TOP || pRHS == SignValue.TOP) return SignValue.TOP;

    if (pRHS == SignValue.ZERO) return pLHS;

    if (pLHS == SignValue.ZERO && pRHS == SignValue.PLUS) return SignValue.MINUS;

    if (pLHS == SignValue.ZERO && pRHS == SignValue.MINUS) return SignValue.PLUS;

    if (pLHS == SignValue.ZERO && pRHS == SignValue.ZERO_MINUS) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.ZERO && pRHS == SignValue.ZERO_PLUS) return SignValue.ZERO_MINUS;

    if (pLHS == SignValue.ZERO && pRHS == SignValue.PLUS_MINUS) return SignValue.PLUS_MINUS;

    if (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS) return SignValue.MINUS;

    if (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_PLUS) return SignValue.ZERO_MINUS;

    if (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.TOP;

    if (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.MINUS) return SignValue.PLUS;

    if (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_MINUS) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_PLUS) return SignValue.TOP;

    if (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS_MINUS) return SignValue.TOP;

    if (pLHS == pRHS) {
        if (pLHS == SignValue.PLUS || pLHS == SignValue.MINUS) {
            return SignValue.TOP;
        }
        return SignValue.ZERO;
    }

    if (pLHS == SignValue.PLUS && pRHS == SignValue.MINUS) return SignValue.PLUS;

    if (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS) return SignValue.MINUS;

    if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS_MINUS) return SignValue.TOP;

    if (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS) return SignValue.TOP;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.MINUS)) return SignValue.TOP;

    if (pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_PLUS) return SignValue.MINUS;

    if (pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_MINUS) return SignValue.PLUS;

    return SignValue.TOP;
}
    // Multiplication
    if (pOperation == Operation.MUL) {

    if (pLHS == SignValue.BOTTOM || pRHS == SignValue.BOTTOM) return SignValue.BOTTOM;

    if ((pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO_MINUS)) return SignValue.TOP;

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS_MINUS) ||
    (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO_PLUS)) return SignValue.TOP;

    if ((pLHS == SignValue.TOP && pRHS == SignValue.PLUS_MINUS) ||
    (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.TOP)) return SignValue.TOP;

    if ((pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.PLUS_MINUS) ||
    (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.UNINITIALIZED_VALUE)) return SignValue.TOP;


    if (pLHS == SignValue.ZERO || pRHS == SignValue.ZERO) return SignValue.ZERO;

    if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) return SignValue.PLUS;

    if (pLHS == SignValue.MINUS && pRHS == SignValue.MINUS) return SignValue.PLUS;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.MINUS) ||
        (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS)) return SignValue.MINUS;

    if (pLHS == SignValue.PLUS_MINUS || pRHS == SignValue.PLUS_MINUS) return SignValue.PLUS_MINUS;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_PLUS) ||
        (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS)) return SignValue.ZERO_PLUS;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_PLUS) ||
        (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.MINUS)) return SignValue.ZERO_MINUS;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS)) return SignValue.ZERO_MINUS;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.MINUS)) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_PLUS) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.ZERO_PLUS;

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_PLUS)) return SignValue.ZERO_MINUS;


    return SignValue.TOP;
    }

// Division
if (pOperation == Operation.DIV) {

    if (pLHS == SignValue.BOTTOM || pRHS == SignValue.BOTTOM) return SignValue.BOTTOM;

    if (pRHS == SignValue.ZERO) return SignValue.BOTTOM;

    if (pLHS == SignValue.ZERO) return SignValue.ZERO;

    if ((pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO_MINUS)) return SignValue.TOP;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.MINUS)) return SignValue.TOP;

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.ZERO_PLUS)) return SignValue.TOP;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS)) return SignValue.TOP;

    if ((pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_PLUS)) return SignValue.ZERO_MINUS;

    if (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.PLUS_MINUS) return SignValue.TOP;

    if ((pLHS == SignValue.TOP && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.TOP)) return SignValue.TOP;

    if ((pLHS == SignValue.UNINITIALIZED_VALUE && pRHS == SignValue.PLUS_MINUS) ||
        (pLHS == SignValue.PLUS_MINUS && pRHS == SignValue.UNINITIALIZED_VALUE)) return SignValue.TOP;

    if (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_PLUS) return SignValue.ZERO_MINUS;
    if (pLHS == SignValue.PLUS && pRHS == SignValue.PLUS) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.MINUS && pRHS == SignValue.MINUS) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.PLUS && pRHS == SignValue.MINUS) return SignValue.ZERO_MINUS;

    if (pLHS == SignValue.MINUS && pRHS == SignValue.PLUS) return SignValue.ZERO_MINUS;

    if (pLHS == SignValue.PLUS_MINUS || pRHS == SignValue.PLUS_MINUS) return SignValue.PLUS_MINUS;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_PLUS) ||
        (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.PLUS)) return SignValue.ZERO_PLUS;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_PLUS) ||
        (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.MINUS)) return SignValue.ZERO_MINUS;

    if ((pLHS == SignValue.PLUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.PLUS)) return SignValue.ZERO_MINUS;

    if ((pLHS == SignValue.MINUS && pRHS == SignValue.ZERO_MINUS) ||
        (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.MINUS)) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.ZERO_PLUS && pRHS == SignValue.ZERO_PLUS) return SignValue.ZERO_PLUS;

    if (pLHS == SignValue.ZERO_MINUS && pRHS == SignValue.ZERO_MINUS) return SignValue.ZERO_PLUS;

    return SignValue.TOP;
}

    return SignValue.TOP;
  }
}
