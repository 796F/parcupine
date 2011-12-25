package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.Date;

import com.parq.server.dao.model.object.Payment.PaymentType;

public class PendingCharge implements Serializable {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 5718790001407120743L;
	private long paymentId;
	private long parkingInstId;
	private PaymentType paymentType;
	private String paymentRefNumber;
	private Date paymentDateTime;
	private int amountPaid = -1;
	private PaymentAccount paymentAccount;
	
	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
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
	 * @param parkingInstId the parkingInstId to set
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
	 * @param paymentType the paymentType to set
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
	 * @param paymentRefNumber the paymentRefNumber to set
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
	 * @param paymentDateTime the paymentDateTime to set
	 */
	public void setPaymentDateTime(Date paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}
	/**
	 * @return the amountPaid
	 */
	public int getAmountPaid() {
		return amountPaid;
	}
	/**
	 * @param amountPaid the amountPaid to set
	 */
	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}
	/**
	 * @return the paymentAccount
	 */
	public PaymentAccount getPaymentAccount() {
		return paymentAccount;
	}
	/**
	 * @param paymentAccount the paymentAccount to set
	 */
	public void setPaymentAccount(PaymentAccount paymentAccount) {
		this.paymentAccount = paymentAccount;
	}
}
