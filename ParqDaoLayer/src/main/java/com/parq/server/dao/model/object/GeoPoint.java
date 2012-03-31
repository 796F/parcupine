package com.parq.server.dao.model.object;

import java.io.Serializable;

public class GeoPoint implements Serializable{
	
	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 900142590010236050L;
	private long geoPointId = -1;
	private long locationId = -1;
	private double latitude = -1000.00;
	private double longitude = -1000.00;
	private int sortOrder = Integer.MIN_VALUE;
	
	public GeoPoint() {}
	
	public GeoPoint(double lat, double longitude) {
		this.latitude = lat;
		this.longitude = longitude;
	}
	
	/**
	 * @return the geoPointId
	 */
	public long getGeoPointId() {
		return geoPointId;
	}
	
	/**
	 * @param geoPointId the geoPointId to set
	 */
	public void setGeoPointId(long geoPointId) {
		this.geoPointId = geoPointId;
	}
	
	/**
	 * @return the locationId
	 */
	public long getLocationId() {
		return locationId;
	}
	
	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(long locationId) {
		this.locationId = locationId;
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
	 * @return the sortOrder
	 */
	public int getSortOrder() {
		return sortOrder;
	}
	
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
