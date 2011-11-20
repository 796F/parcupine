package parkservice.model;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EditUserResponse {
	private HashMap<String, String> editedVals;

	/**
	 * @return the editedVals
	 */
	public HashMap<String, String> getEditedVals() {
		return editedVals;
	}

	/**
	 * @param editedVals the editedVals to set
	 */
	public void setEditedVals(HashMap<String, String> editedVals) {
		this.editedVals = editedVals;
	}
	
}
