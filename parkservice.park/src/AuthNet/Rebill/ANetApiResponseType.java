
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ANetApiResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ANetApiResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultCode" type="{https://api.authorize.net/soap/v1/}MessageTypeEnum"/>
 *         &lt;element name="messages" type="{https://api.authorize.net/soap/v1/}ArrayOfMessagesTypeMessage" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ANetApiResponseType", propOrder = {
    "resultCode",
    "messages"
})
@XmlSeeAlso({
    ARBCreateSubscriptionResponseType.class,
    UpdateCustomerProfileResponseType.class,
    ValidateCustomerPaymentProfileResponseType.class,
    ARBUpdateSubscriptionResponseType.class,
    DeleteCustomerShippingAddressResponseType.class,
    DeleteCustomerPaymentProfileResponseType.class,
    DeleteCustomerProfileResponseType.class,
    UpdateCustomerPaymentProfileResponseType.class,
    CreateCustomerPaymentProfileResponseType.class,
    GetCustomerShippingAddressResponseType.class,
    GetCustomerProfileIdsResponseType.class,
    UpdateCustomerShippingAddressResponseType.class,
    GetCustomerProfileResponseType.class,
    CreateCustomerProfileResponseType.class,
    ARBCancelSubscriptionResponseType.class,
    CreateCustomerShippingAddressResponseType.class,
    GetCustomerPaymentProfileResponseType.class,
    CreateCustomerProfileTransactionResponseType.class
})
public class ANetApiResponseType {

    @XmlElement(required = true)
    protected MessageTypeEnum resultCode;
    protected ArrayOfMessagesTypeMessage messages;

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link MessageTypeEnum }
     *     
     */
    public MessageTypeEnum getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageTypeEnum }
     *     
     */
    public void setResultCode(MessageTypeEnum value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the messages property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMessagesTypeMessage }
     *     
     */
    public ArrayOfMessagesTypeMessage getMessages() {
        return messages;
    }

    /**
     * Sets the value of the messages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMessagesTypeMessage }
     *     
     */
    public void setMessages(ArrayOfMessagesTypeMessage value) {
        this.messages = value;
    }

}
