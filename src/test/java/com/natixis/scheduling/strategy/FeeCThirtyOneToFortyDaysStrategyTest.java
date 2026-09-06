package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeCThirtyOneToFortyDaysStrategyTest {

    private FeeCThirtyOneToFortyDaysStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FeeCThirtyOneToFortyDaysStrategy();
    }

    @Test
    void shouldAcceptThirtyOneDays() {

        assertTrue(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        31
                )
        );
    }

    @Test
    void shouldAcceptFortyDays() {

        assertTrue(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        40
                )
        );
    }

    @Test
    void shouldRejectThirtyDays() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        30
                )
        );
    }

    @Test
    void shouldRejectFortyOneDays() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        41
                )
        );
    }

    @Test
    void shouldRejectAmountUpTo2000() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("2000.00"),
                        35
                )
        );
    }

    @Test
    void shouldCalculateFourPointSevenPercentFee() {

        BigDecimal result = strategy.calculate(
                new BigDecimal("3000.00")
        );

        assertEquals(
                new BigDecimal("141.00"),
                result
        );
    }
}