package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthResponse {
	//future return CC info for app.
	
	//information about user that will be displayed. 
	int uid;
	int parkstate; //0 not parked, 1 parked
	
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getParkstate() {
		return parkstate;
	}
	public void setParkstate(int parkstate) {
		this.parkstate = parkstate;
	}
	
}
