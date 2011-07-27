package com.objects;

public class ParkObject {
	private float lat;
	private float lon;
	private String location;
	private int spotNum;
	public ParkObject(){
		this.location="NONE";
	}
	
	public ParkObject(String loc, float lat, float lon, int spot){
		this.lat=lat;
		this.lon=lon;
		this.location=loc;
		this.spotNum=spot;
	}
	@Override
	public String toString(){
		return location + lat + lon + spotNum;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getSpotNum() {
		return spotNum;
	}
	public void setSpotNum(int spotNum) {
		this.spotNum = spotNum;
	}
}
