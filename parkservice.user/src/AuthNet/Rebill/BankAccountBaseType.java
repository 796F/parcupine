
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BankAccountBaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BankAccountBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountType" type="{https://api.authorize.net/soap/v1/}BankAccountTypeEnum" minOccurs="0"/>
 *         &lt;element name="nameOnAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="echeckType" type="{https://api.authorize.net/soap/v1/}EcheckTypeEnum" minOccurs="0"/>
 *         &lt;element name="bankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankAccountBaseType", propOrder = {
    "accountType",
    "nameOnAccount",
    "echeckType",
    "bankName"
})
@XmlSeeAlso({
    BankAccountMaskedType.class,
    BankAccountType.class
})
public class BankAccountBaseType {

    protected BankAccountTypeEnum accountType;
    protected String nameOnAccount;
    protected EcheckTypeEnum echeckType;
    protected String bankName;

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link BankAccountTypeEnum }
     *     
     */
    public BankAccountTypeEnum getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BankAccountTypeEnum }
     *     
     */
    public void setAccountType(BankAccountTypeEnum value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the nameOnAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOnAccount() {
        return nameOnAccount;
    }

    /**
     * Sets the value of the nameOnAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOnAccount(String value) {
        this.nameOnAccount = value;
    }

    /**
     * Gets the value of the echeckType property.
     * 
     * @return
     *     possible object is
     *     {@link EcheckTypeEnum }
     *     
     */
    public EcheckTypeEnum getEcheckType() {
        return echeckType;
    }

    /**
     * Sets the value of the echeckType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EcheckTypeEnum }
     *     
     */
    public void setEcheckType(EcheckTypeEnum value) {
        this.echeckType = value;
    }

    /**
     * Gets the value of the bankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the value of the bankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankName(String value) {
        this.bankName = value;
    }

}
