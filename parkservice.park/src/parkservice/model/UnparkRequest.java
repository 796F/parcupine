package parkservice.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UnparkRequest {
	long uid;
	long spotid;
	String parkingReferenceNumber;
	AuthRequest userInfo;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getSpotid() {
		return spotid;
	}
	public void setSpotid(long spotid) {
		this.spotid = spotid;
	}
	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}
	public void setParkingReferenceNumber(String parkingReferenceNumber) {
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
	public AuthRequest getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(AuthRequest userInfo) {
		this.userInfo = userInfo;
	}


}
