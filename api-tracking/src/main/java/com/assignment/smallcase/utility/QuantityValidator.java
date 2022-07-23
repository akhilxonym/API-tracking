package com.assignment.smallcase.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.assignment.smallcase.request.AddTradeRequest;

public class QuantityValidator implements ConstraintValidator<QtyAndPriceValidator, AddTradeRequest> {
	
	@Override
	public boolean isValid(AddTradeRequest addTradeRequest, ConstraintValidatorContext context) {
		if(addTradeRequest.getQty()<=0)
			return false;
		if(addTradeRequest.getQty()>9999)
			return false;
		if(addTradeRequest.getPrice()<=0)
			return false;
		return true;
		
	}

}
