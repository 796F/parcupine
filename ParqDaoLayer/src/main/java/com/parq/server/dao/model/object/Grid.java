package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grid implements Serializable{
	
	
	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = 1944957367329157867L;
	
	private long gridId = -1;
	private double latitude = -10000.00;
	private double longitude = -10000.00;
	private int fillRate = 101;
	private List<ParkingLocation> parkingLocations;

	public Grid() {
		parkingLocations = new ArrayList<ParkingLocation>();
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
	 * @return the centerLatitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the centerLatitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the centerLongitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the centerLongitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the fillRate
	 */
	public int getFillRate() {
		return fillRate;
	}

	/**
	 * @param fillRate the fillRate to set
	 */
	public void setFillRate(int fillRate) {
		this.fillRate = fillRate;
	}

	/**
	 * @return the parkingLocations
	 */
	public List<ParkingLocation> getParkingLocations() {
		return parkingLocations;
	}

	/**
	 * @param parkingLocations the parkingLocations to set
	 */
	public void setParkingLocations(List<ParkingLocation> parkingLocations) {
		this.parkingLocations = parkingLocations;
	}
}
