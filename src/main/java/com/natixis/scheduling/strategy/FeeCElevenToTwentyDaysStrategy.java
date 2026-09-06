package com.natixis.scheduling.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;


@Component
public class FeeCElevenToTwentyDaysStrategy implements FeeStrategy{

	@Override
	public boolean isApplicable(BigDecimal amount, long daysBetween) {
		return amount.compareTo(new BigDecimal("2000")) > 0
				&& daysBetween >= 11 && daysBetween <= 20;
	}

	@Override
	public BigDecimal calculate(BigDecimal amount) {
		return amount.multiply(new BigDecimal("0.082")).setScale(2, RoundingMode.HALF_UP);
	}
	

}
