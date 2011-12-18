package parkservice.model;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class QrcodeRequest {
	long uid;
	String lot;
	String spot;
	AuthRequest userInfo;
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
	 * @return the lot
	 */
	public String getLot() {
		return lot;
	}
	/**
	 * @param lot the lot to set
	 */
	public void setLot(String lot) {
		this.lot = lot;
	}
	/**
	 * @return the spot
	 */
	public String getSpot() {
		return spot;
	}
	/**
	 * @param spot the spot to set
	 */
	public void setSpot(String spot) {
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
