package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GZ
 *
 */
public class Payment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7518785991839030335L;
	private long paymentId;
	private long parkingInstId;
	private PaymentType paymentType;
	private String paymentRefNumber;
	private Date paymentDateTime;
	private int amountPaid = -1;
	private long accountId;

	public enum PaymentType {
		CreditCard, PrePaid;
	}

	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId
	 *            the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * @return the parkingInstId
	 */
	public long getParkingInstId() {
		return parkingInstId;
	}

	/**
	 * @param parkingInstId
	 *            the parkingInstId to set
	 */
	public void setParkingInstId(long parkingInstId) {
		this.parkingInstId = parkingInstId;
	}

	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType
	 *            the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the paymentRefNumber
	 */
	public String getPaymentRefNumber() {
		return paymentRefNumber;
	}

	/**
	 * @param paymentRefNumber
	 *            the paymentRefNumber to set
	 */
	public void setPaymentRefNumber(String paymentRefNumber) {
		this.paymentRefNumber = paymentRefNumber;
	}

	/**
	 * @return the paymentDateTime
	 */
	public Date getPaymentDateTime() {
		return paymentDateTime;
	}

	/**
	 * @param paymentDateTime
	 *            the paymentDateTime to set
	 */
	public void setPaymentDateTime(Date paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}

	/**
	 * @return the amountPaid
	 */
	public int getAmountPaidCents() {
		return amountPaid;
	}

	/**
	 * @param amountPaid
	 *            the amountPaid to set
	 */
	public void setAmountPaidCents(int amountPaid) {
		this.amountPaid = amountPaid;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		result = prime * result + amountPaid;
		result = prime * result
				+ (int) (parkingInstId ^ (parkingInstId >>> 32));
		result = prime * result + (int) (paymentId ^ (paymentId >>> 32));
		result = prime
				* result
				+ ((paymentRefNumber == null) ? 0 : paymentRefNumber.hashCode());
		result = prime * result
				+ ((paymentType == null) ? 0 : paymentType.hashCode());
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
		Payment other = (Payment) obj;
		if (accountId != other.accountId)
			return false;
		if (amountPaid != other.amountPaid)
			return false;
		if (parkingInstId != other.parkingInstId)
			return false;
		if (paymentId != other.paymentId)
			return false;
		if (paymentRefNumber == null) {
			if (other.paymentRefNumber != null)
				return false;
		} else if (!paymentRefNumber.equals(other.paymentRefNumber))
			return false;
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
			return false;
		return true;
	}
}
