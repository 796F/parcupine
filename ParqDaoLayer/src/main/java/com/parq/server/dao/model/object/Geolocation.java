package com.parq.server.dao.model.object;

import java.io.Serializable;

public class Geolocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5865831027266829443L;
	private int GeolocationId = -1;
	private double Latitude = -10000.00;
	private double Longitude = -10000.00;
	private int locationId = -1;
	private String locationIdentifier;

	/**
	 * @return the geolocationId
	 */
	public int getGeolocationId() {
		return GeolocationId;
	}

	/**
	 * @param geolocationId
	 *            the geolocationId to set
	 */
	public void setGeolocationId(int geolocationId) {
		GeolocationId = geolocationId;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return Latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return Longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationIdentifier() {
		return locationIdentifier;
	}

	/**
	 * @param locationName
	 *            the locationName to set
	 */
	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}

}
