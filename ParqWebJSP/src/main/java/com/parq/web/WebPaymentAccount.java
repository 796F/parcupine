package com.parq.web;

public class WebPaymentAccount {
	private String type;
	private String creditCardNumber;
	private boolean defaultPayment = false;

	/**
	 * @return the creditCardNumber
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	/**
	 * @param creditCardNumber
	 *            the creditCardNumber to set
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the isDefaultPayment
	 */
	public boolean getDefaultPayment() {
		return defaultPayment;
	}

	/**
	 * @param isDefaultPayment the isDefaultPayment to set
	 */
	public void setDefaultPayment(boolean isDefaultPayment) {
		this.defaultPayment = isDefaultPayment;
	}

}
