package com.parq.server.dao.model.object;

import java.io.Serializable;

public class PaymentAccount implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3629363474639731019L;
	int accountId = -1;
	int userId = -1;
	String customerId;
	String paymentMethodId;
	String ccStub;
	boolean isDefaultPaymentMethod;

	/**
	 * @return the accountId
	 */
	public int getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param paymentMethodId
	 *            the paymentMethodId to set
	 */
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * @return the ccStub
	 */
	public String getCcStub() {
		return ccStub;
	}

	/**
	 * @param ccStub
	 *            the ccStub to set
	 */
	public void setCcStub(String ccStub) {
		this.ccStub = ccStub;
	}

	/**
	 * @return the isDefaultMethod
	 */
	public boolean isDefaultPaymentMethod() {
		return isDefaultPaymentMethod;
	}

	/**
	 * @param isDefaultMethod
	 *            the isDefaultMethod to set
	 */
	public void setDefaultPaymentMethod(boolean isDefaultPaymentMethod) {
		this.isDefaultPaymentMethod = isDefaultPaymentMethod;
	}

}
