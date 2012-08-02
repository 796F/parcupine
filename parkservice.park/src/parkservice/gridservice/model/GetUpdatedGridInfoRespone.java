package parkservice.gridservice.model;

public class GetUpdatedGridInfoRespone {
	private long gridId;
	private double fillRate;

	public long getGridId() {
		return gridId;
	}

	public void setGridId(long gridId) {
		this.gridId = gridId;
	}

	public double getFillRate() {
		return fillRate;
	}

	public void setFillRate(double fillRate) {
		this.fillRate = fillRate;
	}
}
