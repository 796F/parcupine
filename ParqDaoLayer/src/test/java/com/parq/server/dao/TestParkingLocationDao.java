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
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLatitude, locationFound.getLatitude());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLongtitude, locationFound.getLongitude());
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
		assertEquals(locationById.getLatitude(), locationList.get(0).getLatitude());
		assertEquals(locationById.getLongitude(), locationList.get(0).getLongitude());
		assertEquals(locationById.getLocationId(), locationList.get(0).getLocationId());
		

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
