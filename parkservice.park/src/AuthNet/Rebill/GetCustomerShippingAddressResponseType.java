
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCustomerShippingAddressResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCustomerShippingAddressResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ANetApiResponseType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{https://api.authorize.net/soap/v1/}CustomerAddressExType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCustomerShippingAddressResponseType", propOrder = {
    "address"
})
public class GetCustomerShippingAddressResponseType
    extends ANetApiResponseType
{

    protected CustomerAddressExType address;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerAddressExType }
     *     
     */
    public CustomerAddressExType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerAddressExType }
     *     
     */
    public void setAddress(CustomerAddressExType value) {
        this.address = value;
    }

}
