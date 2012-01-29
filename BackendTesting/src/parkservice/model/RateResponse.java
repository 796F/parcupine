package parkservice.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RateResponse {
	String resp;
	RateObject rateObject;
	ParkSync sync;
	
	public ParkSync getSync() {
		return sync;
	}
	public void setSync(ParkSync sync) {
		this.sync = sync;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public RateObject getRateObject() {
		return rateObject;
	}
	public void setRateObject(RateObject rateObject) {
		this.rateObject = rateObject;
	}
	
	
	
}
