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
	long parkingInstanceId;
	
	AuthRequest userinfo;
	Date end;
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
	public long getParkingInstanceId() {
		return parkingInstanceId;
	}
	public void setParkingInstanceId(long parkingInstanceId) {
		this.parkingInstanceId = parkingInstanceId;
	}
	public AuthRequest getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(AuthRequest userinfo) {
		this.userinfo = userinfo;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
	
	
	

}
