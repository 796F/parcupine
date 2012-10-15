package parkservice.gridservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class GetUpdatedSpotLevelInfoResponse {
	private long streetId;
	private List<SimpleParkingSpaceWithStatus> parkingSpace;
	
	public GetUpdatedSpotLevelInfoResponse() {
		parkingSpace = new ArrayList<SimpleParkingSpaceWithStatus>();
	}
	
	public long getStreetId() {
		return streetId;
	}
	public void setStreetId(long streetId) {
		this.streetId = streetId;
	}
	public List<SimpleParkingSpaceWithStatus> getParkingSpace() {
		return parkingSpace;
	}
	public void setParkingSpace(List<SimpleParkingSpaceWithStatus> parkingSpace) {
		this.parkingSpace = parkingSpace;
	}
	
	
}
