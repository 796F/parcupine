package parkservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ParkRequest {
	long uid;
	long spotid;
	int iterations;
	int paymentType;
	AuthRequest userinfo;
	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}
	/**
	 * @return the spotid
	 */
	public long getSpotid() {
		return spotid;
	}
	/**
	 * @param spotid the spotid to set
	 */
	public void setSpotid(long spotid) {
		this.spotid = spotid;
	}
	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}
	/**
	 * @param iterations the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	/**
	 * @return the paymentType
	 */
	public int getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	
	/**
	 * @return the userinfo
	 */
	public AuthRequest getUserinfo() {
		return userinfo;
	}
	/**
	 * @param userinfo the userinfo to set
	 */
	public void setUserinfo(AuthRequest userinfo) {
		this.userinfo = userinfo;
	}
	
	
}
