package com.parq.server.dao;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

/**
 * @author GZ
 *
 */
public class TestParkingRateDao extends TestCase {

	private ParkingRateDao parkingRateDao;

	@Override
	protected void setUp() throws Exception {
		parkingRateDao = new ParkingRateDao();
	}

	public void testGetParkingRateByName() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain);
		assertNotNull(pRate);
		assertEquals(pRate.getLocationName(),
				SupportScriptForDaoTesting.parkingLocationNameMain);
		assertEquals(pRate.getParkingRateCents(),
				SupportScriptForDaoTesting.parkingLocationRate);
		assertTrue(pRate.getTimeIncrementsMins() > 1);
	}

	public void testDifferentRates() {
		SupportScriptForDaoTesting.insertMainTestDataSet();
		ParkingRate buildingRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain);

		assertNotNull(buildingRateObj);
		assertTrue(buildingRateObj.getParkingRateCents() == SupportScriptForDaoTesting.parkingLocationRate);
	}
	
	public void testGetParkingRateByRateId() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain);
		assertNotNull(pRate);
		
		
		ParkingRate pRate2 = parkingRateDao.getParkingRateByRateId(pRate.getRateId());
		assertNotNull(pRate2);
		assertEquals(pRate2.getParkingRateCents(),
				SupportScriptForDaoTesting.parkingLocationRate);
		assertTrue(pRate2.getTimeIncrementsMins() > 1);
	}

	public void testCaching() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain);
		ParkingRate pRate1 = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain);
		ParkingRate pRate2 = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain);

		assertNotNull(pRate);
		assertSame(pRate, pRate1);
		assertSame(pRate, pRate2);
		assertSame(pRate1, pRate2);
	}
}
