package com.parq.server.grid.model.object;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.parq.server.dao.model.object.Grid;

public class GridWithFillRate extends Grid implements Serializable {

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = -1051341335608141264L;

	private int numberOfSpaces = -1;
	private Set<Long> parkedSpaces;
	
	// keep track of when the last time the parking
	// status was updated, in seconds interval.
	private long lastUpdatedDateTime;

	public GridWithFillRate(Grid grid) {
		this.gridId = grid.getGridId();
		this.latitude = grid.getLatitude();
		this.longitude = grid.getLongitude();
		this.parkingLocations = grid.getParkingLocations();
		parkedSpaces = new HashSet<Long>();
	}

	/**
	 * @return the fillRate
	 */
	public int getFillRate() {
		return (int) (1.0 * parkedSpaces.size() /  numberOfSpaces * 100.00);
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
		return numberOfSpaces - parkedSpaces.size();
	}
	
	/**
	 * Removed the spaceId from the list of parked spaces.
	 */
	public boolean unPark(long spaceId) {
		return parkedSpaces.remove(spaceId);
	}
	
	/**
	 * Add this space to the park space list for this parking grid
	 * @param spaceId
	 * @return
	 */
	public boolean park(long spaceId) {
		return parkedSpaces.add(spaceId);
	}

	/**
	 * @param lastUpdatedDateTime the lastUpdatedDateTime to set
	 */
	public void setLastUpdatedDateTime(long lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	/**
	 * @return the lastUpdatedDateTime
	 */
	public long getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
}
