package parkservice.gridservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import junit.framework.TestCase;
import parkservice.model.AuthRequest;
import parkservice.model.ParkRequest;
import parkservice.model.ParkResponse;
import parkservice.model.RefillRequest;
import parkservice.model.RefillResponse;
import parkservice.resources.ParkResource;
import parkservice.userscore.model.AddUserReportingRequest;
import parkservice.userscore.model.GetUserScoreRequest;
import parkservice.userscore.model.GetUserScoreResponse;
import parkservice.userscore.model.UpdateUserScoreRequest;

public class TestParkResource extends TestCase {
	
	private static final long UNIT_TEST_USER_ID = 14L;
	private ParkResource parkResource = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		parkResource = new ParkResource();
	}

	public void _testPilotParkUser() {
		GetUserScoreRequest getUserScoreRequest = new GetUserScoreRequest();
		getUserScoreRequest.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> testRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequest);
		GetUserScoreResponse oldScoreResponse = parkResource.getUserScore(testRequest);
		
		// park
		ParkRequest parkRequest = new ParkRequest();
		AuthRequest authRequest = new AuthRequest();
		authRequest.setEmail("TestUser@PaymentAccount.test");
		authRequest.setPassword("TestPassword");
		parkRequest.setUserInfo(authRequest);
		parkRequest.setUid(UNIT_TEST_USER_ID);
		parkRequest.setDurationMinutes(1);
		parkRequest.setSpotId(111);
		JAXBElement<ParkRequest> testParkRequest = new JAXBElement<ParkRequest>(
				new QName("Test"), ParkRequest.class, parkRequest);
		ParkResponse response = parkResource.pilotParkUser(testParkRequest);
		System.out.println(response.getResp());
		
		// get the new score
		GetUserScoreRequest getUserScoreRequestNew = new GetUserScoreRequest();
		getUserScoreRequestNew.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> newScoreRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequestNew);
		GetUserScoreResponse newScoreResponse = parkResource.getUserScore(newScoreRequest);
		
		assertEquals(oldScoreResponse.getScore1() - 1, newScoreResponse.getScore1());
		assertEquals(oldScoreResponse.getScore2(), newScoreResponse.getScore2());
		assertEquals(oldScoreResponse.getScore3(), newScoreResponse.getScore3());
		assertEquals(oldScoreResponse.getUserId(), newScoreResponse.getUserId());
		
		System.out.println(newScoreResponse.getScore1());
	}
	
	public void testPilotRefillUser() {
		GetUserScoreRequest getUserScoreRequest = new GetUserScoreRequest();
		getUserScoreRequest.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> testRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequest);
		GetUserScoreResponse oldScoreResponse = parkResource.getUserScore(testRequest);
		
		// park
		RefillRequest refillRequest = new RefillRequest();
		AuthRequest authRequest = new AuthRequest();
		authRequest.setEmail("TestUser@PaymentAccount.test");
		authRequest.setPassword("TestPassword");
		refillRequest.setParkingReferenceNumber("14:111:1349923213");
		refillRequest.setUserInfo(authRequest);
		refillRequest.setUid(UNIT_TEST_USER_ID);
		refillRequest.setDurationMinutes(1);
		refillRequest.setSpotId(111);
		JAXBElement<RefillRequest> testRefillRequest = new JAXBElement<RefillRequest>(
				new QName("Test"), RefillRequest.class, refillRequest);
		RefillResponse response = parkResource.pilotRefillTime(testRefillRequest);
		System.out.println(response.getResp());
		
		// get the new score
		GetUserScoreRequest getUserScoreRequestNew = new GetUserScoreRequest();
		getUserScoreRequestNew.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> newScoreRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequestNew);
		GetUserScoreResponse newScoreResponse = parkResource.getUserScore(newScoreRequest);
		
		assertEquals(oldScoreResponse.getScore1() - 1, newScoreResponse.getScore1());
		assertEquals(oldScoreResponse.getScore2(), newScoreResponse.getScore2());
		assertEquals(oldScoreResponse.getScore3(), newScoreResponse.getScore3());
		assertEquals(oldScoreResponse.getUserId(), newScoreResponse.getUserId());
		
		System.out.println(newScoreResponse.getScore1());
	}
	
	public void _testGetUserScore() {
		GetUserScoreRequest getUserScoreRequest = new GetUserScoreRequest();
		getUserScoreRequest.setUserId(UNIT_TEST_USER_ID);
		
		JAXBElement<GetUserScoreRequest> testRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequest);
		
		GetUserScoreResponse response = parkResource.getUserScore(testRequest);
		System.out.println("Score1: " + response.getScore1());
		System.out.println("UserId: " + response.getUserId());
	}
	
	public void _testUpdateUserScore() {
		int newScore1 = (int)(Math.random() * 1000);
		int newScore2 = (int)(Math.random() * 1000);
		int newScore3 = (int)(Math.random() * 1000);
		
		UpdateUserScoreRequest updateRequest = new UpdateUserScoreRequest();
		updateRequest.setUserId(UNIT_TEST_USER_ID);
		updateRequest.setScore1(newScore1);
		updateRequest.setScore2(newScore2);
		updateRequest.setScore3(newScore3);
		JAXBElement<UpdateUserScoreRequest> jaxbUpdateRequest = new JAXBElement<UpdateUserScoreRequest>(
				new QName("Test"), UpdateUserScoreRequest.class, updateRequest);
		parkResource.updateUserScore(jaxbUpdateRequest);
		
		GetUserScoreRequest getUserScoreRequest = new GetUserScoreRequest();
		getUserScoreRequest.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> testRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequest);
		GetUserScoreResponse response = parkResource.getUserScore(testRequest);
		assertEquals(newScore1, response.getScore1());
		assertEquals(newScore2, response.getScore2());
		assertEquals(newScore3, response.getScore3());
		assertEquals(UNIT_TEST_USER_ID, response.getUserId());
	}
	
	public void _testAddUserReport(){
		GetUserScoreRequest getUserScoreRequest = new GetUserScoreRequest();
		getUserScoreRequest.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> testRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequest);
		GetUserScoreResponse oldScoreResponse = parkResource.getUserScore(testRequest);
		
		// add the report
		AddUserReportingRequest addReportRequest = new AddUserReportingRequest();
		addReportRequest.setScore1(5);
		addReportRequest.setScore2(5);
		addReportRequest.setScore3(5);
		addReportRequest.setScore4(5);
		addReportRequest.setScore5(5);
		addReportRequest.setScore6(5);
		addReportRequest.setUserId(UNIT_TEST_USER_ID);
		List<Long> spaceList = new ArrayList<Long>();
		spaceList.add(111L);
		spaceList.add(112L);
		spaceList.add(113L);
		spaceList.add(114L);
		spaceList.add(115L);
		spaceList.add(116L);
		addReportRequest.setSpaceIds(spaceList);
		JAXBElement<AddUserReportingRequest> testAddReportRequest = new JAXBElement<AddUserReportingRequest>(
				new QName("Test"), AddUserReportingRequest.class, addReportRequest);
		parkResource.addUserReporting(testAddReportRequest);

		// get the new score
		GetUserScoreRequest getUserScoreRequestNew = new GetUserScoreRequest();
		getUserScoreRequestNew.setUserId(UNIT_TEST_USER_ID);
		JAXBElement<GetUserScoreRequest> newScoreRequest = new JAXBElement<GetUserScoreRequest>(
				new QName("Test"), GetUserScoreRequest.class, getUserScoreRequestNew);
		GetUserScoreResponse newScoreResponse = parkResource.getUserScore(newScoreRequest);
		
		assertEquals(oldScoreResponse.getScore1() + 60, newScoreResponse.getScore1());
		assertEquals(oldScoreResponse.getScore2(), newScoreResponse.getScore2());
		assertEquals(oldScoreResponse.getScore3(), newScoreResponse.getScore3());
		assertEquals(oldScoreResponse.getUserId(), newScoreResponse.getUserId());
	}
	
}
