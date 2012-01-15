package com.parq.web;

public class ParkingReport {
	private String parkingRefNum;
	private String userEmail;
	private String paymentDatetime;
	private double amountPaid;
	
	
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
	
	
}
