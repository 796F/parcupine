package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	long parkingInstanceId;
	String resp;
	
	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	public long getParkingInstanceId() {
		return parkingInstanceId;
	}

	public void setParkingInstanceId(long parkingInstanceId) {
		this.parkingInstanceId = parkingInstanceId;
	}

	

}
