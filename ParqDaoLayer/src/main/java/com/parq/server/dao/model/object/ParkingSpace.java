package com.parq.server.dao.model.object;

import java.io.Serializable;

public class ParkingSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1919122147177571986L;
	
	private long spaceId = -1;
	private String spaceIdentifier;
	private String parkingLevel;
	private long locationId;
	/**
	 * @return the id
	 */
	public long getSpaceId() {
		return spaceId;
	}
	/**
	 * @param id the id to set
	 */
	public void setSpaceId(long id) {
		this.spaceId = id;
	}
	/**
	 * @return the spaceName
	 */
	public String getSpaceIdentifier() {
		return spaceIdentifier;
	}
	/**
	 * @param spaceName the spaceName to set
	 */
	public void setSpaceIdentifier(String spaceIdentifier) {
		this.spaceIdentifier = spaceIdentifier;
	}
	/**
	 * @return the parkingLevel
	 */
	public String getParkingLevel() {
		return parkingLevel;
	}
	/**
	 * @param parkingLevel the parkingLevel to set
	 */
	public void setParkingLevel(String parkingLevel) {
		this.parkingLevel = parkingLevel;
	}
	/**
	 * @return the buildingId
	 */
	public long getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the buildingId to set
	 */
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
}
