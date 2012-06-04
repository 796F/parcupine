package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkingSpaceWithStatus {

	private long spaceId;
	private String spaceName;
	private int segment = -1;
	private String status;
	private double longitude;
	private double latitude;
	private long streetId;

	/**
	 * @return the spaceId
	 */
	public long getSpaceId() {
		return spaceId;
	}

	/**
	 * @param spaceId
	 *            the spaceId to set
	 */
	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
	 * @return
	 */
	public int getSegment() {
		return segment;
	}

	/**
	 * @param spaceOrder
	 */
	public void setSegment(int segment) {
		this.segment = segment;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

}
