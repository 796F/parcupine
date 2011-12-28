package com.objects;

public class ParkInstanceObject {
	private long endTime;
	private String parkingReferenceNumber;
	
	

	public ParkInstanceObject(long endTime, String parkingReferenceNumber) {
		super();
		this.endTime = endTime;
		this.parkingReferenceNumber = parkingReferenceNumber;
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
