package parkservice.gridservice;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.parq.server.dao.model.object.Grid;
import com.parq.server.grid.model.object.GridWithFillRate;

import parkservice.gridservice.model.FindGridsByGPSCoordinateRequest;
import parkservice.gridservice.model.FindGridsByGPSCoordinateResponse;
import parkservice.gridservice.model.GpsCoordinate;
import parkservice.resources.ParkResource;
import junit.framework.TestCase;

public class TestGridServiceJSONAPI extends TestCase {

	public void testFindGridByGPSCoor() {
		GridWithFillRate gridWithFill = new GridWithFillRate(new Grid());
		assertNotNull(gridWithFill);
		
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
	}
}
