package parkservice.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class FindSpotResponse {
	ArrayList<SingleSpot> locationList = new ArrayList<SingleSpot>();

	
	public void add(double lat, double lon, String spot){
		this.locationList.add(new SingleSpot(lat, lon, spot));
	}
	public ArrayList<SingleSpot> getLocationList() {
		return locationList;
	}

	public void setLocationList(ArrayList<SingleSpot> locationList) {
		this.locationList = locationList;
	}
	
}
