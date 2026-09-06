package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeeASameDayStrategyTest {

    private FeeASameDayStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FeeASameDayStrategy();
    }

    @Test
    void shouldBeApplicableWhenAmountIsUpTo1000AndTransferIsSameDay() {
        boolean applicable = strategy.isApplicable(
                new BigDecimal("1000.00"),
                0
        );

        assertTrue(applicable);
    }

    @Test
    void shouldNotBeApplicableWhenTransferIsNotSameDay() {
        boolean applicable = strategy.isApplicable(
                new BigDecimal("500.00"),
                1
        );

        assertFalse(applicable);
    }

    @Test
    void shouldCalculateFeeCorrectly() {
        BigDecimal fee = strategy.calculate(
                new BigDecimal("500.00")
        );

        assertEquals(
                new BigDecimal("18.00"),
                fee
        );
    }
}