
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValidationModeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ValidationModeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="none"/>
 *     &lt;enumeration value="testMode"/>
 *     &lt;enumeration value="liveMode"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ValidationModeEnum")
@XmlEnum
public enum ValidationModeEnum {

    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("testMode")
    TEST_MODE("testMode"),
    @XmlEnumValue("liveMode")
    LIVE_MODE("liveMode");
    private final String value;

    ValidationModeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ValidationModeEnum fromValue(String v) {
        for (ValidationModeEnum c: ValidationModeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
