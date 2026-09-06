package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeBOneToTenDaysStrategyTest {

    private FeeBOneToTenDaysStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FeeBOneToTenDaysStrategy();
    }

    @Test
    void shouldBeApplicableForValidAmountAndPeriod() {

        boolean result = strategy.isApplicable(
                new BigDecimal("1500.00"),
                5
        );

        assertTrue(result);
    }

    @Test
    void shouldAcceptOneDayBoundary() {

        boolean result = strategy.isApplicable(
                new BigDecimal("1500.00"),
                1
        );

        assertTrue(result);
    }

    @Test
    void shouldAcceptTenDaysBoundary() {

        boolean result = strategy.isApplicable(
                new BigDecimal("1500.00"),
                10
        );

        assertTrue(result);
    }

    @Test
    void shouldRejectElevenDays() {

        boolean result = strategy.isApplicable(
                new BigDecimal("1500.00"),
                11
        );

        assertFalse(result);
    }

    @Test
    void shouldRejectAmountGreaterThan2000() {

        boolean result = strategy.isApplicable(
                new BigDecimal("2000.01"),
                5
        );

        assertFalse(result);
    }

    @Test
    void shouldCalculateNinePercentFee() {

        BigDecimal result = strategy.calculate(
                new BigDecimal("1500.00")
        );

        assertEquals(
                new BigDecimal("135.00"),
                result
        );
    }
}