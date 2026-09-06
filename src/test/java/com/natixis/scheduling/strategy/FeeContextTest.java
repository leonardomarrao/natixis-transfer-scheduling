package com.natixis.scheduling.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.natixis.scheduling.model.Transfer;

class FeeContextTest {

    private FeeContext feeContext;

    @BeforeEach
    void setUp() {

        List<FeeStrategy> strategies = List.of(
                new FeeASameDayStrategy(),
                new FeeBOneToTenDaysStrategy(),
                new FeeCElevenToTwentyDaysStrategy(),
                new FeeCTwentyOneToThirtyDaysStrategy(),
                new FeeCThirtyOneToFortyDaysStrategy(),
                new FeeCOverFortyDaysStrategy()
        );

        feeContext = new FeeContext(strategies);
    }

    @Test
    void shouldUseSameDayStrategy() {

        Transfer transfer = createTransfer(
                "500.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 9, 6)
        );

        BigDecimal result = feeContext.calculateFee(transfer);

        assertEquals(
                new BigDecimal("18.00"),
                result
        );
    }

    @Test
    void shouldUseOneToTenDaysStrategy() {

        Transfer transfer = createTransfer(
                "1500.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 9, 11)
        );

        BigDecimal result = feeContext.calculateFee(transfer);

        assertEquals(
                new BigDecimal("135.00"),
                result
        );
    }

    @Test
    void shouldUseElevenToTwentyDaysStrategy() {

        Transfer transfer = createTransfer(
                "3000.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 9, 21)
        );

        BigDecimal result = feeContext.calculateFee(transfer);

        assertEquals(
                new BigDecimal("246.00"),
                result
        );
    }

    @Test
    void shouldUseTwentyOneToThirtyDaysStrategy() {

        Transfer transfer = createTransfer(
                "3000.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 10, 1)
        );

        BigDecimal result = feeContext.calculateFee(transfer);

        assertEquals(
                new BigDecimal("207.00"),
                result
        );
    }

    @Test
    void shouldUseThirtyOneToFortyDaysStrategy() {

        Transfer transfer = createTransfer(
                "3000.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 10, 11)
        );

        BigDecimal result = feeContext.calculateFee(transfer);

        assertEquals(
                new BigDecimal("141.00"),
                result
        );
    }

    @Test
    void shouldUseOverFortyDaysStrategy() {

        Transfer transfer = createTransfer(
                "3000.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 10, 17)
        );

        BigDecimal result = feeContext.calculateFee(transfer);

        assertEquals(
                new BigDecimal("51.00"),
                result
        );
    }

    @Test
    void shouldThrowExceptionWhenTransferDateIsBeforeScheduledDate() {

        Transfer transfer = createTransfer(
                "500.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 9, 5)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> feeContext.calculateFee(transfer)
        );
    }

    @Test
    void shouldThrowExceptionWhenNoStrategyIsApplicable() {

        Transfer transfer = createTransfer(
                "500.00",
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 9, 11)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> feeContext.calculateFee(transfer)
        );
    }

    private Transfer createTransfer(
            String amount,
            LocalDate scheduledDate,
            LocalDate transferDate) {

        Transfer transfer = new Transfer();

        transfer.setAmount(new BigDecimal(amount));
        transfer.setScheduledDate(scheduledDate);
        transfer.setTransferDate(transferDate);

        return transfer;
    }
}