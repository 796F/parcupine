package parkservice.gridservice;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.grid.GridManagementService;

import parkservice.gridservice.model.FindGridsByGPSCoordinateRequest;
import parkservice.gridservice.model.FindGridsByGPSCoordinateResponse;
import parkservice.gridservice.model.GetStreetInfoRequest;
import parkservice.gridservice.model.GetStreetInfoResponse;
import parkservice.gridservice.model.GetUpdatedStreetInfoRequest;
import parkservice.gridservice.model.GetUpdatedStreetInfoResponse;
import parkservice.gridservice.model.GpsCoordinate;
import parkservice.gridservice.model.SearchForStreetsRequest;
import parkservice.gridservice.model.SearchForStreetsResponse;
import parkservice.resources.ParkResource;
import junit.framework.TestCase;

public class TestParkResourceGridServiceAPI extends TestCase {
	
	// TODO this field will likely change a lot
	private long testUserId = 14;
	
	
	public void testBasicFindGridByGPSCoor() {
		ParkResource parkResource = new ParkResource();
		
		FindGridsByGPSCoordinateRequest findGridByGPSCoordinateRequest = new FindGridsByGPSCoordinateRequest();
		
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		
		findGridByGPSCoordinateRequest.setLastUpdateTime(0);
		findGridByGPSCoordinateRequest.setTopLeftCorner(topLeft);
		findGridByGPSCoordinateRequest.setBottomRightCorner(bottomRight);
		
		JAXBElement<FindGridsByGPSCoordinateRequest> testRequest = new JAXBElement<FindGridsByGPSCoordinateRequest>(
				new QName("Test"), FindGridsByGPSCoordinateRequest.class, findGridByGPSCoordinateRequest);
		
		FindGridsByGPSCoordinateResponse[] response = parkResource.findGridByGPSCoor(testRequest);
		
		assertNotNull(response);
		assertTrue(response.length > 0);	
		
		// test for search with no result based on time stamp
		findGridByGPSCoordinateRequest.setLastUpdateTime(System.currentTimeMillis() + 10000);
		FindGridsByGPSCoordinateResponse[] response1 = parkResource.findGridByGPSCoor(testRequest);
		assertNotNull(response1);
		assertEquals(response1.length, 0);
		
		// test for search with no result from small search area
		topLeft.setLatitude(0.0);
		topLeft.setLongitude(0.0);
		bottomRight.setLatitude(0.001);
		bottomRight.setLongitude(0.001);
		findGridByGPSCoordinateRequest.setLastUpdateTime(0);
		FindGridsByGPSCoordinateResponse[] response2 = parkResource.findGridByGPSCoor(testRequest);
		assertNotNull(response2);
		assertEquals(response2.length, 0);
	}
	
	public void testBasicSearchForStreets() {

		ParkResource parkResource = new ParkResource();
		
		SearchForStreetsRequest searchForStreetsRequest = new SearchForStreetsRequest();
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		
		searchForStreetsRequest.setTopLeftCorner(topLeft);
		searchForStreetsRequest.setBottomRightCorner(bottomRight);
		JAXBElement<SearchForStreetsRequest> jaxbTestRequest = new JAXBElement<SearchForStreetsRequest>(
				new QName("Test"), SearchForStreetsRequest.class, searchForStreetsRequest);
		
		SearchForStreetsResponse[] response = parkResource.searchForStreets(jaxbTestRequest);
		assertNotNull(response);
		assertTrue(response.length > 0);
		
		// test for search with no result
		topLeft.setLatitude(0.0);
		topLeft.setLongitude(0.0);
		bottomRight.setLatitude(0.001);
		bottomRight.setLongitude(0.001);
		SearchForStreetsResponse[] response1 = parkResource.searchForStreets(jaxbTestRequest);
		assertNotNull(response1);
		assertEquals(response1.length, 0);
	}
	
