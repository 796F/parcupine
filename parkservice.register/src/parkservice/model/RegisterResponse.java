package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterResponse {
	//the app can check for bad password, email, stuff like that.  
	//but the server must check credit card information.  
	String responsecode;

	/**
	 * @return the responsecode
	 */
	public String getResponsecode() {
		return responsecode;
	}

	/**
	 * @param responsecode the responsecode to set
	 */
	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	/* 11: credit card valid
	 * 22: credit card NOT valid
	 * 33: could not connect to server*/
	
}
