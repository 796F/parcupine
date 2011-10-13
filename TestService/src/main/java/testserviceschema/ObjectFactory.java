
package testserviceschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the testserviceschema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TestResponse_QNAME = new QName("urn:TestServiceSchema", "TestResponse");
    private final static QName _TestRequest_QNAME = new QName("urn:TestServiceSchema", "TestRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testserviceschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestRequestType }
     * 
     */
    public TestRequestType createTestRequestType() {
        return new TestRequestType();
    }

    /**
     * Create an instance of {@link TestResponseType }
     * 
     */
    public TestResponseType createTestResponseType() {
        return new TestResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:TestServiceSchema", name = "TestResponse")
    public JAXBElement<TestResponseType> createTestResponse(TestResponseType value) {
        return new JAXBElement<TestResponseType>(_TestResponse_QNAME, TestResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:TestServiceSchema", name = "TestRequest")
    public JAXBElement<TestRequestType> createTestRequest(TestRequestType value) {
        return new JAXBElement<TestRequestType>(_TestRequest_QNAME, TestRequestType.class, null, value);
    }

}
