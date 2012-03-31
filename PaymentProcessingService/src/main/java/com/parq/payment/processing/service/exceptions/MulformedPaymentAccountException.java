package com.parq.payment.processing.service.exceptions;

public class MulformedPaymentAccountException extends RuntimeException {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 4945201128111316069L;

	public MulformedPaymentAccountException() {
		super();
	}

	public MulformedPaymentAccountException(String arg0) {
		super(arg0);
	}

	public MulformedPaymentAccountException(Throwable arg0) {
		super(arg0);
	}

	public MulformedPaymentAccountException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
