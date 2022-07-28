package com.assignment.smallcase.exception;

public class NoStockFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder("Error! No Stock Found with given id");
		return sb.toString();
	}
}
