package com.natixis.scheduling.strategy;

import com.natixis.scheduling.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class FeeContext {

    private final List<FeeStrategy> strategies;

    public FeeContext(List<FeeStrategy> strategies) {
        this.strategies = strategies;
    }

    public BigDecimal calculateFee(Transfer transfer) {
        long daysBetween = ChronoUnit.DAYS.between(
            transfer.getScheduledDate(), 
            transfer.getTransferDate()
        );

        if (daysBetween < 0) {
            throw new IllegalArgumentException("A data de transferência não pode ser anterior à data de agendamento.");
        }

        return strategies.stream()
                .filter(strategy -> strategy.isApplicable(transfer.getAmount(), daysBetween))
                .findFirst()
                .map(strategy -> strategy.calculate(transfer.getAmount()))
                .orElseThrow(() -> new IllegalArgumentException("Não há taxa aplicável para o valor e prazo informados."));
    }
}