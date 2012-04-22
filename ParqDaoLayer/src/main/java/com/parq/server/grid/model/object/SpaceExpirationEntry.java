package com.parq.server.grid.model.object;

import java.util.Comparator;
import java.util.Date;

public class SpaceExpirationEntry {

	private long spaceId;
	private Date parkingExiprationTime;
	
	public SpaceExpirationEntry(long spaceId, Date expirationTime) {
		this.spaceId = spaceId;
		this.parkingExiprationTime = expirationTime;
	}
	
	/**
	 * @return the spaceId
	 */
	public long getSpaceId() {
		return spaceId;
	}
	
	/**
	 * @return the parkingExiprationTime
	 */
	public Date getParkingExiprationTime() {
		return parkingExiprationTime;
	}
	
	/**
	 * @param parkingExiprationTime the parkingExiprationTime to set
	 */
	public void setParkingExiprationTime(Date parkingExiprationTime) {
		this.parkingExiprationTime = parkingExiprationTime;
	}
	
	public static class SpaceExpirationTimeComparator implements Comparator<SpaceExpirationEntry> {
		@Override
		public int compare(SpaceExpirationEntry arg0, SpaceExpirationEntry arg1) {
			return arg0.getParkingExiprationTime().compareTo(arg1.getParkingExiprationTime());
		}	
	}
}
