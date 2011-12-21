package parkservice.model;

public class parkSync {

	//THE FOLLOWING ARE FOR TIME DISPLAY
	long endTime; //how much to set timer
	int minTime;			//must park 1 hour
	int maxTime;			//max park 3 hours
	int defaultRate; 		//rate is x/increment
	int minIncrement;		//min increase 30mins
	
	//THE FOLLOWING ARE FOR REFILLING/UNPARKING
	String parkingReferenceNumber; 
	long rateId;
	long spotId;
	
	/**
	 * @return the parkingReferenceNumber
	 */
	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}
	/**
	 * @param parkingReferenceNumber the parkingReferenceNumber to set
	 */
	public void setParkingReferenceNumber(String parkingReferenceNumber) {
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the minTime
	 */
	public int getMinTime() {
		return minTime;
	}
	/**
	 * @param minTime the minTime to set
	 */
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
	/**
	 * @return the maxTime
	 */
	public int getMaxTime() {
		return maxTime;
	}
	/**
	 * @param maxTime the maxTime to set
	 */
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	/**
	 * @return the defaultRate
	 */
	public int getDefaultRate() {
		return defaultRate;
	}
	/**
	 * @param defaultRate the defaultRate to set
	 */
	public void setDefaultRate(int defaultRate) {
		this.defaultRate = defaultRate;
	}
	/**
	 * @return the minIncrement
	 */
	public int getMinIncrement() {
		return minIncrement;
	}
	/**
	 * @param minIncrement the minIncrement to set
	 */
	public void setMinIncrement(int minIncrement) {
		this.minIncrement = minIncrement;
	}
	
	/**
	 * @return the rateId
	 */
	public long getRateId() {
		return rateId;
	}
	/**
	 * @param rateId the rateId to set
	 */
	public void setRateId(long rateId) {
		this.rateId = rateId;
	}
	/**
	 * @return the spotId
	 */
	public long getSpotId() {
		return spotId;
	}
	/**
	 * @param spotId the spotId to set
	 */
	public void setSpotId(long spotId) {
		this.spotId = spotId;
	}
	
	
	
}
