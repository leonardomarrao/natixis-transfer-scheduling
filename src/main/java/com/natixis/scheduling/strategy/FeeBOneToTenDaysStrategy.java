package com.natixis.scheduling.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class FeeBOneToTenDaysStrategy implements FeeStrategy{
	
	@Override 
	public boolean isApplicable(BigDecimal amount, long daysBetween) {
		boolean validAmount = amount.compareTo(new BigDecimal("1000")) > 0 
				&& amount.compareTo(new BigDecimal("2000")) <= 0;
		boolean validDays = daysBetween >= 1 && daysBetween <= 10;
		return validAmount && validDays;
	}
	
	@Override
	public BigDecimal calculate(BigDecimal amount) {
		//9% do valor
		return amount.multiply(new BigDecimal("0.09")).setScale(2, RoundingMode.HALF_UP);
	}
}
