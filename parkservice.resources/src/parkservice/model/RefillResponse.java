package parkservice.model;

public class RefillResponse {
	String resp;
	long parkingInstanceId;
	
	

	public long getParkingInstanceId() {
		return parkingInstanceId;
	}

	public void setParkingInstanceId(long parkingInstanceId) {
		this.parkingInstanceId = parkingInstanceId;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	
	
}
