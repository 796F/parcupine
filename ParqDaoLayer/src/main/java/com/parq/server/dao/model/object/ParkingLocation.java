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
	private String locationType;
	private int fillRate = 101;
	
	private List<GeoPoint> geoPoints;
	
	private List<ParkingSpace> spaces;
	
	
	public ParkingLocation()
	{
		spaces = new ArrayList<ParkingSpace>();
		geoPoints = new ArrayList<GeoPoint>();
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
	
	/**
	 * @return the geoPoints
	 */
	public List<GeoPoint> getGeoPoints() {
		return geoPoints;
	}

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}
	
}
