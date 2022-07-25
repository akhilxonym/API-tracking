package com.assignment.smallcase.exception;

public class TradeRemovalException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder("Error! Trade Cannot be Removed");
		return sb.toString();
	}


}