	public void testBasicGetUpdatedStreetInfo() {
		ParkResource parkResource = new ParkResource();
		
		GetUpdatedStreetInfoRequest getUpdatedStreetInfoRequest = new GetUpdatedStreetInfoRequest();
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		
		getUpdatedStreetInfoRequest.setTopLeftCorner(topLeft);
		getUpdatedStreetInfoRequest.setBottomRightCorner(bottomRight);
		getUpdatedStreetInfoRequest.setLastUpdateTime(0);
		JAXBElement<GetUpdatedStreetInfoRequest> jaxbTestRequest = new JAXBElement<GetUpdatedStreetInfoRequest>(
				new QName("Test"), GetUpdatedStreetInfoRequest.class, getUpdatedStreetInfoRequest);
		
		GetUpdatedStreetInfoResponse[] response = parkResource.getUpdatedStreetInfo(jaxbTestRequest);
		assertNotNull(response);
		assertTrue(response.length > 0);
		
		// test for search with no result based on time stamp
		getUpdatedStreetInfoRequest.setLastUpdateTime(System.currentTimeMillis() + 10000);
		GetUpdatedStreetInfoResponse[] response1 = parkResource.getUpdatedStreetInfo(jaxbTestRequest);
		assertNotNull(response1);
		assertEquals(response1.length, 0);
		
		// test for search with no result from small search area
		topLeft.setLatitude(0.0);
		topLeft.setLongitude(0.0);
		bottomRight.setLatitude(0.001);
		bottomRight.setLongitude(0.001);
		getUpdatedStreetInfoRequest.setLastUpdateTime(0);
		GetUpdatedStreetInfoResponse[] response2 = parkResource.getUpdatedStreetInfo(jaxbTestRequest);
		assertNotNull(response2);
		assertEquals(response2.length, 0);
	}
	
	public void testBasicGetStreetInfo() {
		ParkResource parkResource = new ParkResource();
		
		GetStreetInfoRequest getStreetInfoRequest = new GetStreetInfoRequest();
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		
		getStreetInfoRequest.setTopLeftCorner(topLeft);
		getStreetInfoRequest.setBottomRightCorner(bottomRight);
		JAXBElement<GetStreetInfoRequest> jaxbTestRequest = new JAXBElement<GetStreetInfoRequest>(
				new QName("Test"), GetStreetInfoRequest.class, getStreetInfoRequest);
		
		GetStreetInfoResponse[] response = parkResource.getStreetInfo(jaxbTestRequest);
		assertNotNull(response);
		assertTrue(response.length > 0);
		assertTrue(response[0].getParkingSpace().get(0).getSpaceOrder() > -1);
		assertTrue(response[0].getParkingSpace().get(0).getSpaceOrder() < 100);
		
		// test for search with no result
		topLeft.setLatitude(0.0);
		topLeft.setLongitude(0.0);
		bottomRight.setLatitude(0.001);
		bottomRight.setLongitude(0.001);
		GetStreetInfoResponse[] response1 = parkResource.getStreetInfo(jaxbTestRequest);
		assertNotNull(response1);
		assertEquals(response1.length, 0);
	}
	
	public void testConfinedSearchForGridAndStreet() {
		// get the test grid
		ParkResource parkResource = new ParkResource();
		FindGridsByGPSCoordinateRequest findGridByGPSCoordinateRequest = new FindGridsByGPSCoordinateRequest();
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		findGridByGPSCoordinateRequest.setLastUpdateTime(0);
		findGridByGPSCoordinateRequest.setTopLeftCorner(topLeft);
		findGridByGPSCoordinateRequest.setBottomRightCorner(bottomRight);
		JAXBElement<FindGridsByGPSCoordinateRequest> testRequest = new JAXBElement<FindGridsByGPSCoordinateRequest>(
				new QName("Test"), FindGridsByGPSCoordinateRequest.class, findGridByGPSCoordinateRequest);
		
		FindGridsByGPSCoordinateResponse[] responseGrid = parkResource.findGridByGPSCoor(testRequest);
		
		
		long testGridId = responseGrid[0].getGridId();
		double gridLat = responseGrid[0].getLatitude();
		double gridLong = responseGrid[0].getLongitude();
		double curFillRate = responseGrid[0].getFillRate();
		
		// test that the initial value is correct
		assertEquals(0.0, curFillRate);
		
		// do a confined grid search
		topLeft.setLatitude(gridLat - 0.001);
		topLeft.setLongitude(gridLong - 0.001);
		bottomRight.setLatitude(gridLat + 0.001);
		bottomRight.setLongitude(gridLong + 0.001);
		
		responseGrid = parkResource.findGridByGPSCoor(testRequest);
		assertNotNull(responseGrid);
		assertEquals(testGridId, responseGrid[0].getGridId());
		
		// search for streets in gps coor
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		SearchForStreetsRequest searchForStreetsRequest = new SearchForStreetsRequest();
		searchForStreetsRequest.setTopLeftCorner(topLeft);
		searchForStreetsRequest.setBottomRightCorner(bottomRight);
	
		JAXBElement<SearchForStreetsRequest> jaxbTestRequest = new JAXBElement<SearchForStreetsRequest>(
				new QName("Test"), SearchForStreetsRequest.class, searchForStreetsRequest);
		SearchForStreetsResponse[] responseStreet = parkResource.searchForStreets(jaxbTestRequest);
		assertNotNull(responseStreet);
		assertTrue(responseStreet.length > 0);
		
		long testStreetId = responseStreet[0].getStreetId();
		double streetLat = responseStreet[0].getGpsCoor().get(0).getLatitude();
		double streetLong = responseStreet[0].getGpsCoor().get(0).getLongitude();
		double streetFillRate = responseStreet[0].getFillRate();
		
		// test that the initial value is correct
		assertEquals(0.0, streetFillRate);
		
		// do a confined street search
		topLeft.setLatitude(streetLat - 0.0001);
		topLeft.setLongitude(streetLong - 0.0001);
		bottomRight.setLatitude(6);
		bottomRight.setLongitude(6);
		responseStreet = parkResource.searchForStreets(jaxbTestRequest);
		assertNotNull(responseStreet);
		assertTrue(responseStreet.length >= 1);
		assertEquals(testStreetId, responseStreet[0].getStreetId());
	}
	
