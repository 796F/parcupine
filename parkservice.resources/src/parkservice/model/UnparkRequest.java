package parkservice.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UnparkRequest {
	private AuthRequest userinfo;
	private Date end;
	
	public UnparkRequest(AuthRequest userinfo){
		super();
		this.userinfo = userinfo;
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
	

}
