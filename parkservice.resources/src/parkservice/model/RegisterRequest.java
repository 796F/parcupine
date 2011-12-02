package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterRequest {
	String CreditCard;
	String cscNumber;
	String holderName;
	int expMonth;
	int expYear;
	String zipcode;
	
	String email;
	String password;
	
	public String getCreditCard() {
		return CreditCard;
	}
	public void setCreditCard(String creditCard) {
		CreditCard = creditCard;
	}
	public String getCscNumber() {
		return cscNumber;
	}
	public void setCscNumber(String cscNumber) {
		this.cscNumber = cscNumber;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	public int getExpMonth() {
		return expMonth;
	}
	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}
	public int getExpYear() {
		return expYear;
	}
	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
