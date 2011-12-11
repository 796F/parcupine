package parkservice.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RefillRequest {
	Date oldEndTime;
	Date newEndTime;
	int amount;
	int paymentType;
	int spotid;
	AuthRequest userinfo;
	
	public Date getOldEndTime() {
		return oldEndTime;
	}
	public void setOldEndTime(Date oldEndTime) {
		this.oldEndTime = oldEndTime;
	}
	public Date getNewEndTime() {
		return newEndTime;
	}
	public void setNewEndTime(Date newEndTime) {
		this.newEndTime = newEndTime;
	}
	public int getSpotid() {
		return spotid;
	}
	public void setSpotid(int spotid) {
		this.spotid = spotid;
	}
	public RefillRequest(AuthRequest userinfo){
		super();
		this.userinfo = userinfo;
	}
	

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
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
