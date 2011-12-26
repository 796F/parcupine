package com.objects;


public class RateObject {
	private double lat;
	private double lon;
	private long spot;
	private int minTime;
	private int maxTime;
	private int defaultRate;
	private int minIncrement;
	private String description;
	
	public RateObject(){
		
	}
	
	public RateObject(double lat, double lon, long spot, int minTime, int maxTime, int defaultRate, int minIncrement, String description) {
		this.lat=lat;
		this.lon=lon;
		this.spot=spot;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.defaultRate = defaultRate;
		this.minIncrement = minIncrement;
		this.description = description;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	

	/**
	 * @return the spot
	 */
	public long getSpot() {
		return spot;
	}

	/**
	 * @return the minTime
	 */
	public int getMinTime() {
		return minTime;
	}

	/**
	 * @return the maxTime
	 */
	public int getMaxTime() {
		return maxTime;
	}

	/**
	 * @return the defaultRate
	 */
	public int getDefaultRate() {
		return defaultRate;
	}

	/**
	 * @return the minIncrement
	 */
	public int getMinIncrement() {
		return minIncrement;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
