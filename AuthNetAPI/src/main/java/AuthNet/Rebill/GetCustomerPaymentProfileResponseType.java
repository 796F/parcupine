
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCustomerPaymentProfileResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCustomerPaymentProfileResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ANetApiResponseType">
 *       &lt;sequence>
 *         &lt;element name="paymentProfile" type="{https://api.authorize.net/soap/v1/}CustomerPaymentProfileMaskedType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCustomerPaymentProfileResponseType", propOrder = {
    "paymentProfile"
})
public class GetCustomerPaymentProfileResponseType
    extends ANetApiResponseType
{

    protected CustomerPaymentProfileMaskedType paymentProfile;

    /**
     * Gets the value of the paymentProfile property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerPaymentProfileMaskedType }
     *     
     */
    public CustomerPaymentProfileMaskedType getPaymentProfile() {
        return paymentProfile;
    }

    /**
     * Sets the value of the paymentProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerPaymentProfileMaskedType }
     *     
     */
    public void setPaymentProfile(CustomerPaymentProfileMaskedType value) {
        this.paymentProfile = value;
    }

}
