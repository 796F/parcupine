package com.parq.server.dao.model.object;

import java.io.Serializable;

/**
 * @author GZ
 *
 */
public class ParkingSpace implements Serializable {

	
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -6365631744656272541L;
	private long spaceId = -1;
	private String spaceIdentifier;
	private String parkingLevel;
	private long locationId;
	private String spaceName;
	private double latitude = -10000.00;
	private double longitude = -10000.00;
	private int segment = -1;
	
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
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return
	 */
	public int getSegment() {
		return segment ;
	}
	
	/**
	 * @param ordering
	 */
	public void setSegment(int segment) {
		this.segment = segment;
	}
}
