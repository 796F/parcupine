
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateCustomerProfileTransactionResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateCustomerProfileTransactionResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}ANetApiResponseType">
 *       &lt;sequence>
 *         &lt;element name="directResponse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateCustomerProfileTransactionResponseType", propOrder = {
    "directResponse"
})
public class CreateCustomerProfileTransactionResponseType
    extends ANetApiResponseType
{

    protected String directResponse;

    /**
     * Gets the value of the directResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectResponse() {
        return directResponse;
    }

    /**
     * Sets the value of the directResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectResponse(String value) {
        this.directResponse = value;
    }

}
