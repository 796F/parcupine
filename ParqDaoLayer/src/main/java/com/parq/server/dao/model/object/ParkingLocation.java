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
	
	protected long locationId = -1;
	protected long gridId = -1;
	protected String locationIdentifier;
	protected long clientId;
	protected String locationType;
	
	protected List<GeoPoint> geoPoints;
	
	protected List<ParkingSpace> spaces;
	
	
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

	/**
	 * @return the gridId
	 */
	public long getGridId() {
		return gridId;
	}

	/**
	 * @param gridId the gridId to set
	 */
	public void setGridId(long gridId) {
		this.gridId = gridId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (locationId ^ (locationId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkingLocation other = (ParkingLocation) obj;
		if (locationId != other.locationId)
			return false;
		return true;
	}
	
	
	
}
