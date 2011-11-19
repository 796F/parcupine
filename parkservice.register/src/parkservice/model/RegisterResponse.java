package parkservice.model;

public class RegisterResponse {
	//the app can check for bad password, email, stuff like that.  
	//but the server must check credit card information.  
	private int responsecode;

	/* 11: credit card valid
	 * 22: credit card NOT valid
	 * 33: could not connect to server*/
	public RegisterResponse(int responsecode) {
		super();
		this.responsecode = responsecode;
	}
	/**
	 * @return the responsecode
	 */
	public int getResponsecode() {
		return responsecode;
	}

	/**
	 * @param responsecode the responsecode to set
	 */
	public void setResponsecode(int responsecode) {
		this.responsecode = responsecode;
	}
	
}
