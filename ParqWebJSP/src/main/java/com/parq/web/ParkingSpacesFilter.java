package com.parq.web;

public class ParkingSpacesFilter {

	private Status parkingSpaceFillter = Status.ALL;

	public enum Status {
		ALL, OCCUPIED, FREE
	}

	/**
	 * @return the filter
	 */
	public String getParkingSpaceFilter() {
		return parkingSpaceFillter.toString();
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setParkingSpaceFilter(String filter) {
		this.parkingSpaceFillter = Status.valueOf(filter);
	};
}