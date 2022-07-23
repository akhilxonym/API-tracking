package com.assignment.smallcase.utility;

import javax.validation.Payload;

public @interface QtyAndPriceValidator {
	String message() default "Invalid Quantity Entered";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
