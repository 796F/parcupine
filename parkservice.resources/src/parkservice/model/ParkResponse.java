package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	long instanceId;
	String parkingReferenceNumber;
	String resp;
	
	public long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	public String getParkingReferenceNumber() {
		return parkingReferenceNumber;
	}

	public void setParkingReferenceNumber(String parkingReferenceNumber) {
		this.parkingReferenceNumber = parkingReferenceNumber;
	}

	

}
