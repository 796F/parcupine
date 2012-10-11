package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	String resp;
	long endTime;
	String parkingReferenceNumber;
	int statusCode = -1000;
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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
