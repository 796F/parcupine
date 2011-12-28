package com.objects;

public class ParkInstanceObject {
	private String resp;
	private long endTime;
	private String parkingReferenceNumber;
	
	

	public ParkInstanceObject(String resp, long endTime,
			String parkingReferenceNumber) {
		super();
		this.resp = resp;
		this.endTime = endTime;
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
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
