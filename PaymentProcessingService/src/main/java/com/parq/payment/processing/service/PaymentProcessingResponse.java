package com.parq.payment.processing.service;

public class PaymentProcessingResponse {

	private PaymentStatus paymentStatus = null;
	private String paymentRefNumber = "Error, payment not processed";
	
	public PaymentProcessingResponse(String paymentRefNumber, PaymentStatus status) {
		this.paymentRefNumber = paymentRefNumber;
		this.paymentStatus = status;
	}
	
	/**
	 * @return the paymentStatus
	 */
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * @return the paymentRefNumber
	 */
	public String getPaymentRefNumber() {
		return paymentRefNumber;
	}

	public enum PaymentStatus {
		OK, InsufficentFund, NoCreditCardOnRecord, InvalidPaymentMethod;
	}
}
