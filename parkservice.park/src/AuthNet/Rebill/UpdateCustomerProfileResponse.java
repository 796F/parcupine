
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
 *         &lt;element name="UpdateCustomerProfileResult" type="{https://api.authorize.net/soap/v1/}UpdateCustomerProfileResponseType" minOccurs="0"/>
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
    "updateCustomerProfileResult"
})
@XmlRootElement(name = "UpdateCustomerProfileResponse")
public class UpdateCustomerProfileResponse {

    @XmlElement(name = "UpdateCustomerProfileResult")
    protected UpdateCustomerProfileResponseType updateCustomerProfileResult;

    /**
     * Gets the value of the updateCustomerProfileResult property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateCustomerProfileResponseType }
     *     
     */
    public UpdateCustomerProfileResponseType getUpdateCustomerProfileResult() {
        return updateCustomerProfileResult;
    }

    /**
     * Sets the value of the updateCustomerProfileResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateCustomerProfileResponseType }
     *     
     */
    public void setUpdateCustomerProfileResult(UpdateCustomerProfileResponseType value) {
        this.updateCustomerProfileResult = value;
    }

}
