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
	int parkstate; //0 not parked, 1 parked
	ParkSync sync;
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
	 * @return the parkstate
	 */
	public int getParkstate() {
		return parkstate;
	}
	/**
	 * @param parkstate the parkstate to set
	 */
	public void setParkstate(int parkstate) {
		this.parkstate = parkstate;
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

	
	
}
