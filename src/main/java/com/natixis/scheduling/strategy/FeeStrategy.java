package com.natixis.scheduling.strategy;

import java.math.BigDecimal;

public interface FeeStrategy {

    boolean isApplicable(BigDecimal amount, long daysBetween);

    BigDecimal calculate(BigDecimal amount);
}