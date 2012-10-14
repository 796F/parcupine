package parkservice.gridservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import junit.framework.TestCase;
import parkservice.gridservice.model.GetUpdatedSpotLevelInfoRequest;
import parkservice.gridservice.model.GetUpdatedSpotLevelInfoResponse;
import parkservice.gridservice.model.GpsCoordinate;
import parkservice.gridservice.model.SearchArea;
import parkservice.resources.ParkResource;

public class TestBug extends TestCase {
	
	public void testGetUpdatedSpotLevelInfoResponse() {
		// search for streets in gps coor
		ParkResource parkResource = new ParkResource();
		GpsCoordinate northEast = new GpsCoordinate();
		northEast.setLatitude(42.361415);
		northEast.setLongitude(-71.089321);
		GpsCoordinate southWest = new GpsCoordinate();
		southWest.setLatitude(42.353773);
		southWest.setLongitude(-71.100329);
		
		List<SearchArea> testSearchArea = new ArrayList<SearchArea>();
		testSearchArea.add(new SearchArea());
		testSearchArea.get(0).setNorthEastCorner(northEast);
		testSearchArea.get(0).setSouthWestCorner(southWest);
		
		GetUpdatedSpotLevelInfoRequest getStreetInfoRequest = new GetUpdatedSpotLevelInfoRequest();
		getStreetInfoRequest.setSearchArea(testSearchArea);
		
		JAXBElement<GetUpdatedSpotLevelInfoRequest> jaxbTestStreetRequest = new JAXBElement<GetUpdatedSpotLevelInfoRequest>(
				new QName("Test"), GetUpdatedSpotLevelInfoRequest.class, getStreetInfoRequest);
		
		List<GetUpdatedSpotLevelInfoResponse> responseStreet = parkResource.getUpdatedSpotLevelInfo(jaxbTestStreetRequest);
		assertNotNull(responseStreet);
		// assertFalse(responseStreet.length == 0);
		// assertNotNull(responseStreet[0].getParkingSpace());
		// assertFalse(responseStreet[0].getParkingSpace().size() == 0);
	}
}
