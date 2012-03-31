package com.parq.web.model;

public class WebGeoPoint {

	private double latitude = -1000.00;
	private double longitude = -1000.00;
	
	public WebGeoPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
}
