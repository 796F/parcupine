package com.parq.server.dao.model.object;

import java.io.Serializable;

/**
 * @author GZ
 *
 */
public class ParkingRate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2521737369389319843L;
	
	private long rateId;
	private long locationId;
	private String parkingLocationName;
	private int parkingRateCents = -1;
	private int timeIncrementsMins = -1;
	private int maxParkMins = -1;
	private int minParkMins = -1;

	/**
	 * @param rateId the rateId to set
	 */
	public void setRateId(long rateId) {
		this.rateId = rateId;
	}

	/**
	 * @return the rateId
	 */
	public long getRateId() {
		return rateId;
	}

	/**
	 * @return the timeIncrementsMins
	 */
	public int getTimeIncrementsMins() {
		return timeIncrementsMins;
	}

	/**
	 * @param timeIncrementsMins the timeIncrementsMins to set
	 */
	public void setTimeIncrementsMins(int timeIncrementsMins) {
		this.timeIncrementsMins = timeIncrementsMins;
	}

	/**
	 * @return the maxParkMins
	 */
	public int getMaxParkMins() {
		return maxParkMins;
	}

	/**
	 * @param maxParkMins the maxParkMins to set
	 */
	public void setMaxParkMins(int maxParkMins) {
		this.maxParkMins = maxParkMins;
	}

	/**
	 * if the rate is only defined client, then Location id is -1
	 */
	public long getLocationId() {
		return locationId;
	}

	/**
	 * @param LocationId
	 *            the LocationId to set
	 */
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	/**
	 * null if rate is based only on client
	 */
	public String getLocationName() {
		return parkingLocationName;
	}

	/**
	 * @param parkingLocationName
	 *            the parkingLocationName to set
	 */
	public void setLocationName(String parkingLocationName) {
		this.parkingLocationName = parkingLocationName;
	}

	/**
	 * @return the parkingRate
	 */
	public int getParkingRateCents() {
		return parkingRateCents;
	}

	/**
	 * @param parkingRate the parkingRate to set
	 */
	public void setParkingRateCents(int parkingRate) {
		this.parkingRateCents = parkingRate;
	}

	/**
	 * @return the minParkMins
	 */
	public int getMinParkMins() {
		return minParkMins;
	}

	/**
	 * @param minParkMins the minParkMins to set
	 */
	public void setMinParkMins(int minParkMins) {
		this.minParkMins = minParkMins;
	}
}
