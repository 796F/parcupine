
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateCustomerProfileResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateCustomerProfileResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ANetApiResponseType">
 *       &lt;sequence>
 *         &lt;element name="customerProfileId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="customerPaymentProfileIdList" type="{https://api.authorize.net/soap/v1/}ArrayOfLong" minOccurs="0"/>
 *         &lt;element name="customerShippingAddressIdList" type="{https://api.authorize.net/soap/v1/}ArrayOfLong" minOccurs="0"/>
 *         &lt;element name="validationDirectResponseList" type="{https://api.authorize.net/soap/v1/}ArrayOfString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateCustomerProfileResponseType", propOrder = {
    "customerProfileId",
    "customerPaymentProfileIdList",
    "customerShippingAddressIdList",
    "validationDirectResponseList"
})
public class CreateCustomerProfileResponseType
    extends ANetApiResponseType
{

    protected long customerProfileId;
    protected ArrayOfLong customerPaymentProfileIdList;
    protected ArrayOfLong customerShippingAddressIdList;
    protected ArrayOfString validationDirectResponseList;

    /**
     * Gets the value of the customerProfileId property.
     * 
     */
    public long getCustomerProfileId() {
        return customerProfileId;
    }

    /**
     * Sets the value of the customerProfileId property.
     * 
     */
    public void setCustomerProfileId(long value) {
        this.customerProfileId = value;
    }

    /**
     * Gets the value of the customerPaymentProfileIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLong }
     *     
     */
    public ArrayOfLong getCustomerPaymentProfileIdList() {
        return customerPaymentProfileIdList;
    }

    /**
     * Sets the value of the customerPaymentProfileIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLong }
     *     
     */
    public void setCustomerPaymentProfileIdList(ArrayOfLong value) {
        this.customerPaymentProfileIdList = value;
    }

    /**
     * Gets the value of the customerShippingAddressIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLong }
     *     
     */
    public ArrayOfLong getCustomerShippingAddressIdList() {
        return customerShippingAddressIdList;
    }

    /**
     * Sets the value of the customerShippingAddressIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLong }
     *     
     */
    public void setCustomerShippingAddressIdList(ArrayOfLong value) {
        this.customerShippingAddressIdList = value;
    }

    /**
     * Gets the value of the validationDirectResponseList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getValidationDirectResponseList() {
        return validationDirectResponseList;
    }

    /**
     * Sets the value of the validationDirectResponseList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setValidationDirectResponseList(ArrayOfString value) {
        this.validationDirectResponseList = value;
    }

}
