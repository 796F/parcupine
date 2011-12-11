package parkservice.model;

public class RefillResponse {
	String resp;
	int parkingInstanceId;
	
	public int getParkingInstanceId() {
		return parkingInstanceId;
	}

	public void setParkingInstanceId(int parkingInstanceId) {
		this.parkingInstanceId = parkingInstanceId;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	
	
}
