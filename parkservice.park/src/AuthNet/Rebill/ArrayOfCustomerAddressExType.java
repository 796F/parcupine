
package AuthNet.Rebill;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCustomerAddressExType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCustomerAddressExType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustomerAddressExType" type="{https://api.authorize.net/soap/v1/}CustomerAddressExType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCustomerAddressExType", propOrder = {
    "customerAddressExType"
})
public class ArrayOfCustomerAddressExType {

    @XmlElement(name = "CustomerAddressExType", nillable = true)
    protected List<CustomerAddressExType> customerAddressExType;

    /**
     * Gets the value of the customerAddressExType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerAddressExType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerAddressExType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerAddressExType }
     * 
     * 
     */
    public List<CustomerAddressExType> getCustomerAddressExType() {
        if (customerAddressExType == null) {
            customerAddressExType = new ArrayList<CustomerAddressExType>();
        }
        return this.customerAddressExType;
    }

}
