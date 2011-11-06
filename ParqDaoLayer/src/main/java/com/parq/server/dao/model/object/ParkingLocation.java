package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParkingLocation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -234300066035879276L;

	private int locationId = -1;
	private String locationName;
	private int clientId;
	private List<ParkingSpace> spaces;
	
	
	public ParkingLocation()
	{
		spaces = new ArrayList<ParkingSpace>();
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
	/**
	 * @return the buildingName
	 */
	public String getLocationName() {
		return locationName;
	}
	/**
	 * @param locationName the buildingName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the spaces
	 */
	public List<ParkingSpace> getSpaces() {
		return spaces;
	}
	
}
