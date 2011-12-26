package com.objects;

public class ParkResponse {
	private String resp;
	private long endTime;
	private String parkingReferenceNumber;
	
	
	public ParkResponse(String resp, long endTime, String parkingReferenceNumber) {
		super();
		this.resp = resp;
		this.endTime = endTime;
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
	public String getResp() {
		return resp;
	}
	public long getEndTime() {
		return endTime;
	}
	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}
	
	
}
