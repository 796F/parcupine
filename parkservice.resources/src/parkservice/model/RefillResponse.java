package parkservice.model;

public class RefillResponse {
	private int responseCode;
	
	public RefillResponse(int code){
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
