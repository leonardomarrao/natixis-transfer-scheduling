package com.natixis.scheduling.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class FeeC2Strategy implements FeeStrategy{

	@Override
	public boolean isApplicable(BigDecimal amount, long daysBetween) {
		return amount.compareTo(new BigDecimal("2000")) > 0
				&& daysBetween >= 21 & daysBetween <= 30;
	}

	@Override
	public BigDecimal calculate(BigDecimal amount) {
		return amount.multiply(new BigDecimal("0.069")).setScale(2, RoundingMode.HALF_UP);
	}
	

}
