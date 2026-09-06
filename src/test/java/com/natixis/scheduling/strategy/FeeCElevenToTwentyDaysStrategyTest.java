package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeCElevenToTwentyDaysStrategyTest {

    private FeeCElevenToTwentyDaysStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FeeCElevenToTwentyDaysStrategy();
    }

    @Test
    void shouldAcceptElevenDays() {

        boolean result = strategy.isApplicable(
                new BigDecimal("3000.00"),
                11
        );

        assertTrue(result);
    }

    @Test
    void shouldAcceptTwentyDays() {

        boolean result = strategy.isApplicable(
                new BigDecimal("3000.00"),
                20
        );

        assertTrue(result);
    }

    @Test
    void shouldRejectTenDays() {

        boolean result = strategy.isApplicable(
                new BigDecimal("3000.00"),
                10
        );

        assertFalse(result);
    }

    @Test
    void shouldRejectTwentyOneDays() {

        boolean result = strategy.isApplicable(
                new BigDecimal("3000.00"),
                21
        );

        assertFalse(result);
    }

    @Test
    void shouldRejectAmountUpTo2000() {

        boolean result = strategy.isApplicable(
                new BigDecimal("2000.00"),
                15
        );

        assertFalse(result);
    }

    @Test
    void shouldCalculateEightPointTwoPercentFee() {

        BigDecimal result = strategy.calculate(
                new BigDecimal("3000.00")
        );

        assertEquals(
                new BigDecimal("246.00"),
                result
        );
    }
}