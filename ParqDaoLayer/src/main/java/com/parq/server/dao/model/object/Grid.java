package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grid extends SimpleGrid implements Serializable{
	
	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = 1944957367329157867L;
	
	protected List<ParkingLocation> parkingLocations;

	public Grid() {
		parkingLocations = new ArrayList<ParkingLocation>();
	}

	/**
	 * @return the parkingLocations
	 */
	public List<ParkingLocation> getParkingLocations() {
		return parkingLocations;
	}
}
