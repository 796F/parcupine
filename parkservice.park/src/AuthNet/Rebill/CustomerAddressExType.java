
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerAddressExType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerAddressExType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}CustomerAddressType">
 *       &lt;sequence>
 *         &lt;element name="customerAddressId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerAddressExType", propOrder = {
    "customerAddressId"
})
public class CustomerAddressExType
    extends CustomerAddressType
{

    protected long customerAddressId;

    /**
     * Gets the value of the customerAddressId property.
     * 
     */
    public long getCustomerAddressId() {
        return customerAddressId;
    }

    /**
     * Sets the value of the customerAddressId property.
     * 
     */
    public void setCustomerAddressId(long value) {
        this.customerAddressId = value;
    }

}
