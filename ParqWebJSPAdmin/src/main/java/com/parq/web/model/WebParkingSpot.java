package com.parq.web.model;

public class WebParkingSpot {
	private long spotId;
	private double latitude;
	private double longitude;
	private String spotName;
	private boolean isAvailable;
	
	public long getSpotId() {
		return spotId;
	}
	
	public void setSpotId(long spotId) {
		this.spotId = spotId;
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
	 * @return the locationName
	 */
	public String getSpotName() {
		return spotName;
	}
	/**
	 * @param locationName the locationName to set
	 */
	public void setSpotName(String spotName) {
		this.spotName = spotName;
	}
	
	/**
	 * @param isAvailable
	 */
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	/**
	 * @return
	 */
	public boolean isAvailable() {
		return isAvailable;
	}
}
