package com.objects;

public class ParkInstanceObject {
	private long parkReference;
	private long endTime;
	private String parkingReferenceNumber;
	
	
	public ParkInstanceObject(long parkReference, long endTime,
			String parkingReferenceNumber) {
		super();
		this.parkReference = parkReference;
		this.endTime = endTime;
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
	public long getParkReference() {
		return parkReference;
	}
	public void setParkReference(long parkReference) {
		this.parkReference = parkReference;
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
