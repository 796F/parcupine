
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
 *         &lt;element name="CreateCustomerPaymentProfileResult" type="{https://api.authorize.net/soap/v1/}CreateCustomerPaymentProfileResponseType" minOccurs="0"/>
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
    "createCustomerPaymentProfileResult"
})
@XmlRootElement(name = "CreateCustomerPaymentProfileResponse")
public class CreateCustomerPaymentProfileResponse {

    @XmlElement(name = "CreateCustomerPaymentProfileResult")
    protected CreateCustomerPaymentProfileResponseType createCustomerPaymentProfileResult;

    /**
     * Gets the value of the createCustomerPaymentProfileResult property.
     * 
     * @return
     *     possible object is
     *     {@link CreateCustomerPaymentProfileResponseType }
     *     
     */
    public CreateCustomerPaymentProfileResponseType getCreateCustomerPaymentProfileResult() {
        return createCustomerPaymentProfileResult;
    }

    /**
     * Sets the value of the createCustomerPaymentProfileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateCustomerPaymentProfileResponseType }
     *     
     */
    public void setCreateCustomerPaymentProfileResult(CreateCustomerPaymentProfileResponseType value) {
        this.createCustomerPaymentProfileResult = value;
    }

}
