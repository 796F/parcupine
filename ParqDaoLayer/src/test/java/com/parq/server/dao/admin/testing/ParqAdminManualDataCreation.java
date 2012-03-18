package com.parq.server.dao.admin.testing;

import com.parq.server.dao.model.object.PaymentMethod;

import junit.framework.TestCase;

/**
 * @author GZ
 *
 */
public class ParqAdminManualDataCreation extends TestCase {

	private static final String clientName = "MIT_TEST";
	private static final String parkingLocationIdentifier = "main_lot";
	private static final String parkingLocationName = "Henry_Steinbrenner_Stadium";
	private static final double latitude = 42.358000;
	private static final double logitude = -71.097983;
	private static final String parkingSpaceIdentifier1 = "1410";
	private static final String parkingSpaceName1 = "MIT_TEST: space num 1";
	private static final String parkingSpaceIdentifier2 = "1411";
	private static final String parkingSpaceName2 = "MIT_TEST: space num 2";
	private static final String parkingSpaceIdentifier3 = "1412";
	private static final String parkingSpaceName3 = "MIT_TEST: space num 3";
	private static final int parkingRateInCents = 337;
	private static final int parkingMinutesIncresments = 10;
	
	private static final String clientAddress = "ParqTestAddress";
	private static final String clientDesc = "ParqTestClient";
	private static final String parkingSpaceLevel = "1";
	private static final int LOCATION_RATE_PRIORITY = -100;
	
	private static final String adminEmail = "MitTest@parqme.com";
	private static final String adminPassword = "password";
	
	private static final String adminRoleName = "admin";
	

	public void testCreateTestDataSqlScripts() {

		System.out.println("------------------------------------------------");
		System.out.println();
		
		ParqAdminDataCreationHelper helper = new ParqAdminDataCreationHelper();

		helper.createNewClient(clientName, clientAddress, clientDesc, PaymentMethod.PREFILL.name());
		helper.createNewParkingLocation(clientName, parkingLocationIdentifier, parkingLocationName);
		helper.setGeoCoordinateForParkingLocation(parkingLocationIdentifier,
				latitude, logitude);
		helper.insertParkingSpace(parkingLocationIdentifier,
				parkingSpaceIdentifier1, parkingSpaceName1, parkingSpaceLevel);
		helper.insertParkingSpace(parkingLocationIdentifier,
				parkingSpaceIdentifier2, parkingSpaceName2, parkingSpaceLevel);
		helper.insertParkingSpace(parkingLocationIdentifier,
				parkingSpaceIdentifier3, parkingSpaceName3, parkingSpaceLevel);
		helper.setParkingLocationRate(parkingRateInCents, LOCATION_RATE_PRIORITY,
				parkingLocationIdentifier, parkingMinutesIncresments);
		
		helper.createAdmin(adminEmail, adminPassword, adminRoleName, clientName);
		
		System.out.println();
		System.out.println("------------------------------------------------");
	}

}






