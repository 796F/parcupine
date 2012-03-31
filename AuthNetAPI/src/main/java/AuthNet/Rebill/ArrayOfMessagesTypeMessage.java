
package AuthNet.Rebill;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfMessagesTypeMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfMessagesTypeMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessagesTypeMessage" type="{https://api.authorize.net/soap/v1/}MessagesTypeMessage" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfMessagesTypeMessage", propOrder = {
    "messagesTypeMessage"
})
public class ArrayOfMessagesTypeMessage {

    @XmlElement(name = "MessagesTypeMessage", nillable = true)
    protected List<MessagesTypeMessage> messagesTypeMessage;

    /**
     * Gets the value of the messagesTypeMessage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messagesTypeMessage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessagesTypeMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessagesTypeMessage }
     * 
     * 
     */
    public List<MessagesTypeMessage> getMessagesTypeMessage() {
        if (messagesTypeMessage == null) {
            messagesTypeMessage = new ArrayList<MessagesTypeMessage>();
        }
        return this.messagesTypeMessage;
    }

}
