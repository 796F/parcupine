package com.objects;


public class SpotObject {
	private double lat;
	private double lon;
	private String location;
	private int spot;
	private int minTime;
	private int maxTime;
	private int defaultRate;
	private int minIncrement;
	
	public SpotObject(){
		
	}
	
	public SpotObject(double lat, double lon, String location, int spot, int minTime, int maxTime, int defaultRate, int minIncrement) {
		this.lat=lat;
		this.lon=lon;
		this.location=location;
		this.spot=spot;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.defaultRate = defaultRate;
		this.minIncrement = minIncrement;
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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the spot
	 */
	public int getSpot() {
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
}
