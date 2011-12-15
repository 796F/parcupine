package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.Date;

public class ParkingInstance implements Serializable {
	
	
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 7398916394181422252L;
	private Payment paymentInfo;
	private long ParkingInstId = -1;
	private long userId = -1;
	private long spaceId = -1;
	private Date parkingBeganTime;
	private Date parkingEndTime;
	private boolean isPaidParking;
	private String parkingRefNumber;

	/**
	 * @return the paymentInfo
	 */
	public Payment getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * @param paymentInfo
	 *            the paymentInfo to set
	 */
	public void setPaymentInfo(Payment paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	/**
	 * @return the parkingInstId
	 */
	public long getParkingInstId() {
		return ParkingInstId;
	}

	/**
	 * @param parkingInstId
	 *            the parkingInstId to set
	 */
	public void setParkingInstId(long parkingInstId) {
		ParkingInstId = parkingInstId;
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
	 * @return the parkingBeganTime
	 */
	public Date getParkingBeganTime() {
		return parkingBeganTime;
	}

	/**
	 * @param parkingBeganTime
	 *            the parkingBeganTime to set
	 */
	public void setParkingBeganTime(Date parkingBeganTime) {
		this.parkingBeganTime = parkingBeganTime;
	}

	/**
	 * @return the parkingEndTime
	 */
	public Date getParkingEndTime() {
		return parkingEndTime;
	}

	/**
	 * @param parkingEndTime
	 *            the parkingEndTime to set
	 */
	public void setParkingEndTime(Date parkingEndTime) {
		this.parkingEndTime = parkingEndTime;
	}

	/**
	 * @return the isPaidParking
	 */
	public boolean isPaidParking() {
		return isPaidParking;
	}

	/**
	 * @param isPaidParking
	 *            the isPaidParking to set
	 */
	public void setPaidParking(boolean isPaidParking) {
		this.isPaidParking = isPaidParking;
	}

	/**
	 * @param spaceId the spaceId to set
	 */
	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}

	/**
	 * @return the spaceId
	 */
	public long getSpaceId() {
		return spaceId;
	}

	/**
	 * @return the parkingRefNumber
	 */
	public String getParkingRefNumber() {
		return parkingRefNumber;
	}

	/**
	 * @param parkingRefNumber the parkingRefNumber to set
	 */
	public void setParkingRefNumber(String parkingRefNumber) {
		this.parkingRefNumber = parkingRefNumber;
	}
}
