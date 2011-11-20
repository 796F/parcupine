package parkservice.model;

public class UnparkResponse {
	private int responseCode;
	public UnparkResponse(int code){
		super();
		this.responseCode = code;
	}
	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
