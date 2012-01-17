package com.parq.web;

public class ParkingReport {
	private String parkingRefNum;
	private String userEmail;
	private String paymentDatetime;
	private double amountPaid;
	private String parkingStartTime;
	private String parkingEndTime;
	
	
	/**
	 * @return the parkingRefNum
	 */
	public String getParkingRefNum() {
		return parkingRefNum;
	}
	/**
	 * @param parkingRefNum the parkingRefNum to set
	 */
	public void setParkingRefNum(String parkingRefNum) {
		this.parkingRefNum = parkingRefNum;
	}
	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/**
	 * @return the paymentDatetime
	 */
	public String getPaymentDatetime() {
		return paymentDatetime;
	}
	/**
	 * @param paymentDatetime the paymentDatetime to set
	 */
	public void setPaymentDatetime(String paymentDatetime) {
		this.paymentDatetime = paymentDatetime;
	}
	/**
	 * @return the amountPaid
	 */
	public double getAmountPaid() {
		return amountPaid;
	}
	/**
	 * @param amountPaid the amountPaid to set
	 */
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}
	/**
	 * @return the parkingStartTime
	 */
	public String getParkingStartTime() {
		return parkingStartTime;
	}
	/**
	 * @param parkingStartTime the parkingStartTime to set
	 */
	public void setParkingStartTime(String parkingStartTime) {
		this.parkingStartTime = parkingStartTime;
	}
	/**
	 * @return the parkingEndTime
	 */
	public String getParkingEndTime() {
		return parkingEndTime;
	}
	/**
	 * @param parkingEndTime the parkingEndTime to set
	 */
	public void setParkingEndTime(String parkingEndTime) {
		this.parkingEndTime = parkingEndTime;
	}
	
	
}
