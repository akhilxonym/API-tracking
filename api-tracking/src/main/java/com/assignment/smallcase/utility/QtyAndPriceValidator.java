package com.assignment.smallcase.utility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy= {QuantityAndPriceValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER })
public @interface QtyAndPriceValidator {
	String message() default "Invalid Quantity Entered";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
