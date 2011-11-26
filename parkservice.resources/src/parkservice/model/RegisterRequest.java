package parkservice.model;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterRequest {
	String CreditCard;
	int cccNumber;
	String holderName;
	String expDate;
	String billingAddress;
	
	String email;
	String password;
	/**
	 * @return the creditCard
	 */
	public String getCreditCard() {
		return CreditCard;
	}
	/**
	 * @param creditCard the creditCard to set
	 */
	public void setCreditCard(String creditCard) {
		CreditCard = creditCard;
	}
	/**
	 * @return the cccNumber
	 */
	public int getCccNumber() {
		return cccNumber;
	}
	/**
	 * @param cccNumber the cccNumber to set
	 */
	public void setCccNumber(int cccNumber) {
		this.cccNumber = cccNumber;
	}
	/**
	 * @return the holderName
	 */
	public String getHolderName() {
		return holderName;
	}
	/**
	 * @param holderName the holderName to set
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	/**
	 * @return the expDate
	 */
	public String getExpDate() {
		return expDate;
	}
	/**
	 * @param expDate the expDate to set
	 */
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	/**
	 * @return the billingAddress
	 */
	public String getBillingAddress() {
		return billingAddress;
	}
	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
