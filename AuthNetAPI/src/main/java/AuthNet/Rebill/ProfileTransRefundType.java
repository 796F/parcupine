
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProfileTransRefundType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProfileTransRefundType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ProfileTransAmountType">
 *       &lt;sequence>
 *         &lt;element name="customerProfileId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="customerPaymentProfileId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="customerShippingAddressId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="creditCardNumberMasked" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bankRoutingNumberMasked" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bankAccountNumberMasked" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="order" type="{https://api.authorize.net/soap/v1/}OrderExType" minOccurs="0"/>
 *         &lt;element name="transId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProfileTransRefundType", propOrder = {
    "customerProfileId",
    "customerPaymentProfileId",
    "customerShippingAddressId",
    "creditCardNumberMasked",
    "bankRoutingNumberMasked",
    "bankAccountNumberMasked",
    "order",
    "transId"
})
public class ProfileTransRefundType
    extends ProfileTransAmountType
{

    protected Long customerProfileId;
    protected Long customerPaymentProfileId;
    protected Long customerShippingAddressId;
    protected String creditCardNumberMasked;
    protected String bankRoutingNumberMasked;
    protected String bankAccountNumberMasked;
    protected OrderExType order;
    protected String transId;

    /**
     * Gets the value of the customerProfileId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCustomerProfileId() {
        return customerProfileId;
    }

    /**
     * Sets the value of the customerProfileId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustomerProfileId(Long value) {
        this.customerProfileId = value;
    }

    /**
     * Gets the value of the customerPaymentProfileId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCustomerPaymentProfileId() {
        return customerPaymentProfileId;
    }

    /**
     * Sets the value of the customerPaymentProfileId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustomerPaymentProfileId(Long value) {
        this.customerPaymentProfileId = value;
    }

    /**
     * Gets the value of the customerShippingAddressId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCustomerShippingAddressId() {
        return customerShippingAddressId;
    }

    /**
     * Sets the value of the customerShippingAddressId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustomerShippingAddressId(Long value) {
        this.customerShippingAddressId = value;
    }

    /**
     * Gets the value of the creditCardNumberMasked property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditCardNumberMasked() {
        return creditCardNumberMasked;
    }

    /**
     * Sets the value of the creditCardNumberMasked property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditCardNumberMasked(String value) {
        this.creditCardNumberMasked = value;
    }

    /**
     * Gets the value of the bankRoutingNumberMasked property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankRoutingNumberMasked() {
        return bankRoutingNumberMasked;
    }

    /**
     * Sets the value of the bankRoutingNumberMasked property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankRoutingNumberMasked(String value) {
        this.bankRoutingNumberMasked = value;
    }

    /**
     * Gets the value of the bankAccountNumberMasked property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankAccountNumberMasked() {
        return bankAccountNumberMasked;
    }

    /**
     * Sets the value of the bankAccountNumberMasked property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankAccountNumberMasked(String value) {
        this.bankAccountNumberMasked = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link OrderExType }
     *     
     */
    public OrderExType getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderExType }
     *     
     */
    public void setOrder(OrderExType value) {
        this.order = value;
    }

    /**
     * Gets the value of the transId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransId() {
        return transId;
    }

    /**
     * Sets the value of the transId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransId(String value) {
        this.transId = value;
    }

}
