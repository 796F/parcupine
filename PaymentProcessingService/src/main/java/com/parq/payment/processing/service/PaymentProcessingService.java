package com.parq.payment.processing.service;

import java.util.Map;

import com.parq.server.dao.model.object.PaymentAccount;

public interface PaymentProcessingService {

	/**
	 * Process the user parking charge, this method will automatically check to see if the user is using
	 * NORMAL, PREFILLED, and BATCH payment methods
	 * 
	 * @param paymentAccount
	 * @param amountToCharge
	 * @return <code>PaymentProcessingResponse</code> that will show the status of the payment charge. if anything 
	 * 		went wrong with the parking charge the PaymentProcessingResponse.getPaymentStatus() will show the 
	 * 		error status. 
	 * 		If the payment went through ok the getPaymentStatus() will be PaymentStatus.OK; 
	 * 		Other status include: PaymentStatus.InsufficentFund, 
	 * 						PaymentStatus.CreditCardExpired, PaymentStatus.InvalidCreditCard
	 */
	public PaymentProcessingResponse processParkingPayment(PaymentAccount paymentAccount, int amountToCharge);
	
	/**
	 * Refill the user prefill account. 
	 * 
	 * @param paymentAccount
	 * @param amountToRecharge
	 * 
	 * @return <code>PaymentProcessingResponse</code> that will show the status of the payment charge.
	 * 		if anything went wrong with the parking charge the PaymentProcessingResponse.getPaymentStatus() 
	 * 		will show the error status. 
	 * 		If the payment went through ok the getPaymentStatus() will be PaymentStatus.OK; 
	 * 		Other status include: PaymentStatus.CreditCardExpired, PaymentStatus.InvalidCreditCard
	 */
	public PaymentProcessingResponse refillCredit(PaymentAccount paymentAccount, int amountToRecharge);
	
	/**
	 * Search the DB for the batch payment by the batchPaymentToken
	 * 
	 * @param batchPaymentToken
	 * @return a map of all userId to payment processing response.
	 */
	public Map<Long, PaymentProcessingResponse> settleAllBatchPayments(String batchPaymentToken);
	
}
