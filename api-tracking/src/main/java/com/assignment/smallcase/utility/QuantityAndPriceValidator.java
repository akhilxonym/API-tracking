package com.assignment.smallcase.utility;

import java.text.DecimalFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.assignment.smallcase.repository.StockRepository;
import com.assignment.smallcase.request.AddTradeRequest;

public class QuantityAndPriceValidator implements ConstraintValidator<QtyAndPriceValidator, AddTradeRequest> {

	@Autowired
	private StockRepository stockRepo;

	@Override
	public boolean isValid(AddTradeRequest addTradeRequest, ConstraintValidatorContext context) {
		int qty = addTradeRequest.getQty();
		Double price = addTradeRequest.getPrice();

		if (qty <= 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Qantity should be a positive integer")
					.addConstraintViolation();
			return false;
		}

		if (qty > 9999) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Qantity size should be a less than 10K")
					.addConstraintViolation();
			return false;
		}
		if (price <= 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Price should be a positive integer").addConstraintViolation();
			return false;
		}

		if (stockRepo.findById(addTradeRequest.getStock().getId()).isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Invalid Stock").addConstraintViolation();
			return false;
		}

		return true;

	}

}
