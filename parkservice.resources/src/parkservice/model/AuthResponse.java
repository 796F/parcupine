package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthResponse {
	//every user field has [uid, fname, lname, email, password, phone number, parkstate, parkeloc]
	
	//information about user that will be displayed. 
	String fname;
	String lname;
	String phone;
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
