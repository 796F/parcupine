package com.parq.server.dao;

import java.util.ArrayList;
import java.util.List;

import com.parq.server.dao.model.object.Building;
import com.parq.server.dao.model.object.Client;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.support.SupportScriptForClientAndParkRateDaoTesting;

import junit.framework.TestCase;

public class TestClientDao extends TestCase {

	private ClientDao clientDao;

	@Override
	protected void setUp() throws Exception {
		clientDao = new ClientDao();
	}

	public void testGetClientByName() {
		SupportScriptForClientAndParkRateDaoTesting.insertFakeData();
		Client client1 = clientDao
				.getClientByName(SupportScriptForClientAndParkRateDaoTesting.clientNameMain);
		assertNotNull(client1);

		assertEquals(client1.getName(),
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain);
		assertTrue(client1.getId() > 0);
		assertTrue(client1.getClientDescription().length() > 0);
		assertTrue(client1.getAddress().length() > 0);
	}

	public void testGetClientById() {
		SupportScriptForClientAndParkRateDaoTesting.insertFakeData();
		Client tempClient = clientDao
				.getClientByName(SupportScriptForClientAndParkRateDaoTesting.clientNameMain);
		assertNotNull(tempClient);

		Client client1 = clientDao.getClientById(tempClient.getId());
		assertNotNull(client1);
		assertEquals(client1.getName(),
				SupportScriptForClientAndParkRateDaoTesting.clientNameMain);
		assertTrue(client1.getId() > 0);
		assertTrue(client1.getClientDescription().length() > 0);
		assertTrue(client1.getAddress().length() > 0);

		assertEquals(client1.getId(), tempClient.getId());
		assertEquals(client1.getAddress(), tempClient.getAddress());
		assertEquals(client1.getClientDescription(), tempClient
				.getClientDescription());
		assertEquals(client1.getName(), tempClient.getName());
	}

	public void testGetBuildingsAndSpacesByClientId() {
		SupportScriptForClientAndParkRateDaoTesting.insertFakeData();
		Client tempClient = clientDao
				.getClientByName(SupportScriptForClientAndParkRateDaoTesting.clientNameMain);
		assertNotNull(tempClient);

		List<Building> buildings = clientDao
				.getBuildingsAndSpacesByClientId(tempClient.getId());
		assertNotNull(buildings);
		assertFalse(buildings.isEmpty());
		
		for (Building b : buildings) {
			assertNotNull(b);
			assertNotNull(b.getBuildingName());
			assertFalse(b.getBuildingName().isEmpty());
			assertNotNull(b.getSpaces());
			assertFalse(b.getSpaces().isEmpty());
			assertTrue(b.getBuildingId() > 0);
			assertTrue(b.getClientId() > 0);
			
			for (ParkingSpace s : b.getSpaces()){
				assertNotNull(s);
				assertEquals(s.getBuildingId(), b.getBuildingId());
				assertTrue(s.getSpaceId() > 0);
				assertNotNull(s.getParkingLevel());
				assertFalse(s.getParkingLevel().isEmpty());
				assertNotNull(s.getSpaceName());
				assertFalse(s.getSpaceName().isEmpty());
			}
		}
	}

	public void testCache() {

		String name = SupportScriptForClientAndParkRateDaoTesting.clientNameMain;
		int id = clientDao.getClientByName(name).getId();

		List<Object> tempHolder = new ArrayList<Object>();

		for (int i = 0; i < 10000; i++) {
			tempHolder.add(clientDao.getClientByName(name));
			tempHolder.add(clientDao.getClientById(id));
			tempHolder.add(clientDao.getBuildingsAndSpacesByClientId(id));
		}

		for (Object o : tempHolder) {
			assertNotNull(o);
		}

	}

	public void testCleanUp() {
		SupportScriptForClientAndParkRateDaoTesting.deleteFakeData();
	}
}
