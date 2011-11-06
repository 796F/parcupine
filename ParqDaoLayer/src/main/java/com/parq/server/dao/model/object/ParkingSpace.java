package com.parq.server.dao.model.object;

import java.io.Serializable;

public class ParkingSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1919122147177571986L;
	
	private int spaceId = -1;
	private String spaceName;
	private String parkingLevel;
	private int locationId;
	/**
	 * @return the id
	 */
	public int getSpaceId() {
		return spaceId;
	}
	/**
	 * @param id the id to set
	 */
	public void setSpaceId(int id) {
		this.spaceId = id;
	}
	/**
	 * @return the spaceName
	 */
	public String getSpaceName() {
		return spaceName;
	}
	/**
	 * @param spaceName the spaceName to set
	 */
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
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
	public int getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the buildingId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
}
