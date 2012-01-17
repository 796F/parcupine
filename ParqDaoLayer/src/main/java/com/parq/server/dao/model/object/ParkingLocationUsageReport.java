package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingLocationUsageReport implements Serializable{

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 5326132839159790010L;
	private List<ParkingLocationUsageEntry> usageReportEntries;

	// default constructor
	public ParkingLocationUsageReport() {
		usageReportEntries = new ArrayList<ParkingLocationUsageEntry>();
	}
	
	/**
	 * @param usageReportEntry the usageReportEntry to set
	 */
	public void addUsageReportEntry(ParkingLocationUsageEntry usageReportEntry) {
		this.usageReportEntries.add(usageReportEntry);
	}

	/**
	 * @return the usageReportEntry
	 */
	public List<ParkingLocationUsageEntry> getUsageReportEntries() {
		return usageReportEntries;
	}


	public static class ParkingLocationUsageEntry implements Serializable{
	
		/**
		 * Generated serial ID
		 */
		private static final long serialVersionUID = -4824125713669245681L;
		private long ParkingInstId = -1;
		private long userId = -1;
		private long spaceId = -1;
		private long locationId = -1;
		
		private Date parkingBeganTime;
		private Date parkingEndTime;
		private boolean isPaidParking;
		private String parkingRefNumber;
		private String locationIdentifier;
		private String locationName;
		private String userEmail;
		private String spaceIdentifier;
		
		/**
		 * @return the parkingInstId
		 */
		public long getParkingInstId() {
			return ParkingInstId;
		}
		/**
		 * @param parkingInstId the parkingInstId to set
		 */
		public void setParkingInstId(long parkingInstId) {
			ParkingInstId = parkingInstId;
		}
		/**
		 * @return the userId
		 */
		public long getUserId() {
			return userId;
		}
		/**
		 * @param userId the userId to set
		 */
		public void setUserId(long userId) {
			this.userId = userId;
		}
		/**
		 * @return the spaceId
		 */
		public long getSpaceId() {
			return spaceId;
		}
		/**
		 * @param spaceId the spaceId to set
		 */
		public void setSpaceId(long spaceId) {
			this.spaceId = spaceId;
		}
		/**
		 * @return the locationId
		 */
		public long getLocationId() {
			return locationId;
		}
		/**
		 * @param locationId the locationId to set
		 */
		public void setLocationId(long locationId) {
			this.locationId = locationId;
		}
		/**
		 * @return the parkingBeganTime
		 */
		public Date getParkingBeganTime() {
			return parkingBeganTime;
		}
		/**
		 * @param parkingBeganTime the parkingBeganTime to set
		 */
		public void setParkingBeganTime(Date parkingBeganTime) {
			this.parkingBeganTime = parkingBeganTime;
		}
		/**
		 * @return the parkingEndTime
		 */
		public Date getParkingEndTime() {
			return parkingEndTime;
		}
		/**
		 * @param parkingEndTime the parkingEndTime to set
		 */
		public void setParkingEndTime(Date parkingEndTime) {
			this.parkingEndTime = parkingEndTime;
		}
		/**
		 * @return the isPaidParking
		 */
		public boolean isPaidParking() {
			return isPaidParking;
		}
		/**
		 * @param isPaidParking the isPaidParking to set
		 */
		public void setPaidParking(boolean isPaidParking) {
			this.isPaidParking = isPaidParking;
		}
		/**
		 * @return the parkingRefNumber
		 */
		public String getParkingRefNumber() {
			return parkingRefNumber;
		}
		/**
		 * @param parkingRefNumber the parkingRefNumber to set
		 */
		public void setParkingRefNumber(String parkingRefNumber) {
			this.parkingRefNumber = parkingRefNumber;
		}
		/**
		 * @return the locationIdentifier
		 */
		public String getLocationIdentifier() {
			return locationIdentifier;
		}
		/**
		 * @param locationIdentifier the locationIdentifier to set
		 */
		public void setLocationIdentifier(String locationIdentifier) {
			this.locationIdentifier = locationIdentifier;
		}
		/**
		 * @return the locationName
		 */
		public String getLocationName() {
			return locationName;
		}
		/**
		 * @param locationName the locationName to set
		 */
		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}
		/**
		 * @return the userEmail
		 */
		public String getUserEmail() {
			return userEmail;
		}
		/**
		 * @param userEmail the userEmail to set
		 */
		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}
		/**
		 * @return the spaceIdentifier
		 */
		public String getSpaceIdentifier() {
			return spaceIdentifier;
		}
		/**
		 * @param spaceIdentifier the spaceIdentifier to set
		 */
		public void setSpaceIdentifier(String spaceIdentifier) {
			this.spaceIdentifier = spaceIdentifier;
		}
	}
}
