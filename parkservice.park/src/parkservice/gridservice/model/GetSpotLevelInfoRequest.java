package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class GetSpotLevelInfoRequest {
	
	private int numberOfSearchBox = 1;
	private GpsCoordinate topLeftCorner1;
	private GpsCoordinate bottomRightCorner1;
	private GpsCoordinate topLeftCorner2;
	private GpsCoordinate bottomRightCorner2;
	/**
	 * @return the topLeftCorner
	 */
	public GpsCoordinate getTopLeftCorner1() {
		return topLeftCorner1;
	}

	/**
	 * @param topLeftCorner the topLeftCorner to set
	 */
	public void setTopLeftCorner1(GpsCoordinate topLeftCorner) {
		this.topLeftCorner1 = topLeftCorner;
	}

	/**
	 * @return the bottomRightCorner
	 */
	public GpsCoordinate getBottomRightCorner1() {
		return bottomRightCorner1;
	}

	/**
	 * @param bottomRightCorner the bottomRightCorner to set
	 */
	public void setBottomRightCorner1(GpsCoordinate bottomRightCorner) {
		this.bottomRightCorner1 = bottomRightCorner;
	}

	public int getNumberOfSearchBox() {
		return numberOfSearchBox;
	}

	public void setNumberOfSearchBox(int numberOfBox) {
		this.numberOfSearchBox = numberOfBox;
	}

	public GpsCoordinate getTopLeftCorner2() {
		return topLeftCorner2;
	}

	public void setTopLeftCorner2(GpsCoordinate topLeftCorner2) {
		this.topLeftCorner2 = topLeftCorner2;
	}

	public GpsCoordinate getBottomRightCorner2() {
		return bottomRightCorner2;
	}

	public void setBottomRightCorner2(GpsCoordinate bottomRightCorner2) {
		this.bottomRightCorner2 = bottomRightCorner2;
	}

}
