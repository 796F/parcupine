package com.parq.server.dao;

import java.util.List;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

/**
 * @author GZ
 *
 */
public class TestParkingLocationDao extends TestCase {

	private ParkingLocationDao geolocationDao;

	@Override
	protected void setUp() throws Exception {
		geolocationDao = new ParkingLocationDao();
	}

	public void testGetCloseByParkingLocation() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

		List<ParkingLocation> locationList = geolocationDao.findCloseByParkingLocation(-1, 1, -1, 1);
		assertNotNull(locationList);
		assertFalse(locationList.isEmpty());
		assertEquals(1, locationList.size());

		ParkingLocation locationFound = locationList.get(0);
		assertEquals(SupportScriptForDaoTesting.parkingLocationNameMain, locationFound.getLocationIdentifier());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLatitude, locationFound.getGeoPoints().get(0).getLatitude());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLongtitude, locationFound.getGeoPoints().get(0).getLongitude());
		assertEquals(SupportScriptForDaoTesting.fakeParkingLocationType, locationFound.getLocationType());
		
		assertEquals(1, locationFound.getGeoPoints().size());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLatitude, locationFound.getGeoPoints().get(0).getLatitude());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLongtitude, locationFound.getGeoPoints().get(0).getLongitude());
		
		assertTrue(locationFound.getLocationId() > 0);
	}
	
	public void testGetLocationById()
	{
		List<ParkingLocation> locationList = geolocationDao.findCloseByParkingLocation(-1, 1, -1, 1);
		assertNotNull(locationList);
		assertFalse(locationList.isEmpty());
		assertEquals(1, locationList.size());
		
		ParkingLocation locationById = geolocationDao.getLocationById(locationList.get(0).getLocationId());
		assertNotNull(locationById);
		assertEquals(locationById.getLocationIdentifier(), locationList.get(0).getLocationIdentifier());
		assertEquals(locationById.getGeoPoints().get(0).getLatitude(), locationList.get(0).getGeoPoints().get(0).getLatitude());
		assertEquals(locationById.getGeoPoints().get(0).getLongitude(), locationList.get(0).getGeoPoints().get(0).getLongitude());
		assertEquals(locationById.getLocationId(), locationList.get(0).getLocationId());
		
		assertEquals(1, locationList.get(0).getGeoPoints().size());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLatitude, locationList.get(0).getGeoPoints().get(0).getLatitude());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLongtitude, locationList.get(0).getGeoPoints().get(0).getLongitude());
	}
	
	public void testMultiGeoPointLocaiton() {
		List<ParkingLocation> locationList = geolocationDao.findCloseByParkingLocation(0.49, 0.51, 4.99, 5.01);
		assertNotNull(locationList);
		assertFalse(locationList.isEmpty());
		assertEquals(1, locationList.size());
		
		ParkingLocation loc = locationList.get(0);

		assertEquals(2, loc.getGeoPoints().size());
		assertEquals(SupportScriptForDaoTesting.fakeParkingLocation2Latitude, loc.getGeoPoints().get(0).getLatitude());
		assertEquals(SupportScriptForDaoTesting.fakeParkingLocation2Longtitude, loc.getGeoPoints().get(0).getLongitude());
		assertEquals(1, loc.getGeoPoints().get(0).getSortOrder());
		
		assertEquals(SupportScriptForDaoTesting.fakeParkingLocation1Latitude, loc.getGeoPoints().get(1).getLatitude());
		assertEquals(SupportScriptForDaoTesting.fakeParkingLocation1Longtitude, loc.getGeoPoints().get(1).getLongitude());
		assertEquals(2, loc.getGeoPoints().get(1).getSortOrder());
	}

	public void testCache() {
		for (int i = 0; i < 10000; i++) {
			List<ParkingLocation> locationList = geolocationDao
					.findCloseByParkingLocation(-0.85, 0.75, -0.65, 0.5);
			assertNotNull(locationList);
			assertFalse(locationList.isEmpty());
			assertEquals(1, locationList.size());
		}
	}
}
