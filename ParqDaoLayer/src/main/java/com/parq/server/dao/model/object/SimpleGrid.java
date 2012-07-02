package com.parq.server.dao.model.object;

import java.io.Serializable;

public class SimpleGrid implements Serializable{

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 5584651009198762772L;
	
	protected long gridId = -1;
	protected double latitude = -10000.00;
	protected double longitude = -10000.00;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gridId ^ (gridId >>> 32));
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
		SimpleGrid other = (SimpleGrid) obj;
		if (gridId != other.gridId)
			return false;
		return true;
	}
	
	
}
