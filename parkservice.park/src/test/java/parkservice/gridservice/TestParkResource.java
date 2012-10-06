package parkservice.gridservice;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import junit.framework.TestCase;
import parkservice.model.AuthRequest;
import parkservice.model.ParkRequest;
import parkservice.model.ParkResponse;
import parkservice.resources.ParkResource;

public class TestParkResource extends TestCase {

	public void testPilotParkUser() {
		ParkResource parkResource = new ParkResource();
		ParkRequest parkRequest = new ParkRequest();
		AuthRequest authRequest = new AuthRequest();
		authRequest.setEmail("TestUser@PaymentAccount.test");
		authRequest.setPassword("TestPassword");
		parkRequest.setUserInfo(authRequest);
		parkRequest.setUid(14L);
		parkRequest.setDurationMinutes(10);
		parkRequest.setSpotId(111);
		
		JAXBElement<ParkRequest> testRequest = new JAXBElement<ParkRequest>(
				new QName("Test"), ParkRequest.class, parkRequest);
		
		ParkResponse response = parkResource.pilotParkUser(testRequest);
		System.out.println(response);
		
	}
}
