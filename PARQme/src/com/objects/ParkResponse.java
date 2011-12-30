package com.objects;

public class ParkResponse {
	private String resp;
	private long endTime;
	private String parkingReferenceNumber;
	private ParkSync sync;


	public ParkResponse(String resp, long endTime,
			String parkingReferenceNumber, ParkSync sync) {
		super();
		this.resp = resp;
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
	public void setResp(String resp) {
		this.resp = resp;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public void setParkingReferenceNumber(String parkingReferenceNumber) {
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
