package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthResponse {
	//future return CC info for app.
	
	//information about user that will be displayed. 
	long uid;
	int parkstate; //0 not parked, 1 parked
	
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getParkstate() {
		return parkstate;
	}
	public void setParkstate(int parkstate) {
		this.parkstate = parkstate;
	}
	
}
