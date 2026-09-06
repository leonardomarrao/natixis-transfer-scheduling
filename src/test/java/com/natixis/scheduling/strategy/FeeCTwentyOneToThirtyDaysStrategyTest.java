package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeCTwentyOneToThirtyDaysStrategyTest {

    private FeeCTwentyOneToThirtyDaysStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FeeCTwentyOneToThirtyDaysStrategy();
    }

    @Test
    void shouldAcceptTwentyOneDays() {

        assertTrue(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        21
                )
        );
    }

    @Test
    void shouldAcceptThirtyDays() {

        assertTrue(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        30
                )
        );
    }

    @Test
    void shouldRejectTwentyDays() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        20
                )
        );
    }

    @Test
    void shouldRejectThirtyOneDays() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        31
                )
        );
    }

    @Test
    void shouldRejectAmountUpTo2000() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("2000.00"),
                        25
                )
        );
    }

    @Test
    void shouldCalculateSixPointNinePercentFee() {

        BigDecimal result = strategy.calculate(
                new BigDecimal("3000.00")
        );

        assertEquals(
                new BigDecimal("207.00"),
                result
        );
    }
}