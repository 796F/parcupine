package com.objects;



public class UserObject {
	/* store user info on get auth to display*/
	private long uid;
	private boolean parkState;
	private ParkSync sync;
	
	
	public UserObject(long uid, boolean parkState, ParkSync sync) {
		super();
		this.uid = uid;
		this.parkState = parkState;
		this.sync = sync;
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
