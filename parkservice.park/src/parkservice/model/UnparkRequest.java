package parkservice.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UnparkRequest {
	long uid;
	long spotId;
	String parkingReferenceNumber;
	AuthRequest userInfo;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}


	public long getSpotId() {
		return spotId;
	}
	public void setSpotId(long spotId) {
		this.spotId = spotId;
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
