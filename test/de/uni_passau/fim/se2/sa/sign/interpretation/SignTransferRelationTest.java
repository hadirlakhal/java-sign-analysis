package de.uni_passau.fim.se2.sa.sign.interpretation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignTransferRelationTest {

    private final SignTransferRelation transfer = new SignTransferRelation();

    @Test
    void evaluateConstantCoversAllBranches() {
        assertEquals(SignValue.PLUS, transfer.evaluate(7));
        assertEquals(SignValue.ZERO, transfer.evaluate(0));
        assertEquals(SignValue.MINUS, transfer.evaluate(-3));
    }

    @Test
    void evaluateNegation() {
        assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.PLUS));
        assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.MINUS));
        assertEquals(SignValue.ZERO, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO));
        assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.TOP));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.BOTTOM));
        assertEquals(SignValue.UNINITIALIZED_VALUE, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    void evaluateNegationSpecialCases() {
        assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO_MINUS));
        assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.ZERO_PLUS));
        assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.NEG, SignValue.PLUS_MINUS));
    }

    @Test
    void evaluateAdditionBottomBranches() {
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.MINUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.ZERO_MINUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.TOP, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.TOP));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.UNINITIALIZED_VALUE, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.ZERO_PLUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.PLUS_MINUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.BOTTOM, SignValue.ZERO));
    }

    @Test
    void evaluateAdditionUninitializedBranches() {
        assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
    }

    @Test
    void evaluateSubtractionBottomBranches() {
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.BOTTOM));
    }

    @Test
    void evaluateSubtractionUninitializedBranches() {
        assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));
        assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
        assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.UNINITIALIZED_VALUE, SignValue.UNINITIALIZED_VALUE));
    }

    @Test
    void evaluateMultiplicationBottomBranches() {
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.BOTTOM));
    }

    @Test
    void evaluateDivisionBottomBranches() {
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.ZERO)); // division by zero
    }
@Test
void evaluateAdditionSpecialBranches() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.UNINITIALIZED_VALUE));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.UNINITIALIZED_VALUE, SignValue.ZERO));

    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.PLUS));
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.ZERO));

    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.PLUS));

    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.MINUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.PLUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.PLUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.MINUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.MINUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.PLUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.ZERO_PLUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.ZERO_MINUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.ZERO_PLUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.PLUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.ZERO_MINUS));

    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.PLUS));

    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.MINUS));

    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.PLUS_MINUS));

    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));

    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));

    assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.ZERO));
    assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO, SignValue.PLUS_MINUS));
}
@Test
void evaluateBottomShortCircuitForAllOps() {
    for (TransferRelation.Operation op : new TransferRelation.Operation[]{
            TransferRelation.Operation.ADD,
            TransferRelation.Operation.SUB,
            TransferRelation.Operation.MUL,
            TransferRelation.Operation.DIV
    }) {
        assertEquals(SignValue.BOTTOM, transfer.evaluate(op, SignValue.BOTTOM, SignValue.PLUS));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(op, SignValue.PLUS, SignValue.BOTTOM));
        assertEquals(SignValue.BOTTOM, transfer.evaluate(op, SignValue.BOTTOM, SignValue.BOTTOM));
    }
}
@Test
void evaluateZeroMinusPlusMinusReturnsTop() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_MINUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_MINUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS_MINUS, SignValue.ZERO_MINUS));
}

@Test
void evaluateZeroPlusPlusMinusReturnsTop() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.ZERO_PLUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS_MINUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_PLUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS_MINUS, SignValue.ZERO_PLUS));
}

@Test
void evaluateTopPlusMinusReturnsTop() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.TOP, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.TOP));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.TOP, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS_MINUS, SignValue.TOP));
}

@Test
void evaluateUninitializedPlusMinusReturnsTop() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.UNINITIALIZED_VALUE, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.UNINITIALIZED_VALUE));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.UNINITIALIZED_VALUE, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS_MINUS, SignValue.UNINITIALIZED_VALUE));
}

@Test
void evaluatePlusPlusReturnsPlus() {
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.PLUS, SignValue.PLUS));
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.PLUS));
}
@Test
void evaluateMinusMinusReturnsPlusForMul() {
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.MINUS));
}
@Test
void evaluateMinusMinusReturnsMinusForAdd() {
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.MINUS, SignValue.MINUS));
}
@Test
void evaluatePlusTimesMinusAndMinusTimesPlusReturnsMinus() {
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.MINUS));
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.PLUS));
}

@Test
void evaluatePlusMinusShortCircuitForMul() {
    assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS_MINUS, SignValue.PLUS));
    assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.PLUS_MINUS));
}

@Test
void evaluatePlusZeroPlusReturnsZeroPlusForMul() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.PLUS));
}

@Test
void evaluateMinusZeroPlusReturnsZeroMinusForMul() {
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.MINUS));
}

