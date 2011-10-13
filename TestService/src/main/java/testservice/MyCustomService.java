package testservice;

import java.util.List;

import javax.jws.WebService;

import org.w3c.dom.Element;

import testserviceschema.ObjectFactory;
import testserviceschema.TestRequestType;
import testserviceschema.TestResponseType;

/**
 * Implement the TestServicePortType
 *
 */
@WebService(endpointInterface="testservice.TestServicePortType")
public class MyCustomService implements TestServicePortType {

	public TestResponseType doSomething(TestRequestType request)
	{
		List<Object> contentList = request.getAny();
		
		for (Object item : contentList)
		{
			Element ele = (Element) item;
			System.out.println("-----------------------------------");
			System.out.println("Node Type: " + ele.getNodeName());
			System.out.println("Node Value: " + ele.getTextContent());
			
			
		}
		
		TestResponseType response = new ObjectFactory().createTestResponseType();
		// sample response
		response.getAny().addAll(contentList);
		
		return response;
	}
}
