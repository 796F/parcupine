package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EditCCResponse {
	String resp;

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}
	
	
}
