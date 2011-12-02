package parkservice.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EditUserResponse {
	String resp;

	public EditUserResponse(String resp) {
		super();
		this.resp = resp;
	}
	
	
}
