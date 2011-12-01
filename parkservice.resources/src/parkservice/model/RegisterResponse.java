package parkservice.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RegisterResponse {
	//the app can check for bad password, email, stuff like that.  
	//but the server must check credit card information.  
	TestResponseCode resp;

	public RegisterResponse(TestResponseCode resp) {
		super();
		this.resp = resp;
	}
	
}
