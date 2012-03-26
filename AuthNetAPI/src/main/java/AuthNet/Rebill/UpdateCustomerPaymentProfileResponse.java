
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
 *         &lt;element name="UpdateCustomerPaymentProfileResult" type="{https://api.authorize.net/soap/v1/}UpdateCustomerPaymentProfileResponseType" minOccurs="0"/>
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
    "updateCustomerPaymentProfileResult"
})
@XmlRootElement(name = "UpdateCustomerPaymentProfileResponse")
public class UpdateCustomerPaymentProfileResponse {

    @XmlElement(name = "UpdateCustomerPaymentProfileResult")
    protected UpdateCustomerPaymentProfileResponseType updateCustomerPaymentProfileResult;

    /**
     * Gets the value of the updateCustomerPaymentProfileResult property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateCustomerPaymentProfileResponseType }
     *     
     */
    public UpdateCustomerPaymentProfileResponseType getUpdateCustomerPaymentProfileResult() {
        return updateCustomerPaymentProfileResult;
    }

    /**
     * Sets the value of the updateCustomerPaymentProfileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateCustomerPaymentProfileResponseType }
     *     
     */
    public void setUpdateCustomerPaymentProfileResult(UpdateCustomerPaymentProfileResponseType value) {
        this.updateCustomerPaymentProfileResult = value;
    }

}
