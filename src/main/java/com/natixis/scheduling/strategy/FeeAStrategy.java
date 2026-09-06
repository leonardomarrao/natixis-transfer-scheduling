package com.natixis.scheduling.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeeAStrategy implements FeeStrategy{
	
	@Override 
	public boolean isApplicable(BigDecimal amount, long daysBetween) {
		return amount.compareTo(new BigDecimal("1000")) <= 0 && daysBetween == 0;
	}
	
	@Override
	public BigDecimal calculate(BigDecimal amount) {
		//3 euros + 3%
		BigDecimal percentage = amount.multiply(new BigDecimal("0.03"));
        BigDecimal fixedFee = new BigDecimal("3.00");
        return percentage.add(fixedFee).setScale(2, RoundingMode.HALF_UP);
	}
}
