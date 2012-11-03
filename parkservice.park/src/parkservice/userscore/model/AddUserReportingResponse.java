package parkservice.userscore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class AddUserReportingResponse {

	private boolean updateSuccessful;
	private String resp;
	private int statusCode;

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isUpdateSuccessful() {
		return updateSuccessful;
	}

	public void setUpdateSuccessful(boolean updateSuccessful) {
		this.updateSuccessful = updateSuccessful;
	}
}
