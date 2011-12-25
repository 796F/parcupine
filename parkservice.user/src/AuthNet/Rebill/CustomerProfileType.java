
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerProfileType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}CustomerProfileBaseType">
 *       &lt;sequence>
 *         &lt;element name="paymentProfiles" type="{https://api.authorize.net/soap/v1/}ArrayOfCustomerPaymentProfileType" minOccurs="0"/>
 *         &lt;element name="shipToList" type="{https://api.authorize.net/soap/v1/}ArrayOfCustomerAddressType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerProfileType", propOrder = {
    "paymentProfiles",
    "shipToList"
})
public class CustomerProfileType
    extends CustomerProfileBaseType
{

    protected ArrayOfCustomerPaymentProfileType paymentProfiles;
    protected ArrayOfCustomerAddressType shipToList;

    /**
     * Gets the value of the paymentProfiles property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCustomerPaymentProfileType }
     *     
     */
    public ArrayOfCustomerPaymentProfileType getPaymentProfiles() {
        return paymentProfiles;
    }

    /**
     * Sets the value of the paymentProfiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCustomerPaymentProfileType }
     *     
     */
    public void setPaymentProfiles(ArrayOfCustomerPaymentProfileType value) {
        this.paymentProfiles = value;
    }

    /**
     * Gets the value of the shipToList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCustomerAddressType }
     *     
     */
    public ArrayOfCustomerAddressType getShipToList() {
        return shipToList;
    }

    /**
     * Sets the value of the shipToList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCustomerAddressType }
     *     
     */
    public void setShipToList(ArrayOfCustomerAddressType value) {
        this.shipToList = value;
    }

}
