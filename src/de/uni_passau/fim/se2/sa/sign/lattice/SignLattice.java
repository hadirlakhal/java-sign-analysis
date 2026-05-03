package de.uni_passau.fim.se2.sa.sign.lattice;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;

/** A lattice for {@link SignValue}. */
public class SignLattice implements Lattice<SignValue> {
// Implemented with the assistance of an LLM
  /** {@inheritDoc} */
  @Override
  public SignValue top() {
    return SignValue.TOP;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue bottom() {
    return SignValue.BOTTOM;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue join(final SignValue pFirst, final SignValue pSecond) {
    if (pFirst == SignValue.UNINITIALIZED_VALUE || pSecond == SignValue.UNINITIALIZED_VALUE)
      return SignValue.UNINITIALIZED_VALUE;
    if (pFirst == SignValue.BOTTOM) return pSecond;
    if (pSecond == SignValue.BOTTOM) return pFirst;
    if (pFirst == pSecond) return pFirst;
    int mask = pFirst.ordinal() | pSecond.ordinal();
    for (SignValue v : SignValue.values()) {
      if (v.ordinal() == mask) return v;
    }
    return SignValue.TOP;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isLessOrEqual(final SignValue pFirst, final SignValue pSecond) {
    if (pFirst == SignValue.UNINITIALIZED_VALUE || pSecond == SignValue.UNINITIALIZED_VALUE)
      return true;
    if (pFirst == pSecond) return true;
    if (pFirst == SignValue.BOTTOM) return true;
    if (pSecond == SignValue.TOP) return true;
    return (pFirst.ordinal() | pSecond.ordinal()) == pSecond.ordinal();
  }
}
