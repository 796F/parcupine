package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterResponse {
	String resp;

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}
	
}
