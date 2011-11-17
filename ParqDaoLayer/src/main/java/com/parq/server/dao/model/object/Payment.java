package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7518785991839030335L;
	private int paymentId;
	private int parkingInstId;
	private PaymentType paymentType;
	private String paymentRefNumber;
	private Date paymentDateTime;
	private int amountPaid = -1;

	public enum PaymentType {
		CreditCard, PrePaid;
	}

	/**
	 * @return the paymentId
	 */
	public int getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId
	 *            the paymentId to set
	 */
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * @return the parkingInstId
	 */
	public int getParkingInstId() {
		return parkingInstId;
	}

	/**
	 * @param parkingInstId
	 *            the parkingInstId to set
	 */
	public void setParkingInstId(int parkingInstId) {
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
}
