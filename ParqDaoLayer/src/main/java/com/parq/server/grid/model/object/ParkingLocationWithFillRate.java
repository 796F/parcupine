package com.parq.server.grid.model.object;

import java.util.HashSet;
import java.util.Set;

import com.parq.server.dao.model.object.ParkingLocation;

public class ParkingLocationWithFillRate extends ParkingLocation {

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = -7240916083257037864L;
	private long gridId = -1;
	private int numberOfSpaces = -1;
	private Set<Long> parkedSpaces;
	
	// keep track of when the last time the parking
	// status was updated, in seconds interval.
	private long lastUpdatedDateTime;

	public ParkingLocationWithFillRate(ParkingLocation location) {
		this.gridId = location.getGridId();
		this.clientId = location.getClientId();
		this.geoPoints = location.getGeoPoints();
		this.locationId = location.getLocationId();
		this.locationIdentifier = location.getLocationIdentifier();
		this.locationType = location.getLocationType();
		this.spaces = location.getSpaces();
		this.parkedSpaces = new HashSet<Long>();
	}
	
	/**
	 * @return the numberOfSpaces
	 */
	public int getNumberOfSpaces() {
		return numberOfSpaces;
	}

	/**
	 * @param numberOfSpaces
	 *            the numberOfSpaces to set
	 */
	public void setNumberOfSpaces(int numberOfSpaces) {
		this.numberOfSpaces = numberOfSpaces;
	}

	/**
	 * @return the numberOfFreeSpaces
	 */
	public int getNumberOfFreeSpaces() {
		return parkedSpaces.size() - numberOfSpaces;
	}

	/**
	 * @return the fillRate
	 */
	public int getFillRate() {
		return (int) (1.0 * parkedSpaces.size() /  numberOfSpaces * 100.00);
	}
	
	
	/**
	 * Removed the spaceId from the list of parked spaces.
	 */
	public boolean unPark(long spaceId) {
		return parkedSpaces.remove(spaceId);
	}
	
	/**
	 * Add this space to the park space list for this parking location
	 * @param spaceId
	 * @return
	 */
	public boolean park(long spaceId) {
		return parkedSpaces.add(spaceId);
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

	/**
	 * @return the lastUpdateDateTime
	 */
	public long getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	/**
	 * @param lastUpdateDateTime the lastUpdateDateTime to set
	 */
	public void setLastUpdatedDateTime(long lastUpdateDateTime) {
		this.lastUpdatedDateTime = lastUpdateDateTime;
	}
	
	/**
	 * Check to see if the space is available
	 */
	public boolean isSpaceAvaliable(long spaceId) {
		return !parkedSpaces.contains(spaceId);
	}
}