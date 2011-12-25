
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
 *         &lt;element name="ValidateCustomerPaymentProfileResult" type="{https://api.authorize.net/soap/v1/}ValidateCustomerPaymentProfileResponseType" minOccurs="0"/>
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
    "validateCustomerPaymentProfileResult"
})
@XmlRootElement(name = "ValidateCustomerPaymentProfileResponse")
public class ValidateCustomerPaymentProfileResponse {

    @XmlElement(name = "ValidateCustomerPaymentProfileResult")
    protected ValidateCustomerPaymentProfileResponseType validateCustomerPaymentProfileResult;

    /**
     * Gets the value of the validateCustomerPaymentProfileResult property.
     * 
     * @return
     *     possible object is
     *     {@link ValidateCustomerPaymentProfileResponseType }
     *     
     */
    public ValidateCustomerPaymentProfileResponseType getValidateCustomerPaymentProfileResult() {
        return validateCustomerPaymentProfileResult;
    }

    /**
     * Sets the value of the validateCustomerPaymentProfileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValidateCustomerPaymentProfileResponseType }
     *     
     */
    public void setValidateCustomerPaymentProfileResult(ValidateCustomerPaymentProfileResponseType value) {
        this.validateCustomerPaymentProfileResult = value;
    }

}
