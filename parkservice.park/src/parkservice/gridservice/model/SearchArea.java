package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchArea {
	
	private GpsCoordinate southWestCorner;
	private GpsCoordinate northEastCorner;
	
	/**
	 * @return
	 */
	public GpsCoordinate getSouthWestCorner() {
		return southWestCorner;
	}
	
	/**
	 * @param southWestCorner
	 */
	public void setSouthWestCorner(GpsCoordinate southWestCorner) {
		this.southWestCorner = southWestCorner;
	}
	
	/**
	 * @return
	 */
	public GpsCoordinate getNorthEastCorner() {
		return northEastCorner;
	}
	
	/**
	 * @param northEastCorner
	 */
	public void setNorthEastCorner(GpsCoordinate northEastCorner) {
		this.northEastCorner = northEastCorner;
	}
	
	
}
