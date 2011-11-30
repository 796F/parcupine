package parkservice.model;

public class UnparkResponse {
	ResponseCode resp;

	public UnparkResponse(ResponseCode resp) {
		super();
		this.resp = resp;
	}
	public String getResponseDescription(){
		return resp.getInfo();
	}
	
}
