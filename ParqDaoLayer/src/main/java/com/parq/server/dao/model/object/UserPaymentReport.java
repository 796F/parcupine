package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parq.server.dao.model.object.Payment.PaymentType;

public class UserPaymentReport implements Serializable {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -2601469424804702832L;
	private List<UserPaymentEntry> userPaymentEntries;

	// default constructor
	public UserPaymentReport() {
		userPaymentEntries = new ArrayList<UserPaymentEntry>();
	}

	/**
	 * @param paymentEntry
	 */
	public void addPaymentEntry(UserPaymentEntry paymentEntry) {
		this.userPaymentEntries.add(paymentEntry);
	}

	/**
	 * @return the userPaymentEntries
	 */
	public List<UserPaymentEntry> getPaymentEntries() {
		return userPaymentEntries;
	}

	public static class UserPaymentEntry implements Serializable {

		/**
		 * Generated serial ID
		 */
		private static final long serialVersionUID = 507327762948796688L;
		private long paymentId;
		private long locationId = -1;

		private PaymentType paymentType;
		private String paymentRefNumber;
		private Date paymentDateTime;
		private int amountPaid = -1;
		private String locationIdentifier;
		private String locationName;

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
		 * @return the locationId
		 */
		public long getLocationId() {
			return locationId;
		}

		/**
		 * @param locationId
		 *            the locationId to set
		 */
		public void setLocationId(long locationId) {
			this.locationId = locationId;
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
		 * @return the locationIdentifier
		 */
		public String getLocationIdentifier() {
			return locationIdentifier;
		}

		/**
		 * @param locationIdentifier
		 *            the locationIdentifier to set
		 */
		public void setLocationIdentifier(String locationIdentifier) {
			this.locationIdentifier = locationIdentifier;
		}

		/**
		 * @return the locationName
		 */
		public String getLocationName() {
			return locationName;
		}

		/**
		 * @param locationName
		 *            the locationName to set
		 */
		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}

	}
}
