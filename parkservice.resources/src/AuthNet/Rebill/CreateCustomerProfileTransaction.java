
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="merchantAuthentication" type="{https://api.authorize.net/soap/v1/}MerchantAuthenticationType" minOccurs="0"/>
 *         &lt;element name="transaction" type="{https://api.authorize.net/soap/v1/}ProfileTransactionType" minOccurs="0"/>
 *         &lt;element name="extraOptions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "merchantAuthentication",
    "transaction",
    "extraOptions"
})
@XmlRootElement(name = "CreateCustomerProfileTransaction")
public class CreateCustomerProfileTransaction {

    protected MerchantAuthenticationType merchantAuthentication;
    protected ProfileTransactionType transaction;
    protected String extraOptions;

    /**
     * Gets the value of the merchantAuthentication property.
     * 
     * @return
     *     possible object is
     *     {@link MerchantAuthenticationType }
     *     
     */
    public MerchantAuthenticationType getMerchantAuthentication() {
        return merchantAuthentication;
    }

    /**
     * Sets the value of the merchantAuthentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link MerchantAuthenticationType }
     *     
     */
    public void setMerchantAuthentication(MerchantAuthenticationType value) {
        this.merchantAuthentication = value;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransactionType }
     *     
     */
    public ProfileTransactionType getTransaction() {
        return transaction;
    }

    /**
     * Sets the value of the transaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransactionType }
     *     
     */
    public void setTransaction(ProfileTransactionType value) {
        this.transaction = value;
    }

    /**
     * Gets the value of the extraOptions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraOptions() {
        return extraOptions;
    }

    /**
     * Sets the value of the extraOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraOptions(String value) {
        this.extraOptions = value;
    }

}
