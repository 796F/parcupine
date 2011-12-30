package com.objects;

public class RateResponse {
	private String resp;
	private RateObject rateObject;
	private ParkSync sync;
	
	
	public RateResponse(String resp, RateObject rateObject, ParkSync sync) {
		super();
		this.resp = resp;
		this.rateObject = rateObject;
		this.sync = sync;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public RateObject getRateObject() {
		return rateObject;
	}
	public void setRateObject(RateObject rateObject) {
		this.rateObject = rateObject;
	}

	
	
	
	
}
