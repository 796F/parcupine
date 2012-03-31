package com.parq.payment.processing.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AuthNet.Rebill.CreateCustomerProfileTransactionResponseType;
import AuthNet.Rebill.OrderExType;
import AuthNet.Rebill.ProfileTransAuthCaptureType;
import AuthNet.Rebill.ProfileTransactionType;
import AuthNet.Rebill.ServiceSoap;

import com.parq.payment.processing.service.PaymentProcessingResponse.PaymentStatus;
import com.parq.payment.processing.service.exceptions.InvalidUserPaymentMethodException;
import com.parq.payment.processing.service.exceptions.MulformedPaymentAccountException;
import com.parq.payment.processing.service.exceptions.PaymentFailedException;
import com.parq.payment.processing.service.support.AuthNetSoapUtilities;
import com.parq.server.dao.BatchCCProcessingDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.UserPrePaidAccountBalanceDao;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.PaymentMethod;
import com.parq.server.dao.model.object.PendingCharge;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.UserPrePaidAccountBalance;

public class PaymentProcessingServiceImpl implements PaymentProcessingService {

	/* (non-Javadoc)
	 * @see com.parq.payment.processing.service.PaymentProcessingService#processParkingPayment(com.parq.server.dao.model.object.PaymentAccount, int)
	 */
	public PaymentProcessingResponse processParkingPayment(PaymentAccount paymentAccount, int amountToCharge) {
		UserDao userDao = new UserDao();
		User user = userDao.getUserById(paymentAccount.getUserId());
		
		PaymentProcessingResponse response = null;
		if (PaymentMethod.PREFILL == user.getAccountType()) {
			response = processPrefilledPayment(paymentAccount, amountToCharge);
		} else if (PaymentMethod.NORMAL == user.getAccountType()) {
			response = processNormalPayment(paymentAccount, amountToCharge);
		} else if (PaymentMethod.BATCH == user.getAccountType()) {
			response = processBatchPayment(paymentAccount, amountToCharge);
		} else {
			throw new InvalidUserPaymentMethodException("User: "+ paymentAccount.getUserId() + 
					", Payment type does not match current support time of PREFIL, NORMAL, BATCH.");
		}
		return response;
	}

	/* (non-Javadoc)
	 * @see com.parq.payment.processing.service.PaymentProcessingService#refillCredit(com.parq.server.dao.model.object.PaymentAccount, int)
	 */
	public PaymentProcessingResponse refillCredit(PaymentAccount paymentAccount, int amountToRecharge) {
		validatePaymentRequest(paymentAccount, amountToRecharge);
		
		String paymentRefNumber = createPaymentRefNumber(paymentAccount);
		CreateCustomerProfileTransactionResponseType soapResponse =
			chargeUser(amountToRecharge, paymentAccount, paymentRefNumber);
		
		PaymentStatus status = validMerchantResponse(soapResponse);

		boolean refillSuccessful = 
			addRefillInformationToUserAccountBalance(amountToRecharge, paymentAccount.getUserId());
		if (!refillSuccessful) {
			System.err.println("CRITICAL!!!  User: " + paymentAccount.getUserId() + ", payment refill: "
					+ amountToRecharge + ", PARQ DB update failed");
		}
		
		PaymentProcessingResponse response = new PaymentProcessingResponse(paymentRefNumber, status);
		
		return response;
	}
	
	/**
	 * Validate the merchant CC processing company's resposne to make sure the charges
	 * went through ok.
	 * @param soapResponse
	 * @return
	 */
	private PaymentStatus validMerchantResponse(
			CreateCustomerProfileTransactionResponseType soapResponse) {
		
		PaymentStatus status = PaymentStatus.OK;
		// validate the soapResponse to make sure the charges goes through
		if (!soapResponse.getResultCode().value().equalsIgnoreCase("Ok")) {
			// TODO we also need to handle credit card decline and credit card expired 
			// error message
			String errorMessage = "Payment Processing Failed!! Merchant Terminal Error message: " 
				+ soapResponse.getResultCode().value();
			throw new PaymentFailedException(errorMessage);
		} else {
			
		}
		
		return status;
	}

	/**
	 * Validate the user's payment request.
	 * @param paymentAccount
	 * @param amountToCharge
	 */
	private void validatePaymentRequest(PaymentAccount paymentAccount,
			int amountToCharge) {
		if (paymentAccount == null || paymentAccount.getUserId() <= 0 
				|| paymentAccount.getCustomerId().isEmpty() || paymentAccount.getPaymentMethodId().isEmpty()
				|| amountToCharge <= 100) {
			StringBuffer errorMessage = new StringBuffer("Mulformed PaymentAccount - userId: ");
			errorMessage.append(paymentAccount.getUserId());
			errorMessage.append(" customerId: ");
			errorMessage.append(paymentAccount.getCustomerId());
			errorMessage.append(" paymentMethodId: ");
			errorMessage.append(paymentAccount.getPaymentMethodId());
			throw new MulformedPaymentAccountException(errorMessage.toString());
		}
	}

