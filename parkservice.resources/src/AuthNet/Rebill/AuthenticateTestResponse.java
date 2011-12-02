
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
 *         &lt;element name="AuthenticateTestResult" type="{https://api.authorize.net/soap/v1/}ANetApiResponseType" minOccurs="0"/>
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
    "authenticateTestResult"
})
@XmlRootElement(name = "AuthenticateTestResponse")
public class AuthenticateTestResponse {

    @XmlElement(name = "AuthenticateTestResult")
    protected ANetApiResponseType authenticateTestResult;

    /**
     * Gets the value of the authenticateTestResult property.
     * 
     * @return
     *     possible object is
     *     {@link ANetApiResponseType }
     *     
     */
    public ANetApiResponseType getAuthenticateTestResult() {
        return authenticateTestResult;
    }

    /**
     * Sets the value of the authenticateTestResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ANetApiResponseType }
     *     
     */
    public void setAuthenticateTestResult(ANetApiResponseType value) {
        this.authenticateTestResult = value;
    }

}
