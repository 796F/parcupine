package com.objects;


public class ParkSync {

	//THE FOLLOWING ARE FOR TIME DISPLAY
	long endTime; //how much to set timer
	int minTime;			//must park 1 hour
	int maxTime;			//max park 3 hours
	int defaultRate; 		//rate is x/increment
	int minIncrement;		//min increase 30mins
	
	//THE FOLLOWING ARE FOR REFILLING/UNPARKING
	String parkingReferenceNumber; 
	double lat;
	double lon;
	long spotId;
	String description;
	String spotNumber;
	
	
	
	public ParkSync(long endTime, int minTime, int maxTime, int defaultRate,
			int minIncrement, String parkingReferenceNumber, double lat,
			double lon, long spotId, String description, String spotNumber) {
		super();
		this.endTime = endTime;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.defaultRate = defaultRate;
		this.minIncrement = minIncrement;
		this.parkingReferenceNumber = parkingReferenceNumber;
		this.lat = lat;
		this.lon = lon;
		this.spotId = spotId;
		this.description = description;
		this.spotNumber = spotNumber;
	}
	public String getSpotNumber() {
		return spotNumber;
	}
	public String getDescription() {
		return description;
	}
	public long getEndTime() {
		return endTime;
	}
	public int getMinTime() {
		return minTime;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public int getDefaultRate() {
		return defaultRate;
	}
	public int getMinIncrement() {
		return minIncrement;
	}
	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public long getSpotId() {
		return spotId;
	}
	
	
	
	
}
