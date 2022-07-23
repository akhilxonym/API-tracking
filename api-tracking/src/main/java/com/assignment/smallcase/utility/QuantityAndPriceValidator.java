package com.assignment.smallcase.utility;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.assignment.smallcase.repository.StockRepository;
import com.assignment.smallcase.request.AddTradeRequest;

public class QuantityAndPriceValidator implements ConstraintValidator<QtyAndPriceValidator, AddTradeRequest> {
	
	@Autowired
	private StockRepository stockRepo;
	
	@Override
	public boolean isValid(AddTradeRequest addTradeRequest, ConstraintValidatorContext context) {
		if(addTradeRequest.getQty()<=0)
			{	context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Qantity should be a positive integer").addConstraintViolation();
				return false;
			}
		if(addTradeRequest.getQty()>9999)
			{return false;}
		if(addTradeRequest.getPrice()<=0)
			{return false;}
		if(stockRepo.findById(addTradeRequest.getStock().getId()).get()==null)
			{return false;}
		return true;
		
	}

}
