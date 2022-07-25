package com.assignment.smallcase.exception;

public class NoTradeFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder("Error! No Trade Found with given id");
		return sb.toString();
	}
}
