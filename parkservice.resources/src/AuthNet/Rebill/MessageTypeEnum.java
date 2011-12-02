
package AuthNet.Rebill;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Ok"/>
 *     &lt;enumeration value="Error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageTypeEnum")
@XmlEnum
public enum MessageTypeEnum {

    @XmlEnumValue("Ok")
    OK("Ok"),
    @XmlEnumValue("Error")
    ERROR("Error");
    private final String value;

    MessageTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageTypeEnum fromValue(String v) {
        for (MessageTypeEnum c: MessageTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
