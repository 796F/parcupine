package parkservice.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RefillRequest {
	String parkingReferenceNumber;
	int iterations;
	int paymentType;
	long spotid;
	long uid;
	AuthRequest userinfo;
	
	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}
	public void setParkingReferenceNumber(String parkingReferenceNumber) {
		this.parkingReferenceNumber = parkingReferenceNumber;
	}
	public int getIterations() {
		return iterations;
	}
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	public int getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	public AuthRequest getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(AuthRequest userinfo) {
		this.userinfo = userinfo;
	}
	public long getSpotid() {
		return spotid;
	}
	public void setSpotid(long spotid) {
		this.spotid = spotid;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	
	
	
}
