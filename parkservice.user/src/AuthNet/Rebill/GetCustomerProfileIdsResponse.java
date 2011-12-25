
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
 *         &lt;element name="GetCustomerProfileIdsResult" type="{https://api.authorize.net/soap/v1/}GetCustomerProfileIdsResponseType" minOccurs="0"/>
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
    "getCustomerProfileIdsResult"
})
@XmlRootElement(name = "GetCustomerProfileIdsResponse")
public class GetCustomerProfileIdsResponse {

    @XmlElement(name = "GetCustomerProfileIdsResult")
    protected GetCustomerProfileIdsResponseType getCustomerProfileIdsResult;

    /**
     * Gets the value of the getCustomerProfileIdsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetCustomerProfileIdsResponseType }
     *     
     */
    public GetCustomerProfileIdsResponseType getGetCustomerProfileIdsResult() {
        return getCustomerProfileIdsResult;
    }

    /**
     * Sets the value of the getCustomerProfileIdsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetCustomerProfileIdsResponseType }
     *     
     */
    public void setGetCustomerProfileIdsResult(GetCustomerProfileIdsResponseType value) {
        this.getCustomerProfileIdsResult = value;
    }

}
