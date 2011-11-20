package parkservice.model;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ParkRequest {
	
	private int spotid;
	private Date start;
	private Date end;
	private int paymentType;
	private int amount;
	private AuthRequest userinfo;
	/**
	 * @return the spotid
	 */
	public int getSpotid() {
		return spotid;
	}
	/**
	 * @param spotid the spotid to set
	 */
	public void setSpotid(int spotid) {
		this.spotid = spotid;
	}
	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(Date end) {
		this.end = end;
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
