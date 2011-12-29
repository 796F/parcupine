package com.parq.server.dao;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

public class TestParkingSpaceDao extends TestCase {

	private ParkingSpaceDao parkingSpaceDao;

	@Override
	protected void setUp() throws Exception {
		parkingSpaceDao = new ParkingSpaceDao();
	}
	
	public void testGetParkingSpaceBySpaceIdentifier() {
		SupportScriptForDaoTesting.insertMainTestDataSet();
		
		ParkingSpace pSpace = parkingSpaceDao
				.getParkingSpaceBySpaceIdentifier(
						SupportScriptForDaoTesting.parkingLocationNameMain, SupportScriptForDaoTesting.spaceNameMain);
		assertNotNull(pSpace);
		assertTrue(pSpace.getSpaceId() > 0);
		assertTrue(pSpace.getLocationId() > 0);
		assertNotNull(pSpace.getSpaceIdentifier());
		assertNotNull(pSpace.getParkingLevel());
		assertNotNull(pSpace.getSpaceName());
	}
	
	public void testGetParkingSpaceBySpaceId(){
		SupportScriptForDaoTesting.insertMainTestDataSet();
		
		ParkingSpace pTemp = parkingSpaceDao
			.getParkingSpaceBySpaceIdentifier(
					SupportScriptForDaoTesting.parkingLocationNameMain, SupportScriptForDaoTesting.spaceNameMain);
		ParkingSpace pSpace = parkingSpaceDao
			.getParkingSpaceBySpaceId(pTemp.getSpaceId());
		
		assertNotNull(pSpace);
		assertTrue(pSpace.getSpaceId() > 0);
		assertTrue(pSpace.getLocationId() > 0);
		assertNotNull(pSpace.getSpaceIdentifier());
		assertNotNull(pSpace.getParkingLevel());
		assertNotNull(pSpace.getSpaceName());
	}
}
