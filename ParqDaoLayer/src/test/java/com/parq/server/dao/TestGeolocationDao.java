package com.parq.server.dao;

import java.util.List;

import com.parq.server.dao.model.object.Geolocation;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

import junit.framework.TestCase;

public class TestGeolocationDao extends TestCase {

	private GeolocationDao geolocationDao;

	@Override
	protected void setUp() throws Exception {
		geolocationDao = new GeolocationDao();
	}

	public void testGetCloseByParkingLocation() {
		SupportScriptForDaoTesting.insertFakeData();

		List<Geolocation> locationList = geolocationDao.findCloseByParkingLocation(-1, 1, -1, 1);
		assertNotNull(locationList);
		assertFalse(locationList.isEmpty());
		assertEquals(1, locationList.size());

		Geolocation locationFound = locationList.get(0);
		assertEquals(SupportScriptForDaoTesting.parkingLocationNameMain, locationFound.getLocationName());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLatitude, locationFound.getLatitude());
		assertEquals(SupportScriptForDaoTesting.parkingLocationMainLongtitude, locationFound.getLongitude());
		assertTrue(locationFound.getGeolocationId() > 0);
		assertTrue(locationFound.getLocationId() > 0);
	}

	public void testCache() {
		for (int i = 0; i < 10000; i++) {
			List<Geolocation> locationList = geolocationDao
					.findCloseByParkingLocation(-0.85, 0.75, -0.65, 0.5);
			assertNotNull(locationList);
			assertFalse(locationList.isEmpty());
			assertEquals(1, locationList.size());
		}
	}
}
