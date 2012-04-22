package com.parq.payment.processing.service.exceptions;

public class InvalidUserPaymentMethodException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 848444390952151993L;

	public InvalidUserPaymentMethodException() {
		super();
	}

	public InvalidUserPaymentMethodException(String arg0) {
		super(arg0);
	}

	public InvalidUserPaymentMethodException(Throwable arg0) {
		super(arg0);
	}

	public InvalidUserPaymentMethodException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