@Test
void evaluatePlusZeroMinusReturnsZeroMinusForMul() {
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.PLUS));
}

@Test
void evaluateMinusZeroMinusReturnsZeroPlusForMul() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.MINUS));
}

@Test
void evaluateZeroPlusZeroPlusReturnsZeroPlusForMul() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
}

@Test
void evaluateZeroMinusZeroMinusReturnsZeroPlusForMul() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
}

@Test
void evaluateZeroPlusZeroMinusReturnsZeroMinusForMul() {
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.ZERO_PLUS));
}


@Test
void evaluatePlusZeroPlusReturnsZeroPlusForMulAndDiv() {
    
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.PLUS));
    
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_PLUS, SignValue.PLUS));
}

@Test
void evaluateMinusZeroPlusReturnsZeroMinusForMulAndDiv() {
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.MINUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_PLUS, SignValue.MINUS));
}
@Test
void evaluatePlusZeroMinusReturnsZeroMinusForMulAndDiv() {
    
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.PLUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.PLUS));
    
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.PLUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_MINUS, SignValue.PLUS));
}

@Test
void evaluateMinusZeroMinusReturnsZeroPlusForMulAndDiv() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.MINUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_MINUS, SignValue.MINUS));
}

@Test
void evaluateZeroPlusZeroPlusReturnsZeroPlusForMulAndDiv() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
}

@Test
void evaluateZeroMinusZeroMinusReturnsZeroPlusForMulAndDiv() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
}
@Test
void evaluateReturnsTopForUnhandledCases() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.ADD, SignValue.UNINITIALIZED_VALUE, SignValue.TOP));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.UNINITIALIZED_VALUE, SignValue.TOP));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.MUL, SignValue.UNINITIALIZED_VALUE, SignValue.TOP));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.UNINITIALIZED_VALUE, SignValue.TOP));
}

@Test
void evaluateSubtractionRhsZeroReturnsLhs() {
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.ZERO));
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.ZERO));
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO_MINUS, SignValue.ZERO));
    assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.ZERO));
}
@Test
void evaluateZeroMinusPlusReturnsMinusForSub() {
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.PLUS));
}

@Test
void evaluateZeroMinusMinusReturnsPlusForSub() {
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.MINUS));
}

@Test
void evaluateZeroMinusZeroMinusReturnsZeroPlusForSub() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO_MINUS));
}
@Test
void evaluateZeroMinusZeroPlusReturnsZeroMinusForSub() {
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.ZERO_PLUS));
}
@Test
void evaluateZeroMinusPlusMinusReturnsPlusMinusForSub() {
    assertEquals(SignValue.PLUS_MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO, SignValue.PLUS_MINUS));
}

@Test
void evaluateZeroMinusMinusPlusReturnsMinusForSub() {
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO_MINUS, SignValue.PLUS));
}
@Test
void evaluateZeroMinusDivZeroPlusReturnsZeroMinus() {
    assertEquals(SignValue.ZERO_MINUS, transfer.evaluate(TransferRelation.Operation.DIV, SignValue.ZERO_MINUS, SignValue.ZERO_PLUS));
}
@Test
void evaluateZeroMinusMinusZeroMinusReturnsTopForSub() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO_MINUS, SignValue.ZERO_MINUS));
}
@Test
void evaluateZeroPlusMinusReturnsPlusForSub() {
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO_PLUS, SignValue.MINUS));
}
@Test
void evaluateZeroPlusMinusZeroMinusReturnsZeroPlusForSub() {
    assertEquals(SignValue.ZERO_PLUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO_PLUS, SignValue.ZERO_MINUS));
}
@Test
void evaluateZeroPlusMinusZeroPlusReturnsTopForSub() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.ZERO_PLUS, SignValue.ZERO_PLUS));
}

@Test
void evaluatePlusMinusMinusPlusMinusReturnsTopForSub() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.PLUS_MINUS));
}

@Test
void evaluateSubtractionSamePlusOrMinusReturnsTop() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.PLUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.MINUS));
}

@Test
void evaluatePlusMinusMinusReturnsPlusForSub() {
    assertEquals(SignValue.PLUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.MINUS));
}
@Test
void evaluateMinusMinusPlusReturnsMinusForSub() {
    assertEquals(SignValue.MINUS, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.PLUS));
}
@Test
void evaluatePlusMinusPlusMinusReturnsTopForSub() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS, SignValue.PLUS_MINUS));
}

@Test
void evaluatePlusMinusMinusPlusReturnsTopForSub() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.PLUS));
}
@Test
void evaluateMinusMinusPlusMinusReturnsTopForSub() {
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.MINUS, SignValue.PLUS_MINUS));
    assertEquals(SignValue.TOP, transfer.evaluate(TransferRelation.Operation.SUB, SignValue.PLUS_MINUS, SignValue.MINUS));
}

}
