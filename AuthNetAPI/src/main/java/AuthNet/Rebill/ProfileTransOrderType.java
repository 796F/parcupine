
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProfileTransOrderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProfileTransOrderType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ProfileTransAmountType">
 *       &lt;sequence>
 *         &lt;element name="customerProfileId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="customerPaymentProfileId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="customerShippingAddressId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="order" type="{https://api.authorize.net/soap/v1/}OrderExType" minOccurs="0"/>
 *         &lt;element name="taxExempt" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="recurringBilling" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="cardCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProfileTransOrderType", propOrder = {
    "customerProfileId",
    "customerPaymentProfileId",
    "customerShippingAddressId",
    "order",
    "taxExempt",
    "recurringBilling",
    "cardCode"
})
@XmlSeeAlso({
    ProfileTransAuthCaptureType.class,
    ProfileTransAuthOnlyType.class,
    ProfileTransCaptureOnlyType.class
})
public class ProfileTransOrderType
    extends ProfileTransAmountType
{

    protected long customerProfileId;
    protected long customerPaymentProfileId;
    protected Long customerShippingAddressId;
    protected OrderExType order;
    protected Boolean taxExempt;
    protected Boolean recurringBilling;
    protected String cardCode;

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
     * Gets the value of the customerPaymentProfileId property.
     * 
     */
    public long getCustomerPaymentProfileId() {
        return customerPaymentProfileId;
    }

    /**
     * Sets the value of the customerPaymentProfileId property.
     * 
     */
    public void setCustomerPaymentProfileId(long value) {
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
     * Gets the value of the taxExempt property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTaxExempt() {
        return taxExempt;
    }

    /**
     * Sets the value of the taxExempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTaxExempt(Boolean value) {
        this.taxExempt = value;
    }

    /**
     * Gets the value of the recurringBilling property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecurringBilling() {
        return recurringBilling;
    }

    /**
     * Sets the value of the recurringBilling property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecurringBilling(Boolean value) {
        this.recurringBilling = value;
    }

    /**
     * Gets the value of the cardCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardCode() {
        return cardCode;
    }

    /**
     * Sets the value of the cardCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardCode(String value) {
        this.cardCode = value;
    }

}
