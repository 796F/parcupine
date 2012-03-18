package com.parq.payment.processing.service;

import java.util.Map;

import com.parq.server.dao.model.object.User;

public class PaymentProcessingService {

	
	public PaymentStatus processParkingPayment(long userId, long spaceId, int amountToCharge) {
		
		// TODO
		return null;
	}
	
	public Map<User, PaymentStatus> settleAllBatchPayments() {
		// TODO
		return null;
	}
	
	private PaymentStatus processBatchPayment() {
		// TODO
		return PaymentStatus.InvalidPaymentMethod;
	}
	
	private PaymentStatus processPrefilledPayment() {
		// TODO
		return PaymentStatus.InvalidPaymentMethod;
	}
	
	private PaymentStatus processNormalPayment() {
		// TODO
		return PaymentStatus.InvalidPaymentMethod;
	}
	
	private PaymentStatus refillCredit() {
		// TODO
		return PaymentStatus.InvalidPaymentMethod;
	}
}
