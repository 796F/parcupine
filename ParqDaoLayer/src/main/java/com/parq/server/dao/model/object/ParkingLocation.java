package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GZ
 *
 */
public class ParkingLocation implements Serializable {


	/**
	 * Auto generated serial id
	 */
	private static final long serialVersionUID = -6446814645979756594L;
	
	private long locationId = -1;
	private String locationIdentifier;
	private long clientId;
	private double Latitude = -10000.00;
	private double Longitude = -10000.00;
	private int fillRate = 101;
	
	private List<ParkingSpace> spaces;
	
	
	public ParkingLocation()
	{
		spaces = new ArrayList<ParkingSpace>();
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
	 * @return the buildingName
	 */
	public String getLocationIdentifier() {
		return locationIdentifier;
	}
	/**
	 * @param locationIdentifier the buildingName to set
	 */
	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}
	/**
	 * @return the clientId
	 */
	public long getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the spaces
	 */
	public List<ParkingSpace> getSpaces() {
		return spaces;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return Latitude;
	}

	/**
	 * @param latitude the latitude to set
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
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	/**
	 * @param fillRate the fillRate to set
	 */
	public void setFillRate(int fillRate) {
		this.fillRate = fillRate;
	}

	/**
	 * @return the fillRate
	 */
	public int getFillRate() {
		return fillRate;
	}
	
}
