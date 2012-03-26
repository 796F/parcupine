package com.parq.payment.processing.service;

import java.util.Map;

import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;

public interface PaymentProcessingService {

	public PaymentProcessingResponse processParkingPayment(long userId, long spaceId, int amountToCharge);
	
	public Map<User, PaymentProcessingResponse> settleAllBatchPayments(long clientId);

	public PaymentProcessingResponse processNormalPayment(long userId, long spaceId, int amountToCharge);
	
	public PaymentProcessingResponse refillCredit(PaymentAccount paymentAccount, int amountToRecharge);
}
