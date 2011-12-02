
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentScheduleTypeInterval complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentScheduleTypeInterval">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="unit" type="{https://api.authorize.net/soap/v1/}ARBSubscriptionUnitEnum"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentScheduleTypeInterval", propOrder = {
    "length",
    "unit"
})
public class PaymentScheduleTypeInterval {

    protected short length;
    @XmlElement(required = true)
    protected ARBSubscriptionUnitEnum unit;

    /**
     * Gets the value of the length property.
     * 
     */
    public short getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     */
    public void setLength(short value) {
        this.length = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link ARBSubscriptionUnitEnum }
     *     
     */
    public ARBSubscriptionUnitEnum getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARBSubscriptionUnitEnum }
     *     
     */
    public void setUnit(ARBSubscriptionUnitEnum value) {
        this.unit = value;
    }

}
