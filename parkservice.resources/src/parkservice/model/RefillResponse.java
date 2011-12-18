package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RefillResponse {
	String resp;
	long instanceId;
	
	public long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	
	
}
