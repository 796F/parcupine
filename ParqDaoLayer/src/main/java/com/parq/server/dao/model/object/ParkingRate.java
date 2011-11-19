package com.parq.server.dao.model.object;

import java.io.Serializable;

public class ParkingRate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2521737369389319843L;
	
	private RateType rateType;
	private int locationId;
	private String parkingLocationName;
	private int spaceId;
	private String spaceName;
	private int parkingRateCents = -1;
	private int timeIncrementsMins = -1;
	private int maxParkMins = -1;

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
	 * tell the rate type of this ParkingRate </br>
	 * 
	 * <code>RateType.Client</code> if rate type is applicable to all space within this client </br>
	 * <code>RateType.Location</code> if rate type is applicable to only this parkingLocation </br>
	 * <code>RateType.Space</code> if rate type is applicable to only this parking space </br>
	 */
	public RateType getRateType() {
		return rateType;
	}

	/**
	 * @param rateType
	 *            the rateType to set
	 */
	public void setRateType(RateType rateType) {
		if (rateType == null) {
			throw new IllegalStateException("RateType cannot be null");
		}
		this.rateType = rateType;
	}

	/**
	 * if the rate is only defined client, then Location id is -1
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * @param LocationId
	 *            the LocationId to set
	 */
	public void setLocationId(int locationId) {
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
	 * if the rate is only defined by client or by parkingLocation, then space id is -1
	 */
	public int getSpaceId() {
		return spaceId;
	}

	/**
	 * @param spaceId
	 *            the spaceId to set
	 */
	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	/**
	 * null if rate is determine by client or by parkingLocation
	 */
	public String getSpaceName() {
		return spaceName;
	}

	/**
	 * @param spaceName
	 *            the spaceName to set
	 */
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
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

	public enum RateType {
		Client, ParkingLocation, Space;
	}
}