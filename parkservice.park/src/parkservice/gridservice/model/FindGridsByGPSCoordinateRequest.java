package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class FindGridsByGPSCoordinateRequest {
	private long lastUpdateTime;
	private GpsCoordinate topLeftCorner;
	private GpsCoordinate bottomRightCorner;

	/**
	 * @return the topLeftCorner
	 */
	public GpsCoordinate getTopLeftCorner() {
		return topLeftCorner;
	}

	/**
	 * @param topLeftCorner
	 *            the topLeftCorner to set
	 */
	public void setTopLeftCorner(GpsCoordinate topLeftCorner) {
		this.topLeftCorner = topLeftCorner;
	}

	/**
	 * @return the bottomRightCorner
	 */
	public GpsCoordinate getBottomRightCorner() {
		return bottomRightCorner;
	}

	/**
	 * @param bottomRightCorner
	 *            the bottomRightCorner to set
	 */
	public void setBottomRightCorner(GpsCoordinate bottomRightCorner) {
		this.bottomRightCorner = bottomRightCorner;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

}
