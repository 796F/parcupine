package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterResponse {
	//the app can check for bad password, email, stuff like that.  
	//but the server must check credit card information.  
	ResponseCode resp;

	public RegisterResponse(ResponseCode resp) {
		super();
		this.resp = resp;
	}
	public String getResponseDescription(){
		return resp.getInfo();
	}
}