	public void testFillRateUpdate() {
		// search for streets in gps coor
		ParkResource parkResource = new ParkResource();
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		GetStreetInfoRequest getStreetInfoRequest = new GetStreetInfoRequest();
		getStreetInfoRequest.setTopLeftCorner(topLeft);
		getStreetInfoRequest.setBottomRightCorner(bottomRight);
	
		JAXBElement<GetStreetInfoRequest> jaxbTestRequest = new JAXBElement<GetStreetInfoRequest>(
				new QName("Test"), GetStreetInfoRequest.class, getStreetInfoRequest);
		GetStreetInfoResponse[] responseStreet = parkResource.getStreetInfo(jaxbTestRequest);
		assertNotNull(responseStreet);
		
		// park at the first street's first space for 5 second
		long spaceToParkAtId = responseStreet[0].getParkingSpace().get(0).getSpaceId();
		GridManagementService gms = GridManagementService.getInstance();
		gms.park(spaceToParkAtId, new Date(System.currentTimeMillis() + 5000));
		
		responseStreet = parkResource.getStreetInfo(jaxbTestRequest);
		assertTrue(responseStreet[0].getFillRate() > 0.01);
		
		// make sure the grid data also is updated
		FindGridsByGPSCoordinateRequest findGridByGPSCoordinateRequest = new FindGridsByGPSCoordinateRequest();
		findGridByGPSCoordinateRequest.setLastUpdateTime(0);
		findGridByGPSCoordinateRequest.setTopLeftCorner(topLeft);
		findGridByGPSCoordinateRequest.setBottomRightCorner(bottomRight);
		JAXBElement<FindGridsByGPSCoordinateRequest> testRequest = new JAXBElement<FindGridsByGPSCoordinateRequest>(
				new QName("Test"), FindGridsByGPSCoordinateRequest.class, findGridByGPSCoordinateRequest);
		
		FindGridsByGPSCoordinateResponse[] responseGrid = parkResource.findGridByGPSCoor(testRequest);
		assertTrue(responseGrid[0].getFillRate() > 0.01);
		
		// wait 32 second to see if the status are updated correctly
		try {
			Thread.sleep(32000);
		} catch (InterruptedException ie) {}
		
		responseStreet = parkResource.getStreetInfo(jaxbTestRequest);
		assertEquals(responseStreet[0].getFillRate(), 0.0);
		
		responseGrid = parkResource.findGridByGPSCoor(testRequest);
		assertEquals(responseGrid[0].getFillRate(), 0.0);
	}
	
