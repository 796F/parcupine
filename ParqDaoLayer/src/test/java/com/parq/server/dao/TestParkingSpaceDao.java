package com.parq.server.dao;

import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.support.ParqUnitTestParent;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

public class TestParkingSpaceDao extends ParqUnitTestParent {

	private ParkingSpaceDao parkingSpaceDao;

	@Override
	protected void setUp() throws Exception {
		parkingSpaceDao = new ParkingSpaceDao();
	}
	
	public void testGetParkingSpaceBySpaceIdentifier() {
		SupportScriptForDaoTesting.insertFakeData();
		
		ParkingSpace pSpace = parkingSpaceDao
				.getParkingSpaceBySpaceIdentifier(SupportScriptForDaoTesting.spaceNameMain);
		assertNotNull(pSpace);
		assertTrue(pSpace.getSpaceId() > 0);
		assertTrue(pSpace.getLocationId() > 0);
		assertNotNull(pSpace.getSpaceIdentifier());
		assertNotNull(pSpace.getParkingLevel());
	}
	
	public void testGetParkingSpaceBySpaceId(){
		SupportScriptForDaoTesting.insertFakeData();
		
		ParkingSpace pTemp = parkingSpaceDao
			.getParkingSpaceBySpaceIdentifier(SupportScriptForDaoTesting.spaceNameMain);
		ParkingSpace pSpace = parkingSpaceDao
			.getParkingSpaceBySpaceId(pTemp.getSpaceId());
		
		assertNotNull(pSpace);
		assertTrue(pSpace.getSpaceId() > 0);
		assertTrue(pSpace.getLocationId() > 0);
		assertNotNull(pSpace.getSpaceIdentifier());
		assertNotNull(pSpace.getParkingLevel());		
	}
}
