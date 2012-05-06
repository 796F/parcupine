package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FindGridsByGPSCoordinateResponse {
	private long gridId;
	private double fillRate;
	private double longitude;
	private double latitude;

	/**
	 * @return the gridId
	 */
	public long getGridId() {
		return gridId;
	}

	/**
	 * @param gridId
	 *            the gridId to set
	 */
	public void setGridId(long gridId) {
		this.gridId = gridId;
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

}
