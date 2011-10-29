package com.parq.server.dao;

import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.support.SupportScriptForClientAndParkRateDaoTesting;

import junit.framework.TestCase;

public class TestParkingRateDao extends TestCase {

	private ParkingRateDao parkingRateDao;

	@Override
	protected void setUp() throws Exception {
		parkingRateDao = new ParkingRateDao();
	}

	public void testGetParkingRateByName() {
		SupportScriptForClientAndParkRateDaoTesting.insertFakeData();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain,
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain,
				SupportScriptForClientAndParkRateDaoTesting.spaceNameMain);
		assertNotNull(pRate);
		assertEquals(pRate.getBuildingName(),
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain);
		assertEquals(pRate.getClientName(),
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain);
		assertEquals(pRate.getSpaceName(),
				SupportScriptForClientAndParkRateDaoTesting.spaceNameMain);
		assertEquals(pRate.getRateType(), ParkingRate.RateType.Space);
		assertEquals(pRate.getParkingRate(),
				SupportScriptForClientAndParkRateDaoTesting.spaceRate);
	}

	public void testDifferentRates() {
		SupportScriptForClientAndParkRateDaoTesting.insertFakeData();

		ParkingRate spaceRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain,
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain,
				SupportScriptForClientAndParkRateDaoTesting.spaceNameMain);
		ParkingRate buildingRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain,
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain, null);
		ParkingRate clientRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain, null, null);

		assertNotNull(spaceRateObj);
		assertNotNull(buildingRateObj);
		assertNotNull(clientRateObj);

		assertTrue(!(spaceRateObj.getParkingRate() == buildingRateObj
				.getParkingRate()));
		assertTrue(!(buildingRateObj.getParkingRate() == clientRateObj
				.getParkingRate()));

		assertTrue(spaceRateObj.getParkingRate() == SupportScriptForClientAndParkRateDaoTesting.spaceRate);
		assertTrue(buildingRateObj.getParkingRate() == SupportScriptForClientAndParkRateDaoTesting.buildingRate);
		assertTrue(clientRateObj.getParkingRate() == SupportScriptForClientAndParkRateDaoTesting.clientRate);
	}

	public void testCaching() {
		SupportScriptForClientAndParkRateDaoTesting.insertFakeData();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain,
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain,
				SupportScriptForClientAndParkRateDaoTesting.spaceNameMain);
		ParkingRate pRate1 = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain,
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain,
				SupportScriptForClientAndParkRateDaoTesting.spaceNameMain);
		ParkingRate pRate2 = parkingRateDao.getParkingRateByName(
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain,
				SupportScriptForClientAndParkRateDaoTesting.buildingNameMain,
				SupportScriptForClientAndParkRateDaoTesting.spaceNameMain);

		assertNotNull(pRate);
		assertSame(pRate, pRate1);
		assertSame(pRate, pRate2);
		assertSame(pRate1, pRate2);
	}

	public void testCleanUp() {
		SupportScriptForClientAndParkRateDaoTesting.deleteFakeData();
	}
}
