package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignValueTest {

    @Test
    void testJoin() {
        assertEquals(SignValue.MINUS, SignValue.MINUS.join(SignValue.MINUS));
        assertEquals(SignValue.ZERO_MINUS, SignValue.ZERO.join(SignValue.MINUS));
        assertEquals(SignValue.PLUS_MINUS, SignValue.PLUS.join(SignValue.MINUS));
        assertEquals(SignValue.TOP, SignValue.ZERO_MINUS.join(SignValue.PLUS));
        assertEquals(SignValue.ZERO_PLUS, SignValue.ZERO.join(SignValue.PLUS));
        assertEquals(SignValue.TOP, SignValue.PLUS_MINUS.join(SignValue.ZERO));
    }

    @Test
    void testIsLessOrEqual() {
        assertTrue(SignValue.MINUS.isLessOrEqual(SignValue.MINUS));
        assertTrue(SignValue.MINUS.isLessOrEqual(SignValue.PLUS_MINUS));
        assertTrue(SignValue.ZERO.isLessOrEqual(SignValue.TOP));
        assertFalse(SignValue.PLUS.isLessOrEqual(SignValue.MINUS));
        assertTrue(SignValue.BOTTOM.isLessOrEqual(SignValue.PLUS));
        assertTrue(SignValue.PLUS.isLessOrEqual(SignValue.TOP));
        assertFalse(SignValue.TOP.isLessOrEqual(SignValue.ZERO));
    }

    @Test
    void testIsZero() {
        assertTrue(SignValue.isZero(SignValue.ZERO));
        assertFalse(SignValue.isZero(SignValue.PLUS));
        assertFalse(SignValue.isZero(SignValue.ZERO_MINUS));
    }

    @Test
    void testIsMaybeZero() {
        assertTrue(SignValue.isMaybeZero(SignValue.ZERO));
        assertTrue(SignValue.isMaybeZero(SignValue.ZERO_MINUS));
        assertTrue(SignValue.isMaybeZero(SignValue.ZERO_PLUS));
        assertTrue(SignValue.isMaybeZero(SignValue.TOP));
        assertTrue(SignValue.isMaybeZero(SignValue.PLUS_MINUS));
        assertFalse(SignValue.isMaybeZero(SignValue.MINUS));
    }

    @Test
    void testIsNegative() {
        assertTrue(SignValue.isNegative(SignValue.MINUS));
        assertFalse(SignValue.isNegative(SignValue.ZERO));
        assertFalse(SignValue.isNegative(SignValue.PLUS));
    }

    @Test
    void testIsMaybeNegative() {
        assertTrue(SignValue.isMaybeNegative(SignValue.MINUS));
        assertTrue(SignValue.isMaybeNegative(SignValue.ZERO_MINUS));
        assertTrue(SignValue.isMaybeNegative(SignValue.PLUS_MINUS));
        assertTrue(SignValue.isMaybeNegative(SignValue.TOP));
        assertFalse(SignValue.isMaybeNegative(SignValue.ZERO));
        assertFalse(SignValue.isMaybeNegative(SignValue.PLUS));
    }

 @Test
    void joinThrowsIfUninitializedValueUsed() {
        assertThrows(IllegalStateException.class, () -> SignValue.UNINITIALIZED_VALUE.join(SignValue.MINUS));
        assertThrows(IllegalStateException.class, () -> SignValue.MINUS.join(SignValue.UNINITIALIZED_VALUE));
        assertThrows(IllegalStateException.class, () -> SignValue.UNINITIALIZED_VALUE.join(SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    void isLessOrEqualThrowsIfUninitializedValueUsed() {
        assertThrows(IllegalStateException.class, () -> SignValue.UNINITIALIZED_VALUE.isLessOrEqual(SignValue.MINUS));
        assertThrows(IllegalStateException.class, () -> SignValue.MINUS.isLessOrEqual(SignValue.UNINITIALIZED_VALUE));
        assertThrows(IllegalStateException.class, () -> SignValue.UNINITIALIZED_VALUE.isLessOrEqual(SignValue.UNINITIALIZED_VALUE));
    }
    
}
