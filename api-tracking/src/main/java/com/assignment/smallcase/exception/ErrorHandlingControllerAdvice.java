package com.assignment.smallcase.exception;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.assignment.smallcase.response.AddTradeResponse;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  AddTradeResponse onConstraintValidationException(MethodArgumentNotValidException e) {
    AddTradeResponse error = new AddTradeResponse();
    error.setMessage(e.getBindingResult().getGlobalError().getDefaultMessage());
    return error;
  }



}