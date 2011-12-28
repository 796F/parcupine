package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FindSpotRequest {
	int uid;
	double lat;
	double lon;
	AuthRequest userInfo;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public AuthRequest getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(AuthRequest userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
