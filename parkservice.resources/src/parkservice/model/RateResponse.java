package parkservice.model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RateResponse {
	
	double lat;
	double lon;
	String location;
	long spot;
	
	/*these are the defaults*/
	int minTime;			//must park 1 hour
	int maxTime;			//max park 3 hours
	int defaultRate; 		//rate is x/increment
	int minIncrement;		//min increase 30mins
	
	/*specials, such as whole day parking, holiday/game parking, etc*/
	ArrayList<SpecialRate> specials;

	
	public RateResponse(){
		
	}
	public RateResponse(double lat, double lon, String location, long spot,
			int minTime, int maxTime, int defaultRate, int minIncrement,
			ArrayList<SpecialRate> specials) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.location = location;
		this.spot = spot;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.defaultRate = defaultRate;
		this.minIncrement = minIncrement;
		this.specials = specials;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}



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
	public long getSpot() {
		return spot;
	}
	public void setSpot(long spot) {
		this.spot = spot;
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
	 * @return the specials
	 */
	public ArrayList<SpecialRate> getSpecials() {
		return specials;
	}

	/**
	 * @param specials the specials to set
	 */
	public void setSpecials(ArrayList<SpecialRate> specials) {
		this.specials = specials;
	}
	
	
}
