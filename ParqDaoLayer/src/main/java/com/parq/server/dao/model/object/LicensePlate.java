package com.parq.server.dao.model.object;

import java.io.Serializable;

public class LicensePlate  implements Serializable {
	
	/**
	 * auto generated serialization id
	 */
	private static final long serialVersionUID = 5406525312500536198L;
	
	private long plateID;
	private long userID;
	private String plateNum;
	private boolean isDefault;
	
	/**
	 * @return the plateID
	 */
	public long getPlateID() {
		return plateID;
	}
	/**
	 * @param plateID the plateID to set
	 */
	public void setPlateID(long plateID) {
		this.plateID = plateID;
	}
	/**
	 * @return the userID
	 */
	public long getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(long userID) {
		this.userID = userID;
	}
	/**
	 * @return the plateNum
	 */
	public String getPlateNum() {
		return plateNum;
	}
	/**
	 * @param plateNum the plateNum to set
	 */
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	
	
}
