package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	ResponseCode resp;

	public ParkResponse(ResponseCode resp) {
		super();
		this.resp = resp;
	}
	
	public String getResponseDescription(){
		return resp.getInfo();
	}
}
