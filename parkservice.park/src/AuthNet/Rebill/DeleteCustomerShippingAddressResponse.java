
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
 *         &lt;element name="DeleteCustomerShippingAddressResult" type="{https://api.authorize.net/soap/v1/}DeleteCustomerShippingAddressResponseType" minOccurs="0"/>
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
    "deleteCustomerShippingAddressResult"
})
@XmlRootElement(name = "DeleteCustomerShippingAddressResponse")
public class DeleteCustomerShippingAddressResponse {

    @XmlElement(name = "DeleteCustomerShippingAddressResult")
    protected DeleteCustomerShippingAddressResponseType deleteCustomerShippingAddressResult;

    /**
     * Gets the value of the deleteCustomerShippingAddressResult property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteCustomerShippingAddressResponseType }
     *     
     */
    public DeleteCustomerShippingAddressResponseType getDeleteCustomerShippingAddressResult() {
        return deleteCustomerShippingAddressResult;
    }

    /**
     * Sets the value of the deleteCustomerShippingAddressResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteCustomerShippingAddressResponseType }
     *     
     */
    public void setDeleteCustomerShippingAddressResult(DeleteCustomerShippingAddressResponseType value) {
        this.deleteCustomerShippingAddressResult = value;
    }

}
