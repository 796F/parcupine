package com.parq.web.model;

public class ParkingSpaceStatus {
	private long spaceId;
	private String spaceLocation;
	private String spaceName;
	private String parkingLevel;
	private boolean isOccupied;
	private String spaceIdentifier;
	private String spaceFreeOn;
	
	
	/**
	 * @return the spaceId
	 */
	public long getSpaceId() {
		return spaceId;
	}
	/**
	 * @param spaceId the spaceId to set
	 */
	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * @return the spaceLocation
	 */
	public String getSpaceLocation() {
		return spaceLocation;
	}
	/**
	 * @param spaceLocation the spaceLocation to set
	 */
	public void setSpaceLocation(String spaceLocation) {
		this.spaceLocation = spaceLocation;
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
	 * @return the isOccupied
	 */
	public boolean isOccupied() {
		return isOccupied;
	}
	/**
	 * @param isOccupied the isOccupied to set
	 */
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	/**
	 * @return the spaceIdentifier
	 */
	public String getSpaceIdentifier() {
		return spaceIdentifier;
	}
	/**
	 * @param spaceIdentifier the spaceIdentifier to set
	 */
	public void setSpaceIdentifier(String spaceIdentifier) {
		this.spaceIdentifier = spaceIdentifier;
	}
	/**
	 * @return the spaceFreeOn
	 */
	public String getSpaceFreeOn() {
		return spaceFreeOn;
	}
	/**
	 * @param spaceFreeOn the spaceFreeOn to set
	 */
	public void setSpaceFreeOn(String spaceFreeOn) {
		this.spaceFreeOn = spaceFreeOn;
	}
}
