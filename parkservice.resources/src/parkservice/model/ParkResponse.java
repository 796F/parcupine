package parkservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkResponse {
	private int responsecode;

	public ParkResponse(int code){
		super();
		this.responsecode = code;
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
