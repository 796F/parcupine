package com.parq.payment.processing.service.exceptions;

public class PaymentFailedException extends RuntimeException {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -6668971004173028377L;

	public PaymentFailedException() {
		super();
	}

	public PaymentFailedException(String arg0) {
		super(arg0);
	}

	public PaymentFailedException(Throwable arg0) {
		super(arg0);
	}

	public PaymentFailedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
