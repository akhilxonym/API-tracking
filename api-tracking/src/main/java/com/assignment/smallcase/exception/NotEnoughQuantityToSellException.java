package com.assignment.smallcase.exception;

public class NotEnoughQuantityToSellException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder("Error! Not Enough Quantity to Sell");
		return sb.toString();
	}
}