	/**
	 * Once the user has successfully refill their prepaid account, 
	 * add the refill amount to their account balance.
	 * @param amountToRecharge
	 * @param userId
	 * @return
	 */
	private boolean addRefillInformationToUserAccountBalance(int amountToRecharge,
			long userId) {
		
		UserPrePaidAccountBalanceDao prepaidDao = new UserPrePaidAccountBalanceDao();
		UserPrePaidAccountBalance userBalance = prepaidDao.getUserPrePaidAccountBalance(userId);
		int currentBal = userBalance.getAccountBalance();
		int newBalance = currentBal + amountToRecharge;
		
		// Need to switch to use logger later
		System.out.println ("User: " + userId + ", refilling account balnce. Current balance: " +
				currentBal + ", refill amount: " + amountToRecharge + ", new balance: " +
				newBalance);
		
		return prepaidDao.updateUserPrePaidAccountBalance(userId, newBalance);
	}

	/**
	 * Charge the desire amount through the merchant CC processor
	 * 
	 * @param pay_amount
	 * @param paymentAccount
	 * @param invoiceNumber
	 * @return
	 */
	private CreateCustomerProfileTransactionResponseType chargeUser(
			int pay_amount, PaymentAccount paymentAccount, String invoiceNumber){
		java.math.BigDecimal amount = java.math.BigDecimal.valueOf(
				Double.parseDouble("" + (pay_amount/100) + "." + (pay_amount%100)));

		//try to charge their payment profile the pay_amount
		ProfileTransAuthCaptureType auth_capture = new ProfileTransAuthCaptureType();
		// for Auth Net, their ProfileId is equals to Parq's PaymentAccount's CustomerId
		auth_capture.setCustomerProfileId(Long.parseLong(paymentAccount.getCustomerId()));
		// for Auth Net, their PaymentProfileId is equals to Parq's PaymentAccount's PaymentMethodId
		auth_capture.setCustomerPaymentProfileId(Long.parseLong(paymentAccount.getPaymentMethodId()));
		auth_capture.setAmount(amount);
		
		OrderExType order = new OrderExType();
		order.setInvoiceNumber(invoiceNumber);
		auth_capture.setOrder(order);
		
		ProfileTransactionType trans = new ProfileTransactionType();
		trans.setProfileTransAuthCapture(auth_capture);

		ServiceSoap soap = AuthNetSoapUtilities.getServiceSoap();
		return soap.createCustomerProfileTransaction(AuthNetSoapUtilities.getMerchantAuthentication(), trans, null);
	}
		
	/**
	 * Process a normal transaction(Not batched nor prepaid)
	 * 
	 * @param paymentAccount
	 * @param amountToCharge
	 * @return
	 */
	private PaymentProcessingResponse processNormalPayment(PaymentAccount paymentAccount, int amountToCharge) {
		validatePaymentRequest(paymentAccount, amountToCharge);
		
		String paymentRefNumber = createPaymentRefNumber(paymentAccount);
		
		CreateCustomerProfileTransactionResponseType soapResponse =
			chargeUser(amountToCharge, paymentAccount, paymentRefNumber);
		
		PaymentStatus status = validMerchantResponse(soapResponse);
		PaymentProcessingResponse response = new PaymentProcessingResponse(paymentRefNumber, status);
		
		return response;
	}
	
