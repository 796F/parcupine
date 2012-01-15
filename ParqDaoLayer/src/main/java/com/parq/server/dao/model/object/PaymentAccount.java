package com.parq.server.dao.model.object;

import java.io.Serializable;

/**
 * @author GZ
 *
 */
public class PaymentAccount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3629363474639731019L;
	private long accountId = -1;
	private long userId = -1;
	private String customerId;
	private String paymentMethodId;
	private String ccStub;
	private CardType cardType;
	boolean isDefaultPaymentMethod;

	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
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

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentAccount other = (PaymentAccount) obj;
		if (accountId != other.accountId)
			return false;
		return true;
	}

	/**
	 * @return the cardType
	 */
	public CardType getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public enum CardType { VISA, MASTER, DISC, AMAX }
}
