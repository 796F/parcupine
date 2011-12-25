
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EcheckTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EcheckTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PPD"/>
 *     &lt;enumeration value="WEB"/>
 *     &lt;enumeration value="CCD"/>
 *     &lt;enumeration value="TEL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EcheckTypeEnum")
@XmlEnum
public enum EcheckTypeEnum {

    PPD,
    WEB,
    CCD,
    TEL;

    public String value() {
        return name();
    }

    public static EcheckTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