	/**
	 * Create the payment reference number to be use by the Authorize .Net merchant
	 * @param paymentAccount
	 * @return
	 */
	private String createPaymentRefNumber(PaymentAccount paymentAccount) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("MMddyyyyhhmm");
		Date curTime = new Date();
		String dateString = dateFormater.format(curTime);
		//for us, invoice set to uid:MMddyyyyhhmm
		String paymentRefNumber = paymentAccount.getUserId() + ":" + dateString;
		return paymentRefNumber;
	}

	
	private PaymentProcessingResponse processPrefilledPayment(PaymentAccount paymentAccount, int amountToCharge) {
		validatePaymentRequest(paymentAccount, amountToCharge);
		
		UserPrePaidAccountBalanceDao prePaidAccountDao = new UserPrePaidAccountBalanceDao();
		UserPrePaidAccountBalance userPrePaidBalance = 
				prePaidAccountDao.getUserPrePaidAccountBalance(paymentAccount.getUserId());
		
		if (amountToCharge > userPrePaidBalance.getAccountBalance()) {
			// user does not have sufficient fund to cover parking charge
			PaymentProcessingResponse response = new PaymentProcessingResponse("Insufficent PrePaid Account Fund", 
					PaymentStatus.InsufficentFund);
			return response;
		} 
	
		int currentBal = userPrePaidBalance.getAccountBalance();
		int newBalance = currentBal - amountToCharge;
		// Need to switch to use logger later
		System.out.println ("User: " + paymentAccount.getUserId() 
				+ ", paying for parking. Current balance: " + currentBal +
				", parking charge: " + amountToCharge + ", new balance: " + 
				newBalance);
		// charge the parking payment to the user prepaid account
		prePaidAccountDao.updateUserPrePaidAccountBalance(paymentAccount.getUserId(), newBalance);
	
		String prepaidRefNumber = "Prepaid:" + createPaymentRefNumber(paymentAccount);
		PaymentProcessingResponse response = new PaymentProcessingResponse(prepaidRefNumber, 
				PaymentStatus.InsufficentFund);

		return response;
	}
	

	private PaymentProcessingResponse processBatchPayment(PaymentAccount paymentAccount, int amountToCharge) {
		validatePaymentRequest(paymentAccount, amountToCharge);
		
		String paymentRefNumber = "Batch:" + createPaymentRefNumber(paymentAccount);
		
		CreateCustomerProfileTransactionResponseType soapResponse =
			chargeUser(amountToCharge, paymentAccount, paymentRefNumber);
		
		PaymentStatus status = validMerchantResponse(soapResponse);
		PaymentProcessingResponse response = new PaymentProcessingResponse(paymentRefNumber, status);
		
		return response;
	}	
	
	/* (non-Javadoc)
	 * @see com.parq.payment.processing.service.PaymentProcessingService#settleAllBatchPayments(java.lang.String)
	 */
	public Map<Long, PaymentProcessingResponse> settleAllBatchPayments(String batchPaymentToken) {
		BatchCCProcessingDao batchDao = new BatchCCProcessingDao();
		
		GregorianCalendar twoWeekAgo = new GregorianCalendar();
		twoWeekAgo.add(GregorianCalendar.DATE, -16);
		GregorianCalendar today = new GregorianCalendar();
		
		// get all the batch unpaid charges between today, and 16 day ago
		Map<PaymentAccount, List<PendingCharge>> pendingChargeMap = batchDao
				.getPendingCharge(batchPaymentToken, twoWeekAgo.getTime(), today.getTime());

		// create a response map to keep track of all the processed charges.
		Map<Long, PaymentProcessingResponse> reponseMap = new HashMap<Long, PaymentProcessingResponse>();

		// keep track of all the pending charges that are already paid
		List<PendingCharge> paidCharges = new ArrayList<PendingCharge>();
		 
		// loop through all the pending charges accounts, and sum up all the charges for
		// each individual accounts.
		for (PaymentAccount batchAccount : pendingChargeMap.keySet()) {
			List<PendingCharge> pendingCharges = pendingChargeMap.get(batchAccount);

			if (pendingCharges != null && !pendingCharges.isEmpty()) {
				// sum the pending charges per payment account
				int totalPendingCharge = sumBatchPendingCharges(pendingCharges);
				if (totalPendingCharge > 0) {
					// process the batch charge.
					PaymentProcessingResponse batchProcessingResponse = processBatchPayment(
							batchAccount, totalPendingCharge);

					// insert the payment reference number to each of the
					// paid pending charges
					for (PendingCharge paidCharge : pendingCharges) {
						paidCharge.setPaymentRefNumber(batchProcessingResponse.getPaymentRefNumber());
					}
					// add all the pending charges to the paid charges list. if they are paid
					if (PaymentStatus.OK == batchProcessingResponse.getPaymentStatus()) {
						paidCharges.addAll(pendingCharges);
					} else {
						System.out.println("User: " + batchAccount.getUserId() + 
								", batch payment failed, with status "
								+ batchProcessingResponse.getPaymentStatus());
					}
					// add the response status to the response map
					reponseMap.put(batchAccount.getUserId(), batchProcessingResponse);
				}
			}
		}
		// update the batch pending charges status in the DB to reflect that they are paid 
		batchDao.batchUpdatePaymentInfo(paidCharges);
		// return the status back to the user
		return reponseMap;
	}

	/**
	 * Sum up the batch pending charges for the a user
	 * @param pendingCharges
	 * @return
	 */
	private int sumBatchPendingCharges(List<PendingCharge> pendingCharges) {
		int sum = 0;
		for (PendingCharge charge : pendingCharges) {
			sum += charge.getAmountPaid();
		}
		return sum;
	}
}
