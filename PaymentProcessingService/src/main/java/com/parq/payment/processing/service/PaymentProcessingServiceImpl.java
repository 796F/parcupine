package com.parq.payment.processing.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import AuthNet.Rebill.CreateCustomerProfileTransactionResponseType;
import AuthNet.Rebill.OrderExType;
import AuthNet.Rebill.ProfileTransAuthCaptureType;
import AuthNet.Rebill.ProfileTransactionType;
import AuthNet.Rebill.ServiceSoap;

import com.parq.payment.processing.service.exceptions.MulformedPaymentAccountException;
import com.parq.payment.processing.service.exceptions.PaymentFailedException;
import com.parq.payment.processing.service.support.AuthNetSoapUtilities;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;

public class PaymentProcessingServiceImpl implements PaymentProcessingService {

	
	public PaymentProcessingResponse processParkingPayment(long userId, long spaceId, int amountToCharge) {
		// TODO
		return null;
	}

	public PaymentProcessingResponse refillCredit(PaymentAccount paymentAccount, int amountToRecharge) {
		
		if (paymentAccount == null || paymentAccount.getUserId() <= 0 
				|| paymentAccount.getCustomerId().isEmpty() || paymentAccount.getPaymentMethodId().isEmpty()
				|| amountToRecharge <= 100) {
			StringBuffer errorMessage = new StringBuffer("Mulformed PaymentAccount - userId: ");
			errorMessage.append(paymentAccount.getUserId());
			errorMessage.append(" customerId: ");
			errorMessage.append(paymentAccount.getCustomerId());
			errorMessage.append(" paymentMethodId: ");
			errorMessage.append(paymentAccount.getPaymentMethodId());
			throw new MulformedPaymentAccountException(errorMessage.toString());
		}
		
		CreateCustomerProfileTransactionResponseType soapResponse =
			chargeUser(amountToRecharge, paymentAccount);
		
		// validate the soapResponse to make sure the charges goes through
		if (!soapResponse.getResultCode().value().equalsIgnoreCase("Ok")) {
			String errorMessage = "Payment Processing Failed!! Merchant Terminal Error message: " 
				+ soapResponse.getResultCode().value();
			throw new PaymentFailedException(errorMessage);
		}
		
		addRefillInformationToUserAccountBalance(amountToRecharge, paymentAccount.getUserId());
		
		// TODO
		return null;
	}
	
	private boolean addRefillInformationToUserAccountBalance(int amountToRecharge,
			long userId) {
		
		
		
		// TODO Auto-generated method stub
		return false;
	}

	private CreateCustomerProfileTransactionResponseType chargeUser(
			int pay_amount, PaymentAccount paymentAccount){
		java.math.BigDecimal amount = java.math.BigDecimal.valueOf(Double.parseDouble(""+(pay_amount/100)+"."+(pay_amount%100)));

		//try to charge their payment profile the pay_amount
		ProfileTransAuthCaptureType auth_capture = new ProfileTransAuthCaptureType();
		// for Auth Net, their ProfileId is equals to Parq's PaymentAccount's CustomerId
		auth_capture.setCustomerProfileId(Long.parseLong(paymentAccount.getCustomerId()));
		// for Auth Net, their PaymentProfileId is equals to Parq's PaymentAccount's PaymentMethodId
		auth_capture.setCustomerPaymentProfileId(Long.parseLong(paymentAccount.getPaymentMethodId()));
		auth_capture.setAmount(amount);
		OrderExType order = new OrderExType();

		SimpleDateFormat dateFormater = new SimpleDateFormat("MMddyyyyhhmm");
		Date curTime = new Date();
		String dateString = dateFormater.format(curTime);
		//for us, invoice set to uid:MMddyyyyhhmm
		order.setInvoiceNumber(paymentAccount.getUserId()+":"+dateString);

		auth_capture.setOrder(order);
		ProfileTransactionType trans = new ProfileTransactionType();
		trans.setProfileTransAuthCapture(auth_capture);

		ServiceSoap soap = AuthNetSoapUtilities.getServiceSoap();
		return soap.createCustomerProfileTransaction(AuthNetSoapUtilities.getMerchantAuthentication(), trans, null);
	}
	
	
	public Map<User, PaymentProcessingResponse> settleAllBatchPayments(long clientId) {
		// TODO
		return null;
	}
	
	public PaymentProcessingResponse processNormalPayment(long userId, long spaceId, int amountToCharge) {
		// TODO
		return null;
	}
	
	private PaymentProcessingResponse processBatchPayment() {
		// TODO
		return null;
	}
	
	private PaymentProcessingResponse processPrefilledPayment() {
		// TODO
		return null;
	}
}
