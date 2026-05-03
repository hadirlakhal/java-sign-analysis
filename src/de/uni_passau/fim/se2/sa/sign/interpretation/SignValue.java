package de.uni_passau.fim.se2.sa.sign.interpretation;

import com.google.common.base.Preconditions;
import org.objectweb.asm.tree.analysis.Value;
// Implemented with the assistance of an LLM
/** An enum representing the possible abstract values of the sign analysis. */
public enum SignValue implements Value {
  // Important! This implementation is kind of fragile. Don't change the order of enum values!
  // Otherwise, code will break 🤡:)
  BOTTOM("⊥"), // 0
  MINUS("{–}"), // 1
  ZERO("{0}"), // 2
  ZERO_MINUS("{0,–}"), // 3 == ZERO | MINUS
  PLUS("{+}"), // 4 == 4
  PLUS_MINUS("{+,–}"), // 5 == PLUS | MINUS
  ZERO_PLUS("{0,+}"), // 6 == ZERO | PLUS
  TOP("⊤"), // 7 == MINUS | ZERO | PLUS
  UNINITIALIZED_VALUE("∅"); // 8

  private final String repr;

  SignValue(final String pRepr) {
    repr = pRepr;
  }

  @Override
  public int getSize() {
    return 1;
  }

  @Override
  public String toString() {
    return repr;
  }

  public SignValue join(final SignValue pOther) {
    Preconditions.checkState(
        this != UNINITIALIZED_VALUE && pOther != UNINITIALIZED_VALUE,
        "Dummy shall not be used as a value.");

    if (this == BOTTOM) return pOther;
    if (pOther == BOTTOM) return this;
    if (this == pOther) return this;

    int mask = (this.ordinal() | pOther.ordinal());

    switch (mask) {
        case 1: return MINUS;
        case 2: return ZERO;
        case 3: return ZERO_MINUS;
        case 4: return PLUS;
        case 5: return PLUS_MINUS;
        case 6: return ZERO_PLUS;
        case 7: return TOP;
        default:
            throw new IllegalStateException("Unexpected join mask: " + mask + " for " + this + " and " + pOther);
    }
  }

  public boolean isLessOrEqual(final SignValue pOther) {
    Preconditions.checkState(
        this != UNINITIALIZED_VALUE && pOther != UNINITIALIZED_VALUE,
        "Dummy shall not be used as a value.");

    if (this == pOther) return true;
    if (this == BOTTOM) return true;
    if (pOther == TOP) return true;
    return (this.ordinal() | pOther.ordinal()) == pOther.ordinal();
  }

  public static boolean isZero(final SignValue pValue) {
    Preconditions.checkNotNull(pValue);
    switch (pValue) {
        case ZERO:
            return true;
        default:
            return false;
    }
  }

  public static boolean isMaybeZero(final SignValue pValue) {
    Preconditions.checkNotNull(pValue);
    return pValue == ZERO || pValue == ZERO_MINUS || pValue == ZERO_PLUS || pValue == TOP || pValue == PLUS_MINUS;
  }

  public static boolean isNegative(final SignValue pValue) {
    Preconditions.checkNotNull(pValue);
    switch (pValue) {
        case MINUS:
            return true;
        default:
            return false;
    }
  }

  public static boolean isMaybeNegative(final SignValue pValue) {
    Preconditions.checkNotNull(pValue);
    return pValue == MINUS || pValue == ZERO_MINUS || pValue == PLUS_MINUS || pValue == TOP;
  }
}
