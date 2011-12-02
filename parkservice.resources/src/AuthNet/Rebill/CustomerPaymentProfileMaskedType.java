
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerPaymentProfileMaskedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerPaymentProfileMaskedType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://api.authorize.net/soap/v1/}CustomerPaymentProfileBaseType">
 *       &lt;sequence>
 *         &lt;element name="customerPaymentProfileId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="payment" type="{https://api.authorize.net/soap/v1/}PaymentMaskedType" minOccurs="0"/>
 *         &lt;element name="driversLicense" type="{https://api.authorize.net/soap/v1/}DriversLicenseMaskedType" minOccurs="0"/>
 *         &lt;element name="taxId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPaymentProfileMaskedType", propOrder = {
    "customerPaymentProfileId",
    "payment",
    "driversLicense",
    "taxId"
})
public class CustomerPaymentProfileMaskedType
    extends CustomerPaymentProfileBaseType
{

    protected long customerPaymentProfileId;
    protected PaymentMaskedType payment;
    protected DriversLicenseMaskedType driversLicense;
    protected String taxId;

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
     * Gets the value of the payment property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentMaskedType }
     *     
     */
    public PaymentMaskedType getPayment() {
        return payment;
    }

    /**
     * Sets the value of the payment property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentMaskedType }
     *     
     */
    public void setPayment(PaymentMaskedType value) {
        this.payment = value;
    }

    /**
     * Gets the value of the driversLicense property.
     * 
     * @return
     *     possible object is
     *     {@link DriversLicenseMaskedType }
     *     
     */
    public DriversLicenseMaskedType getDriversLicense() {
        return driversLicense;
    }

    /**
     * Sets the value of the driversLicense property.
     * 
     * @param value
     *     allowed object is
     *     {@link DriversLicenseMaskedType }
     *     
     */
    public void setDriversLicense(DriversLicenseMaskedType value) {
        this.driversLicense = value;
    }

    /**
     * Gets the value of the taxId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * Sets the value of the taxId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxId(String value) {
        this.taxId = value;
    }

}
