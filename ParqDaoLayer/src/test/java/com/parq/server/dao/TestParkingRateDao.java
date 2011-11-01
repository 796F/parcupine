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
				SupportScriptForDaoTesting.clientNameMain,
				SupportScriptForDaoTesting.buildingNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		assertNotNull(pRate);
		assertEquals(pRate.getBuildingName(),
				SupportScriptForDaoTesting.buildingNameMain);
		assertEquals(pRate.getClientName(),
				SupportScriptForDaoTesting.clientNameMain);
		assertEquals(pRate.getSpaceName(),
				SupportScriptForDaoTesting.spaceNameMain);
		assertEquals(pRate.getRateType(), ParkingRate.RateType.Space);
		assertEquals(pRate.getParkingRate(),
				SupportScriptForDaoTesting.spaceRate);
	}

	public void testDifferentRates() {
		SupportScriptForDaoTesting.insertFakeData();

		ParkingRate spaceRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.clientNameMain,
				SupportScriptForDaoTesting.buildingNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate buildingRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.clientNameMain,
				SupportScriptForDaoTesting.buildingNameMain, null);
		ParkingRate clientRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.clientNameMain, null, null);

		assertNotNull(spaceRateObj);
		assertNotNull(buildingRateObj);
		assertNotNull(clientRateObj);

		assertTrue(!(spaceRateObj.getParkingRate() == buildingRateObj
				.getParkingRate()));
		assertTrue(!(buildingRateObj.getParkingRate() == clientRateObj
				.getParkingRate()));

		assertTrue(spaceRateObj.getParkingRate() == SupportScriptForDaoTesting.spaceRate);
		assertTrue(buildingRateObj.getParkingRate() == SupportScriptForDaoTesting.buildingRate);
		assertTrue(clientRateObj.getParkingRate() == SupportScriptForDaoTesting.clientRate);
	}

	public void testCaching() {
		SupportScriptForDaoTesting.insertFakeData();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.clientNameMain,
				SupportScriptForDaoTesting.buildingNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate pRate1 = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.clientNameMain,
				SupportScriptForDaoTesting.buildingNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate pRate2 = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.clientNameMain,
				SupportScriptForDaoTesting.buildingNameMain,
				SupportScriptForDaoTesting.spaceNameMain);

		assertNotNull(pRate);
		assertSame(pRate, pRate1);
		assertSame(pRate, pRate2);
		assertSame(pRate1, pRate2);
	}

	public void testCleanUp() {
		SupportScriptForDaoTesting.deleteFakeData();
	}
}
