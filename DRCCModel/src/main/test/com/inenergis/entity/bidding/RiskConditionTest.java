package com.inenergis.entity.bidding;

import com.inenergis.entity.genericEnum.RelationalOperator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RiskConditionTest {

    RiskCondition riskCondition;

    @BeforeEach
    public void setUp() throws Exception {
        riskCondition = new RiskCondition();
    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    public void nullSource() throws Exception {
        riskCondition.setSourceValue(null);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.GE);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void nullTarget() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(null);
        riskCondition.setComparisonOperator(RelationalOperator.GE);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void nullSourceNullTarget() throws Exception {
        riskCondition.setSourceValue(null);
        riskCondition.setTargetValue(null);
        riskCondition.setComparisonOperator(RelationalOperator.GE);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void greaterOrEqualsGreater() throws Exception {
        riskCondition.setSourceValue(BigDecimal.TEN);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.GE);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void greaterOrEqualsEquals() throws Exception {
        riskCondition.setSourceValue(BigDecimal.TEN);
        riskCondition.setTargetValue(BigDecimal.TEN);
        riskCondition.setComparisonOperator(RelationalOperator.GE);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void greaterOrEqualsFalse() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(BigDecimal.TEN);
        riskCondition.setComparisonOperator(RelationalOperator.GE);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void greaterThan() throws Exception {
        riskCondition.setSourceValue(BigDecimal.TEN);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.GT);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void greaterThanFalse() throws Exception {
        riskCondition.setSourceValue(BigDecimal.TEN);
        riskCondition.setTargetValue(BigDecimal.TEN);
        riskCondition.setComparisonOperator(RelationalOperator.GT);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void lessOrEqualsLess() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(BigDecimal.TEN);
        riskCondition.setComparisonOperator(RelationalOperator.LE);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void lessOrEqualsEquals() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.LE);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void lessOrEqualsFalse() throws Exception {
        riskCondition.setSourceValue(BigDecimal.TEN);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.LE);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void lessThan() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(BigDecimal.TEN);
        riskCondition.setComparisonOperator(RelationalOperator.LT);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void lessThanFalse() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.LT);

        assertFalse(riskCondition.evaluate());
    }

    @Test
    public void equals() throws Exception {
        riskCondition.setSourceValue(BigDecimal.ONE);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.EQ);

        assertTrue(riskCondition.evaluate());
    }

    @Test
    public void notEquals() throws Exception {
        riskCondition.setSourceValue(BigDecimal.TEN);
        riskCondition.setTargetValue(BigDecimal.ONE);
        riskCondition.setComparisonOperator(RelationalOperator.NE);

        assertTrue(riskCondition.evaluate());
    }
}