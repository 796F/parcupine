package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetUpdatedStreetInfoResponse {

	private long streetId;
	private double fillRate;

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

}
