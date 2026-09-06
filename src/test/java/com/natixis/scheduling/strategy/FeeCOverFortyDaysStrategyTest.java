package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeCOverFortyDaysStrategyTest {

    private FeeCOverFortyDaysStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FeeCOverFortyDaysStrategy();
    }

    @Test
    void shouldAcceptFortyOneDays() {

        assertTrue(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        41
                )
        );
    }

    @Test
    void shouldAcceptLongPeriod() {

        assertTrue(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        100
                )
        );
    }

    @Test
    void shouldRejectFortyDays() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("3000.00"),
                        40
                )
        );
    }

    @Test
    void shouldRejectAmountUpTo2000() {

        assertFalse(
                strategy.isApplicable(
                        new BigDecimal("2000.00"),
                        50
                )
        );
    }

    @Test
    void shouldCalculateOnePointSevenPercentFee() {

        BigDecimal result = strategy.calculate(
                new BigDecimal("3000.00")
        );

        assertEquals(
                new BigDecimal("51.00"),
                result
        );
    }
}