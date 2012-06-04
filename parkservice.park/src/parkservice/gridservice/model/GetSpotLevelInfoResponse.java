package parkservice.gridservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetSpotLevelInfoResponse {
	private long streetId;
	private double fillRate;
	private List<ParkingSpaceWithStatus> parkingSpace;

	public GetSpotLevelInfoResponse() {
		parkingSpace = new ArrayList<ParkingSpaceWithStatus>();
	}
	
	/**
	 * @return the streetId
	 */
	public long getStreetId() {
		return streetId;
	}

	/**
	 * @param streetId
	 *            the streetId to set
	 */
	public void setStreetId(long streetId) {
		this.streetId = streetId;
	}

	/**
	 * @return the fillRate
	 */
	public double getFillRate() {
		return fillRate;
	}

	/**
	 * @param fillRate
	 *            the fillRate to set
	 */
	public void setFillRate(double fillRate) {
		this.fillRate = fillRate;
	}

	/**
	 * @return the parkingSpace
	 */
	public List<ParkingSpaceWithStatus> getParkingSpace() {
		return parkingSpace;
	}

	/**
	 * @param parkingSpace
	 *            the parkingSpace to set
	 */
	public void setParkingSpace(List<ParkingSpaceWithStatus> parkingSpace) {
		this.parkingSpace = parkingSpace;
	}

}
