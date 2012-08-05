package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
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
