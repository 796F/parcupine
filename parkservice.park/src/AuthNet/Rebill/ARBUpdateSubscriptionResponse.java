
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ARBUpdateSubscriptionResult" type="{https://api.authorize.net/soap/v1/}ARBUpdateSubscriptionResponseType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "arbUpdateSubscriptionResult"
})
@XmlRootElement(name = "ARBUpdateSubscriptionResponse")
public class ARBUpdateSubscriptionResponse {

    @XmlElement(name = "ARBUpdateSubscriptionResult")
    protected ARBUpdateSubscriptionResponseType arbUpdateSubscriptionResult;

    /**
     * Gets the value of the arbUpdateSubscriptionResult property.
     * 
     * @return
     *     possible object is
     *     {@link ARBUpdateSubscriptionResponseType }
     *     
     */
    public ARBUpdateSubscriptionResponseType getARBUpdateSubscriptionResult() {
        return arbUpdateSubscriptionResult;
    }

    /**
     * Sets the value of the arbUpdateSubscriptionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARBUpdateSubscriptionResponseType }
     *     
     */
    public void setARBUpdateSubscriptionResult(ARBUpdateSubscriptionResponseType value) {
        this.arbUpdateSubscriptionResult = value;
    }

}
