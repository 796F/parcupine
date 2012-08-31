package com.parq.web.model;

import java.util.ArrayList;
import java.util.List;

public class MapLocation {
	private String locationName;
	private double latitude = 42.35788;
	private double longitude = -71.094318;
	private List<WebParkingLocation> parkingLocations;
	
	
	public MapLocation() {
		parkingLocations = new ArrayList<WebParkingLocation>();
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
	 * @param parkingLocations the parkingLocations to set
	 */
	public void setParkingLocations(List<WebParkingLocation> parkingLocations) {
		this.parkingLocations = parkingLocations;
	}
	/**
	 * @return the parkingLocations
	 */
	public List<WebParkingLocation> getParkingLocations() {
		return parkingLocations;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}
	
	
}
