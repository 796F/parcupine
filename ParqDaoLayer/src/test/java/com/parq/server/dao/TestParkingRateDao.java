package com.parq.server.dao;

import com.parq.server.dao.model.object.ParkingRate;

import junit.framework.TestCase;

public class TestParkingRateDao extends TestCase {

	private ParkingRateDao parkingRateDao;

	private String clientName = "AU";
	private String buildingName = "main_lot";
	private String spaceName = "1412";

	@Override
	protected void setUp() throws Exception {
		parkingRateDao = new ParkingRateDao();
	}

	public void testGetParkingRateByName() {
		ParkingRate pRate = parkingRateDao.getParkingRateByName(clientName,
				buildingName, spaceName);
		assertNull(pRate);
	}
	
	public void testCaching() {
		ParkingRate pRate = parkingRateDao.getParkingRateByName(clientName,
				buildingName, spaceName);
		ParkingRate pRate1 = parkingRateDao.getParkingRateByName(clientName,
				buildingName, spaceName);
		ParkingRate pRate2 = parkingRateDao.getParkingRateByName(clientName,
				buildingName, spaceName);
		
		assertSame(pRate, pRate1);
		assertSame(pRate, pRate2);
		assertSame(pRate1, pRate2);
	}
}
