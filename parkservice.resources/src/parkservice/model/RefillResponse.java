package parkservice.model;

public class RefillResponse {
	ResponseCode resp;

	public RefillResponse(ResponseCode resp) {
		super();
		this.resp = resp;
	}
	public String getResponseDescription(){
		return resp.getInfo();
	}
	
}
