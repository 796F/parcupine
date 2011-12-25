
package AuthNet.Rebill;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCustomerPaymentProfileMaskedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCustomerPaymentProfileMaskedType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustomerPaymentProfileMaskedType" type="{https://api.authorize.net/soap/v1/}CustomerPaymentProfileMaskedType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCustomerPaymentProfileMaskedType", propOrder = {
    "customerPaymentProfileMaskedType"
})
public class ArrayOfCustomerPaymentProfileMaskedType {

    @XmlElement(name = "CustomerPaymentProfileMaskedType", nillable = true)
    protected List<CustomerPaymentProfileMaskedType> customerPaymentProfileMaskedType;

    /**
     * Gets the value of the customerPaymentProfileMaskedType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerPaymentProfileMaskedType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerPaymentProfileMaskedType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerPaymentProfileMaskedType }
     * 
     * 
     */
    public List<CustomerPaymentProfileMaskedType> getCustomerPaymentProfileMaskedType() {
        if (customerPaymentProfileMaskedType == null) {
            customerPaymentProfileMaskedType = new ArrayList<CustomerPaymentProfileMaskedType>();
        }
        return this.customerPaymentProfileMaskedType;
    }

}
