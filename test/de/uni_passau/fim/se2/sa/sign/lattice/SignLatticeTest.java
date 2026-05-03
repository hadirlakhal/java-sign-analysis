package de.uni_passau.fim.se2.sa.sign.lattice;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignLatticeTest {

    private final SignLattice lattice = new SignLattice();

    @Test
    void testTopAndBottom() {
        assertEquals(SignValue.TOP, lattice.top());
        assertEquals(SignValue.BOTTOM, lattice.bottom());
    }

    @Test
    void testJoinWithUninitialized() {
        assertEquals(SignValue.UNINITIALIZED_VALUE, lattice.join(SignValue.UNINITIALIZED_VALUE, SignValue.PLUS));
        assertEquals(SignValue.UNINITIALIZED_VALUE, lattice.join(SignValue.MINUS, SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    void testJoinWithBottom() {
        assertEquals(SignValue.PLUS, lattice.join(SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.MINUS, lattice.join(SignValue.MINUS, SignValue.BOTTOM));
    }

    @Test
    void testJoinSame() {
        assertEquals(SignValue.PLUS, lattice.join(SignValue.PLUS, SignValue.PLUS));
        assertEquals(SignValue.ZERO_MINUS, lattice.join(SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
    }

    @Test
    void testJoinMask() {
        assertEquals(SignValue.PLUS_MINUS, lattice.join(SignValue.PLUS, SignValue.MINUS));
        assertEquals(SignValue.ZERO_MINUS, lattice.join(SignValue.ZERO, SignValue.MINUS));
        assertEquals(SignValue.ZERO_PLUS, lattice.join(SignValue.ZERO, SignValue.PLUS));
    }

    @Test
    void testJoinFallbackToTop() {
        assertEquals(SignValue.TOP, lattice.join(SignValue.PLUS_MINUS, SignValue.ZERO_PLUS));
    }

    @Test
    void testIsLessOrEqualWithUninitialized() {
        assertTrue(lattice.isLessOrEqual(SignValue.UNINITIALIZED_VALUE, SignValue.PLUS));
        assertTrue(lattice.isLessOrEqual(SignValue.PLUS, SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    void testIsLessOrEqualSame() {
        assertTrue(lattice.isLessOrEqual(SignValue.PLUS, SignValue.PLUS));
    }

    @Test
    void testIsLessOrEqualBottom() {
        assertTrue(lattice.isLessOrEqual(SignValue.BOTTOM, SignValue.PLUS));
    }

    @Test
    void testIsLessOrEqualTop() {
        assertTrue(lattice.isLessOrEqual(SignValue.PLUS, SignValue.TOP));
    }

    @Test
    void testIsLessOrEqualMask() {
        assertTrue(lattice.isLessOrEqual(SignValue.PLUS, SignValue.PLUS_MINUS));
        assertTrue(lattice.isLessOrEqual(SignValue.MINUS, SignValue.ZERO_MINUS));
        assertFalse(lattice.isLessOrEqual(SignValue.ZERO, SignValue.MINUS));
    }
}
