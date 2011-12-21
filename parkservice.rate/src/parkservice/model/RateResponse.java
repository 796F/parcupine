package parkservice.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RateResponse {
	
	double lat;
	double lon;
	String location;
	long spotid;
	
	/*these are the defaults*/
	int minTime;			//must park 1 hour
	int maxTime;			//max park 3 hours
	int defaultRate; 		//rate is x/increment
	int minIncrement;		//min increase 30mins
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
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
	/**
	 * @return the spotid
	 */
	public long getSpotid() {
		return spotid;
	}
	/**
	 * @param spotid the spotid to set
	 */
	public void setSpotid(long spotid) {
		this.spotid = spotid;
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
	
	/*specials, such as whole day parking, holiday/game parking, etc*/

	
	
}
