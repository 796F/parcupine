package parkservice.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GpsRequest {

	private float lat;
	private float lon;
	private int spot;
	private AuthRequest userInfo;
	/**
	 * @return the lat
	 */
	public float getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public float getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(float lon) {
		this.lon = lon;
	}
	/**
	 * @return the spot
	 */
	public int getSpot() {
		return spot;
	}
	/**
	 * @param spot the spot to set
	 */
	public void setSpot(int spot) {
		this.spot = spot;
	}
	/**
	 * @return the userInfo
	 */
	public AuthRequest getUserInfo() {
		return userInfo;
	}
	/**
	 * @param userInfo the userInfo to set
	 */
	public void setUserInfo(AuthRequest userInfo) {
		this.userInfo = userInfo;
	}
	
}
