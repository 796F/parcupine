package com.objects;



public class UserObject {
	/* store user info on get auth to display*/
	private long uid;
	private boolean parkState;
	private ParkSync sync;
	private String creditCardStub;
	
	

	public UserObject(long uid, boolean parkState, ParkSync sync,
			String creditCardStub) {
		super();
		this.uid = uid;
		this.parkState = parkState;
		this.sync = sync;
		this.creditCardStub = creditCardStub;
	}
	public String getCreditCardStub() {
		return creditCardStub;
	}
	public long getUid() {
		return uid;
	}
	public boolean getParkState() {
		return parkState;
	}
	public ParkSync getSync() {
		return sync;
	}
	
	
}
