
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateCustomerPaymentProfileResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateCustomerPaymentProfileResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ANetApiResponseType">
 *       &lt;sequence>
 *         &lt;element name="customerPaymentProfileId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="validationDirectResponse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateCustomerPaymentProfileResponseType", propOrder = {
    "customerPaymentProfileId",
    "validationDirectResponse"
})
public class CreateCustomerPaymentProfileResponseType
    extends ANetApiResponseType
{

    protected long customerPaymentProfileId;
    protected String validationDirectResponse;

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
     * Gets the value of the validationDirectResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidationDirectResponse() {
        return validationDirectResponse;
    }

    /**
     * Sets the value of the validationDirectResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidationDirectResponse(String value) {
        this.validationDirectResponse = value;
    }

}
