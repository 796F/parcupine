package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	int parkingInstanceId;
	String resp;
	
	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	public int getParkingInstanceId() {
		return parkingInstanceId;
	}

	public void setParkingInstanceId(int parkingInstanceId) {
		this.parkingInstanceId = parkingInstanceId;
	}

}
