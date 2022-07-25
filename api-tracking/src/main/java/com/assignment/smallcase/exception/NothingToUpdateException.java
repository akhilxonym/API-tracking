package com.assignment.smallcase.exception;

public class NothingToUpdateException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder("Error! Nothing to Update. Same Trade Found");
		return sb.toString();
	}
}
