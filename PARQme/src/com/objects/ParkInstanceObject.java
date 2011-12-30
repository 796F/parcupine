package com.objects;

public class ParkInstanceObject {
	private long endTime;
	private String parkingReferenceNumber;
	private ParkSync sync;
	
	
	public ParkInstanceObject(long endTime, String parkingReferenceNumber,
			ParkSync sync) {
		super();
		this.endTime = endTime;
		this.parkingReferenceNumber = parkingReferenceNumber;
		this.sync = sync;
	}
	public ParkSync getSync() {
		return sync;
	}
	public void setSync(ParkSync sync) {
		this.sync = sync;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}
	public void setParkingReferenceNumber(String parkingReferenceNumber) {
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
}
