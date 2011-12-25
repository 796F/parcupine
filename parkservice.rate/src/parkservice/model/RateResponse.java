package parkservice.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RateResponse {
	String resp;
	RateObject rate;
	
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public RateObject getRate() {
		return rate;
	}
	public void setRate(RateObject rate) {
		this.rate = rate;
	}
	
	
}
