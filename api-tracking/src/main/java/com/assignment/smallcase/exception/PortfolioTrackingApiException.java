package com.assignment.smallcase.exception;

public class PortfolioTrackingApiException extends RuntimeException{

		private static final long serialVersionUID = 1L;
		
		public PortfolioTrackingApiException(final Throwable throwable) {
			super(throwable);
		}
}
