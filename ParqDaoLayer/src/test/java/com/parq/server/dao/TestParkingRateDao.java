package com.parq.server.dao;

import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

import junit.framework.TestCase;

public class TestParkingRateDao extends TestCase {

	private ParkingRateDao parkingRateDao;

	@Override
	protected void setUp() throws Exception {
		parkingRateDao = new ParkingRateDao();
	}

	public void testGetParkingRateByName() {
		SupportScriptForDaoTesting.insertFakeData();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		assertNotNull(pRate);
		assertEquals(pRate.getLocationName(),
				SupportScriptForDaoTesting.parkingLocationNameMain);
		assertEquals(pRate.getSpaceName(),
				SupportScriptForDaoTesting.spaceNameMain);
		assertEquals(pRate.getRateType(), ParkingRate.RateType.Space);
		assertEquals(pRate.getParkingRateCents(),
				SupportScriptForDaoTesting.spaceRate);
		assertTrue(pRate.getTimeIncrementsMins() > 1);
	}

	public void testDifferentRates() {
		SupportScriptForDaoTesting.insertFakeData();

		ParkingRate spaceRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate buildingRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain, null);


		assertNotNull(spaceRateObj);
		assertNotNull(buildingRateObj);


		assertTrue(!(buildingRateObj.getParkingRateCents() == spaceRateObj
				.getParkingRateCents()));

		assertTrue(spaceRateObj.getParkingRateCents() == SupportScriptForDaoTesting.spaceRate);
		assertTrue(buildingRateObj.getParkingRateCents() == SupportScriptForDaoTesting.parkingLocationRate);
	}

	public void testCaching() {
		SupportScriptForDaoTesting.insertFakeData();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate pRate1 = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate pRate2 = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);

		assertNotNull(pRate);
		assertSame(pRate, pRate1);
		assertSame(pRate, pRate2);
		assertSame(pRate1, pRate2);
	}
}
