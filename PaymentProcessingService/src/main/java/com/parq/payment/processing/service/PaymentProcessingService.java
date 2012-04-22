package com.parq.payment.processing.service;

import java.util.Map;

import AuthNet.Rebill.CreateCustomerProfileResponseType;

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
	
	/**
	 * Create a new customer profile with the credit card processor. Note: check the return type to make sure the
	 * the profile was successfully created.
	 * 
	 * @param emailAddress email address of the user
	 * @param ccNum credit card number
	 * @param csc credit card security check number
	 * @param month expiration month
	 * @param year expiration year
	 * @param firstName
	 * @param lastName
	 * @param zipcode zip code of the bill address of the credit card owner
	 * @param address billing address of the credit card owner
	 * @return the direct response message of the Merchant API.
	 */
	public CreateCustomerProfileResponseType createCustomerProfile(
			String emailAddress, String ccNum, String csc, int month, int year, 
			String firstName, String lastName, String zipcode, String address);
	
}
