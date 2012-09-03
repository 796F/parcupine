package parkservice.userscore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class AddUserReportingResponse {

	private boolean updateSuccessful;

	public boolean isUpdateSuccessful() {
		return updateSuccessful;
	}

	public void setUpdateSuccessful(boolean updateSuccessful) {
		this.updateSuccessful = updateSuccessful;
	}
}