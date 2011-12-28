package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SingleSpot {
	double lat;
	double lon;
	String spot;
	
	public SingleSpot() {
		super();
	}


	public SingleSpot(double lat, double lon, String spot) {
		super();
		this.lat = lat;
		this.spot = spot;
		this.lon = lon;
	}
	
	
	public String getSpot() {
		return spot;
	}


	public void setSpot(String spot) {
		this.spot = spot;
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
	
	
}
