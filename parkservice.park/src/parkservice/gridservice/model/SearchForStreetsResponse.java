package parkservice.gridservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchForStreetsResponse {
	private long streetId;
	private double fillRate;
	private List<GpsCoorWithOrder> gpsCoor;

	public SearchForStreetsResponse() {
		gpsCoor = new ArrayList<GpsCoorWithOrder>();
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
	 * @return the gpsCoor
	 */
	public List<GpsCoorWithOrder> getGpsCoor() {
		return gpsCoor;
	}

}
