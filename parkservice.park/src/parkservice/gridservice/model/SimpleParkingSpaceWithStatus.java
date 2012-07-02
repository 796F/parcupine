package parkservice.gridservice.model;

public class SimpleParkingSpaceWithStatus {

	private long spaceId;
	private String status;
	
	public long getSpaceId() {
		return spaceId;
	}
	
	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
