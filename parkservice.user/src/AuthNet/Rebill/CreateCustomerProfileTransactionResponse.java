
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="CreateCustomerProfileTransactionResult" type="{https://api.authorize.net/soap/v1/}CreateCustomerProfileTransactionResponseType" minOccurs="0"/>
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
    "createCustomerProfileTransactionResult"
})
@XmlRootElement(name = "CreateCustomerProfileTransactionResponse")
public class CreateCustomerProfileTransactionResponse {

    @XmlElement(name = "CreateCustomerProfileTransactionResult")
    protected CreateCustomerProfileTransactionResponseType createCustomerProfileTransactionResult;

    /**
     * Gets the value of the createCustomerProfileTransactionResult property.
     * 
     * @return
     *     possible object is
     *     {@link CreateCustomerProfileTransactionResponseType }
     *     
     */
    public CreateCustomerProfileTransactionResponseType getCreateCustomerProfileTransactionResult() {
        return createCustomerProfileTransactionResult;
    }

    /**
     * Sets the value of the createCustomerProfileTransactionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateCustomerProfileTransactionResponseType }
     *     
     */
    public void setCreateCustomerProfileTransactionResult(CreateCustomerProfileTransactionResponseType value) {
        this.createCustomerProfileTransactionResult = value;
    }

}
