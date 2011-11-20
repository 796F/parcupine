package parkservice.model;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class EditUserRequest {
	
	private HashMap<String, String> newVals;
	private AuthRequest userinfo;
	
	public EditUserRequest(AuthRequest userinfo) {
		super();
		this.newVals = new HashMap<String, String>();
		this.userinfo = userinfo;
	}
	public void addEdit(String field, String val){
		this.newVals.put(field, val);
	}
	
	/**
	 * @return the newVals
	 */
	public HashMap<String, String> getNewVals() {
		return newVals;
	}

	/**
	 * @param newVals the newVals to set
	 */
	public void setNewVals(HashMap<String, String> newVals) {
		this.newVals = newVals;
	}

	/**
	 * @return the userinfo
	 */
	public AuthRequest getUserinfo() {
		return userinfo;
	}
	/**
	 * @param userinfo the userinfo to set
	 */
	public void setUserinfo(AuthRequest userinfo) {
		this.userinfo = userinfo;
	}
	
}
