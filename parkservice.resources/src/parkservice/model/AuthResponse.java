package parkservice.model;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthResponse {
	//future return CC info for app.
	
	//information about user that will be displayed. 
	long uid;
	int parkstate; //0 not parked, 1 parked
	
	//THE FOLLOWING ARE FOR TIME DISPLAY
	Date endTime; //how much to set timer
	int minTime;			//must park 1 hour
	int maxTime;			//max park 3 hours
	int defaultRate; 		//rate is x/increment
	int minIncrement;		//min increase 30mins
	
	//THE FOLLOWING ARE FOR REFILLING/UNPARKING
	long instanceId; 
	long rateId;
	long spotId;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getParkstate() {
		return parkstate;
	}
	public void setParkstate(int parkstate) {
		this.parkstate = parkstate;
	}
	
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getMinTime() {
		return minTime;
	}
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public int getDefaultRate() {
		return defaultRate;
	}
	public void setDefaultRate(int defaultRate) {
		this.defaultRate = defaultRate;
	}
	public int getMinIncrement() {
		return minIncrement;
	}
	public void setMinIncrement(int minIncrement) {
		this.minIncrement = minIncrement;
	}
	public long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}
	public long getRateId() {
		return rateId;
	}
	public void setRateId(long rateId) {
		this.rateId = rateId;
	}
	public long getSpotId() {
		return spotId;
	}
	public void setSpotId(long spotId) {
		this.spotId = spotId;
	}
	
	
	
	
	
}
