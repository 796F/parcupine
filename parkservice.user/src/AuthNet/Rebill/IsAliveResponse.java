
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
 *         &lt;element name="IsAliveResult" type="{https://api.authorize.net/soap/v1/}ANetApiResponseType" minOccurs="0"/>
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
    "isAliveResult"
})
@XmlRootElement(name = "IsAliveResponse")
public class IsAliveResponse {

    @XmlElement(name = "IsAliveResult")
    protected ANetApiResponseType isAliveResult;

    /**
     * Gets the value of the isAliveResult property.
     * 
     * @return
     *     possible object is
     *     {@link ANetApiResponseType }
     *     
     */
    public ANetApiResponseType getIsAliveResult() {
        return isAliveResult;
    }

    /**
     * Sets the value of the isAliveResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ANetApiResponseType }
     *     
     */
    public void setIsAliveResult(ANetApiResponseType value) {
        this.isAliveResult = value;
    }

}
