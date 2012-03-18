package com.parq.server.dao;

import java.util.List;

import junit.framework.TestCase;

import com.parq.server.dao.model.object.Client;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.PaymentMethod;
import com.parq.server.dao.support.SupportScriptForDaoTesting;

public class TestPaymentMethodDao extends TestCase {
	
	private PaymentMethodDao pmDao;
	private boolean testDataCreated = false;
	
	@Override
	protected void setUp() throws Exception {
		pmDao = new PaymentMethodDao();
		createTestData();
	}
	
	private void createTestData() {
		if (!testDataCreated){
			testDataCreated = true;
			SupportScriptForDaoTesting.insertMainTestDataSet();
		}
	}
	
	public void testCreateAdmin(){
		ClientDao clientDao = new ClientDao();
		Client testClient = clientDao.getClientByName(SupportScriptForDaoTesting.clientNameMain);
		List<ParkingLocation> parkingLocations = clientDao.getParkingLocationsAndSpacesByClientId(testClient.getId());
		ParkingSpace ps1 = parkingLocations.get(0).getSpaces().get(0);
		
		PaymentMethod pm1 = pmDao.getPaymentMethodBySpotId(ps1.getSpaceId());
		assertEquals(PaymentMethod.PREFILL, pm1);
	}
}
