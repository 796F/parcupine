package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UnparkResponse {
	String resp;
	int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

}
