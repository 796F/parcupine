package com.parq.server.dao;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.ParkingSpace;
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
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		assertNotNull(pRate);
		assertEquals(pRate.getLocationName(),
				SupportScriptForDaoTesting.parkingLocationNameMain);
		assertEquals(pRate.getSpaceName(),
				SupportScriptForDaoTesting.spaceNameMain);
		assertEquals(pRate.getRateType(), ParkingRate.RateType.SPACE);
		assertEquals(pRate.getParkingRateCents(),
				SupportScriptForDaoTesting.spaceRate);
		assertTrue(pRate.getTimeIncrementsMins() > 1);
	}

	public void testDifferentRates() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

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
	
	public void testGetParkingRateByRateId() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

		ParkingRate pRate = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		assertNotNull(pRate);
		
		
		ParkingRate pRate2 = parkingRateDao.getParkingRateByRateId(pRate.getRateId());
		assertNotNull(pRate2);
		assertEquals(pRate2.getRateType(), ParkingRate.RateType.CLIENT);
		assertEquals(pRate2.getParkingRateCents(),
				SupportScriptForDaoTesting.spaceRate);
		assertTrue(pRate2.getTimeIncrementsMins() > 1);
	}
	
	public void testGetParkingRateBySpaceId() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

		
		ParkingRate spaceRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain,
				SupportScriptForDaoTesting.spaceNameMain);
		ParkingRate buildingRateObj = parkingRateDao.getParkingRateByName(
				SupportScriptForDaoTesting.parkingLocationNameMain, null);
		
		// test get the parking rate by parking space id for rate specific to a parking space
		ParkingRate pRate = parkingRateDao.getParkingRateBySpaceId(spaceRateObj.getSpaceId());
		assertNotNull(pRate);
		assertEquals(pRate.getRateType(), ParkingRate.RateType.SPACE);
		assertEquals(pRate.getParkingRateCents(),
				SupportScriptForDaoTesting.spaceRate);
		assertEquals(pRate.getTimeIncrementsMins(), spaceRateObj.getTimeIncrementsMins());
		assertEquals(pRate.getRateId(), spaceRateObj.getRateId());

		// test get the parking rate by parking space id for rate specific to a location
		ParkingSpaceDao spaceDao = new ParkingSpaceDao();
		ParkingSpace nonRatedSpace = spaceDao.getParkingSpaceBySpaceIdentifier(
				SupportScriptForDaoTesting.parkingLocationNameMain, SupportScriptForDaoTesting.spaceNameMain2);
		ParkingRate pRate2 = parkingRateDao.getParkingRateBySpaceId(nonRatedSpace.getSpaceId());
		assertNotNull(pRate2);
		assertEquals(pRate2.getRateType(), ParkingRate.RateType.LOCATION);
		assertEquals(pRate2.getParkingRateCents(),
				SupportScriptForDaoTesting.parkingLocationRate);
		assertEquals(pRate2.getTimeIncrementsMins(), buildingRateObj.getTimeIncrementsMins());
		assertEquals(pRate2.getRateId(), buildingRateObj.getRateId());		
	}

	public void testCaching() {
		SupportScriptForDaoTesting.insertMainTestDataSet();

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