	public void testFullParkingScenrio() {
		// search for streets in gps coor
		ParkResource parkResource = new ParkResource();
		GpsCoordinate topLeft = new GpsCoordinate();
		topLeft.setLatitude(-180);
		topLeft.setLongitude(-180);
		GpsCoordinate bottomRight = new GpsCoordinate();
		bottomRight.setLatitude(180);
		bottomRight.setLongitude(180);
		
		GetStreetInfoRequest getStreetInfoRequest = new GetStreetInfoRequest();
		getStreetInfoRequest.setTopLeftCorner(topLeft);
		getStreetInfoRequest.setBottomRightCorner(bottomRight);
		JAXBElement<GetStreetInfoRequest> jaxbTestStreetRequest = new JAXBElement<GetStreetInfoRequest>(
				new QName("Test"), GetStreetInfoRequest.class, getStreetInfoRequest);
		
		FindGridsByGPSCoordinateRequest findGridByGPSCoordinateRequest = new FindGridsByGPSCoordinateRequest();
		findGridByGPSCoordinateRequest.setLastUpdateTime(0);
		findGridByGPSCoordinateRequest.setTopLeftCorner(topLeft);
		findGridByGPSCoordinateRequest.setBottomRightCorner(bottomRight);
		JAXBElement<FindGridsByGPSCoordinateRequest> jaxbTestGridRequest = new JAXBElement<FindGridsByGPSCoordinateRequest>(
				new QName("Test"), FindGridsByGPSCoordinateRequest.class, findGridByGPSCoordinateRequest);
		
		FindGridsByGPSCoordinateResponse[] responseGrid = parkResource.findGridByGPSCoor(jaxbTestGridRequest);
		GetStreetInfoResponse[] responseStreet = parkResource.getStreetInfo(jaxbTestStreetRequest);
		long spaceToParkAtId = responseStreet[0].getParkingSpace().get(0).getSpaceId();
		//long streetToParkAtId = responseStreet[0].getStreetId();
		//long gridToParkAtId = responseGrid[0].getGridId();
		
		responseStreet = parkResource.getStreetInfo(jaxbTestStreetRequest);
		assertEquals(responseStreet[0].getFillRate(), 0.0);
		assertEquals(responseGrid[0].getFillRate(), 0.0);
		
		// park for 5 seconds
		ParkingInstance pi = new ParkingInstance();
		pi.setPaidParking(true);
		pi.setParkingBeganTime(new Date(System.currentTimeMillis()));
		pi.setParkingEndTime(new Date(System.currentTimeMillis() + 5000));
		pi.setSpaceId(spaceToParkAtId);
		pi.setUserId(testUserId);
		// set the payment type;
		Payment payment = new Payment();
		payment.setAmountPaidCents(15);
		payment.setPaymentRefNumber("Test");
		payment.setPaymentDateTime(new Date(System.currentTimeMillis()));
		payment.setPaymentType(PaymentType.PrePaid);
		payment.setAccountId(-1);
		pi.setPaymentInfo(payment);
		
		// park with the parking status dao
		ParkingStatusDao psDao = new ParkingStatusDao();
		psDao.addNewParkingAndPayment(pi);
		
		// make sure the grid and street data are updated
		responseGrid = parkResource.findGridByGPSCoor(jaxbTestGridRequest);
		assertTrue(responseGrid[0].getFillRate() > 0.01);
		responseStreet = parkResource.getStreetInfo(jaxbTestStreetRequest);
		assertTrue(responseStreet[0].getFillRate() > 0.01);
		
		// wait 32 second to see if the status are updated correctly
		try {
			Thread.sleep(32000);
		} catch (InterruptedException ie) {}
		responseStreet = parkResource.getStreetInfo(jaxbTestStreetRequest);
		assertEquals(responseStreet[0].getFillRate(), 0.0);
		responseGrid = parkResource.findGridByGPSCoor(jaxbTestGridRequest);
		assertEquals(responseGrid[0].getFillRate(), 0.0);
		
		// park for 500 second
		pi.setParkingEndTime(new Date(System.currentTimeMillis() + 500000));
		psDao.addNewParkingAndPayment(pi);
		
		// make sure the grid and street data are updated
		responseGrid = parkResource.findGridByGPSCoor(jaxbTestGridRequest);
		assertTrue(responseGrid[0].getFillRate() > 0.01);
		responseStreet = parkResource.getStreetInfo(jaxbTestStreetRequest);
		assertTrue(responseStreet[0].getFillRate() > 0.01);
		
		// wait 2 second then unpark
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {}
		
		// unpark
		List<ParkingInstance> piLists = psDao.getParkingStatusBySpaceIds(new long[]{spaceToParkAtId});
		psDao.unparkBySpaceIdAndParkingRefNum(spaceToParkAtId, 
				piLists.get(0).getParkingRefNumber(), new Date(System.currentTimeMillis()));
		
		responseStreet = parkResource.getStreetInfo(jaxbTestStreetRequest);
		assertEquals(responseStreet[0].getFillRate(), 0.0);
		responseGrid = parkResource.findGridByGPSCoor(jaxbTestGridRequest);
		assertEquals(responseGrid[0].getFillRate(), 0.0);
		
	}
	
	
	public void _testGridServiceUpdateThreadIsRunning() {
		try {
			Thread.sleep(61000);
		} catch (InterruptedException ie) {}
		// expecting to see 2 system.out.println messages
	}
	
}
