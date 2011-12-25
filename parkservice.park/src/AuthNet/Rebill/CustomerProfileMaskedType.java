
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerProfileMaskedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerProfileMaskedType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}CustomerProfileExType">
 *       &lt;sequence>
 *         &lt;element name="paymentProfiles" type="{https://api.authorize.net/soap/v1/}ArrayOfCustomerPaymentProfileMaskedType" minOccurs="0"/>
 *         &lt;element name="shipToList" type="{https://api.authorize.net/soap/v1/}ArrayOfCustomerAddressExType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerProfileMaskedType", propOrder = {
    "paymentProfiles",
    "shipToList"
})
public class CustomerProfileMaskedType
    extends CustomerProfileExType
{

    protected ArrayOfCustomerPaymentProfileMaskedType paymentProfiles;
    protected ArrayOfCustomerAddressExType shipToList;

    /**
     * Gets the value of the paymentProfiles property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCustomerPaymentProfileMaskedType }
     *     
     */
    public ArrayOfCustomerPaymentProfileMaskedType getPaymentProfiles() {
        return paymentProfiles;
    }

    /**
     * Sets the value of the paymentProfiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCustomerPaymentProfileMaskedType }
     *     
     */
    public void setPaymentProfiles(ArrayOfCustomerPaymentProfileMaskedType value) {
        this.paymentProfiles = value;
    }

    /**
     * Gets the value of the shipToList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCustomerAddressExType }
     *     
     */
    public ArrayOfCustomerAddressExType getShipToList() {
        return shipToList;
    }

    /**
     * Sets the value of the shipToList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCustomerAddressExType }
     *     
     */
    public void setShipToList(ArrayOfCustomerAddressExType value) {
        this.shipToList = value;
    }

}
