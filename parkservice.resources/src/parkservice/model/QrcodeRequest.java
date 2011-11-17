package parkservice.model;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class QrcodeRequest {

	private int client;
	private int lot;
	private int spot;
	private AuthRequest userInfo;
	/**
	 * @return the client
	 */
	public int getClient() {
		return client;
	}
	/**
	 * @param client the client to set
	 */
	public void setClient(int client) {
		this.client = client;
	}
	/**
	 * @return the spot
	 */
	public int getSpot() {
		return spot;
	}
	/**
	 * @param spot the spot to set
	 */
	public void setSpot(int spot) {
		this.spot = spot;
	}
	/**
	 * @return the lot
	 */
	public int getLot() {
		return lot;
	}
	/**
	 * @param lot the lot to set
	 */
	public void setLot(int lot) {
		this.lot = lot;
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
