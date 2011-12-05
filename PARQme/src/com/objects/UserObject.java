package com.objects;


public class UserObject {
	/* store user info on get auth to display*/
	private long uid;
	private boolean parkState;

	public UserObject(long uid, boolean parkState) {
		this.uid = uid;
		this.parkState = parkState;
	}

	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}

	/**
	 * @return the parkState
	 */
	public boolean getParkState() {
		return parkState;
	}	
}
