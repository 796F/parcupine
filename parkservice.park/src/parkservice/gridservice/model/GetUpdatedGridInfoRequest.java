package parkservice.gridservice.model;

public class GetUpdatedGridInfoRequest {
	private long lastUpdateTime;
	private long[] gridIds;

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public long[] getGridIds() {
		return gridIds;
	}

	public void setGridIds(long[] gridIds) {
		this.gridIds = gridIds;
	}
}
