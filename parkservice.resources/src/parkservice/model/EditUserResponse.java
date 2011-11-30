package parkservice.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EditUserResponse {
	ResponseCode resp;
	
	public EditUserResponse(ResponseCode responseCode){
		super();
		this.resp = responseCode;
	}

	public String getResponseDescription(){
		return resp.getInfo();
	}
	
}
