package parkservice.model;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class AuthResponse {
	//future return CC info for app.
	
	//information about user that will be displayed. 
	long uid;
	int parkState; //0 not parked, 1 parked
	String creditCardStub;
	ParkSync sync;
	
	
	public String getCreditCardStub() {
		return creditCardStub;
	}
	public void setCreditCardStub(String creditCardStub) {
		this.creditCardStub = creditCardStub;
	}
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
	 * @return the sync
	 */
	public ParkSync getSync() {
		return sync;
	}
	/**
	 * @param sync the sync to set
	 */
	public void setSync(ParkSync sync) {
		this.sync = sync;
	}
	public int getParkState() {
		return parkState;
	}
	public void setParkState(int parkState) {
		this.parkState = parkState;
	}

	
	
}
