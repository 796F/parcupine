package parkservice.model;

public class RateObject {
	double lat;
	double lon;
	String location;
	long spotId;	
	String spotNumber;
	
	/*these are the defaults*/
	int minTime;			//must park 1 hour
	int maxTime;			//max park 3 hours
	int defaultRate; 		//rate is x/increment
	int minIncrement;		//min increase 30mins
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


	public long getSpotId() {
		return spotId;
	}
	public void setSpotId(long spotId) {
		this.spotId = spotId;
	}
	public int getMinTime() {
		return minTime;
	}
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public int getDefaultRate() {
		return defaultRate;
	}
	public void setDefaultRate(int defaultRate) {
		this.defaultRate = defaultRate;
	}
	public int getMinIncrement() {
		return minIncrement;
	}
	public void setMinIncrement(int minIncrement) {
		this.minIncrement = minIncrement;
	}
	public String getSpotNumber() {
		return spotNumber;
	}
	public void setSpotNumber(String spotNumber) {
		this.spotNumber = spotNumber;
	}
	
	
}
