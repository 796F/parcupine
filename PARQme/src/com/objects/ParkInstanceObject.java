package com.objects;

public class ParkInstanceObject {
	private long parkReference;
	private long endTime;

	public ParkInstanceObject(long parkReference, long endTime) {
		this.parkReference = parkReference;
		this.endTime = endTime;
	}

	/**
	 * @return the parkInstanceId
	 */
	public long getParkInstanceId() {
		return parkReference;
	}

	/**
	 * @param parkReference the parkInstanceId to set
	 */
	public void setParkInstanceId(long parkReference) {
		this.parkReference = parkReference;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
