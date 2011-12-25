
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProfileTransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProfileTransactionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="profileTransAuthOnly" type="{https://api.authorize.net/soap/v1/}ProfileTransAuthOnlyType" minOccurs="0"/>
 *           &lt;element name="profileTransAuthCapture" type="{https://api.authorize.net/soap/v1/}ProfileTransAuthCaptureType" minOccurs="0"/>
 *           &lt;element name="profileTransRefund" type="{https://api.authorize.net/soap/v1/}ProfileTransRefundType" minOccurs="0"/>
 *           &lt;element name="profileTransCaptureOnly" type="{https://api.authorize.net/soap/v1/}ProfileTransCaptureOnlyType" minOccurs="0"/>
 *           &lt;element name="profileTransPriorAuthCapture" type="{https://api.authorize.net/soap/v1/}ProfileTransPriorAuthCaptureType" minOccurs="0"/>
 *           &lt;element name="profileTransVoid" type="{https://api.authorize.net/soap/v1/}ProfileTransVoidType" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProfileTransactionType", propOrder = {
    "profileTransAuthOnly",
    "profileTransAuthCapture",
    "profileTransRefund",
    "profileTransCaptureOnly",
    "profileTransPriorAuthCapture",
    "profileTransVoid"
})
public class ProfileTransactionType {

    protected ProfileTransAuthOnlyType profileTransAuthOnly;
    protected ProfileTransAuthCaptureType profileTransAuthCapture;
    protected ProfileTransRefundType profileTransRefund;
    protected ProfileTransCaptureOnlyType profileTransCaptureOnly;
    protected ProfileTransPriorAuthCaptureType profileTransPriorAuthCapture;
    protected ProfileTransVoidType profileTransVoid;

    /**
     * Gets the value of the profileTransAuthOnly property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransAuthOnlyType }
     *     
     */
    public ProfileTransAuthOnlyType getProfileTransAuthOnly() {
        return profileTransAuthOnly;
    }

    /**
     * Sets the value of the profileTransAuthOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransAuthOnlyType }
     *     
     */
    public void setProfileTransAuthOnly(ProfileTransAuthOnlyType value) {
        this.profileTransAuthOnly = value;
    }

    /**
     * Gets the value of the profileTransAuthCapture property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransAuthCaptureType }
     *     
     */
    public ProfileTransAuthCaptureType getProfileTransAuthCapture() {
        return profileTransAuthCapture;
    }

    /**
     * Sets the value of the profileTransAuthCapture property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransAuthCaptureType }
     *     
     */
    public void setProfileTransAuthCapture(ProfileTransAuthCaptureType value) {
        this.profileTransAuthCapture = value;
    }

    /**
     * Gets the value of the profileTransRefund property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransRefundType }
     *     
     */
    public ProfileTransRefundType getProfileTransRefund() {
        return profileTransRefund;
    }

    /**
     * Sets the value of the profileTransRefund property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransRefundType }
     *     
     */
    public void setProfileTransRefund(ProfileTransRefundType value) {
        this.profileTransRefund = value;
    }

    /**
     * Gets the value of the profileTransCaptureOnly property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransCaptureOnlyType }
     *     
     */
    public ProfileTransCaptureOnlyType getProfileTransCaptureOnly() {
        return profileTransCaptureOnly;
    }

    /**
     * Sets the value of the profileTransCaptureOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransCaptureOnlyType }
     *     
     */
    public void setProfileTransCaptureOnly(ProfileTransCaptureOnlyType value) {
        this.profileTransCaptureOnly = value;
    }

    /**
     * Gets the value of the profileTransPriorAuthCapture property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransPriorAuthCaptureType }
     *     
     */
    public ProfileTransPriorAuthCaptureType getProfileTransPriorAuthCapture() {
        return profileTransPriorAuthCapture;
    }

    /**
     * Sets the value of the profileTransPriorAuthCapture property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransPriorAuthCaptureType }
     *     
     */
    public void setProfileTransPriorAuthCapture(ProfileTransPriorAuthCaptureType value) {
        this.profileTransPriorAuthCapture = value;
    }

    /**
     * Gets the value of the profileTransVoid property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileTransVoidType }
     *     
     */
    public ProfileTransVoidType getProfileTransVoid() {
        return profileTransVoid;
    }

    /**
     * Sets the value of the profileTransVoid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileTransVoidType }
     *     
     */
    public void setProfileTransVoid(ProfileTransVoidType value) {
        this.profileTransVoid = value;
    }

}
