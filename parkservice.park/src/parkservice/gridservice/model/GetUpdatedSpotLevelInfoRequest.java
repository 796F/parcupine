package parkservice.gridservice.model;

public class GetUpdatedSpotLevelInfoRequest {
	private GpsCoordinate topLeftCorner;
	private GpsCoordinate bottomRightCorner;
	
	public GpsCoordinate getTopLeftCorner() {
		return topLeftCorner;
	}
	public void setTopLeftCorner(GpsCoordinate topLeftCorner) {
		this.topLeftCorner = topLeftCorner;
	}
	public GpsCoordinate getBottomRightCorner() {
		return bottomRightCorner;
	}
	public void setBottomRightCorner(GpsCoordinate bottomRightCorner) {
		this.bottomRightCorner = bottomRightCorner;
	}
}
