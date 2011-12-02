package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	String resp;

	public ParkResponse(String resp) {
		super();
		this.resp = resp;
	}

	
}
