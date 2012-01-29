package parkservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ParkResponse {
	String resp;
	long endTime;
	String parkingReferenceNumber;
	ParkSync sync;
	
	
	public ParkSync getSync() {
		return sync;
	}

	public void setSync(ParkSync sync) {
		this.sync = sync;
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
