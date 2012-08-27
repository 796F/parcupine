package com.parq.web.model;

public class ParkingHistory {
	private String date;
	private String locationName;
	private String startTime;
	private String endTime;
	private double cost;
	private String parkingRefNumber;

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the location
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocationName(String location) {
		this.locationName = location;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the parkingRefNumber
	 */
	public String getParkingRefNumber() {
		return parkingRefNumber;
	}

	/**
	 * @param parkingRefNumber
	 *            the parkingRefNumber to set
	 */
	public void setParkingRefNumber(String parkingRefNumber) {
		this.parkingRefNumber = parkingRefNumber;
	}

}
