package com.parq.server.dao.admin.testing;

import junit.framework.TestCase;

/**
 * @author GZ
 *
 */
public class ParqAdminManualDataCreation extends TestCase {

	private static final String clientName = "MIT_TEST";
	private static final String parkingLocationIdentifier = "stdm";
	private static final String parkingLocationName = "Henry_Steinbrenner_Stadium";
	private static final double latitude = 42.358000;
	private static final double logitude = -71.097983;
	private static final String parkingSpaceIdentifier = "MITtestSpace_11";
	private static final String parkingSpaceName = "MIT_TEST: space num 11";
	private static final int parkingRateInCents = 500;
	private static final int parkingMinutesIncresments = 60;
	
	private static final String clientAddress = "ParqTestAddress";
	private static final String clientDesc = "ParqTestClient";
	private static final String parkingSpaceLevel = "1";
	private static final int LOCATION_RATE_PRIORITY = -100;
	

	public void testCreateTestDataSqlScripts() {

		System.out.println("------------------------------------------------");
		
		ParqAdminDataCreationHelper helper = new ParqAdminDataCreationHelper();

		helper.createNewClient(clientName, clientAddress, clientDesc);
		helper.createNewParkingLocation(clientName, parkingLocationIdentifier, parkingLocationName);
		helper.setGeoCoordinateForParkingLocation(parkingLocationIdentifier,
				latitude, logitude);
		helper.insertParkingSpace(parkingLocationIdentifier,
				parkingSpaceIdentifier, parkingSpaceName, parkingSpaceLevel);
		helper.setParkingLocationRate(parkingRateInCents, LOCATION_RATE_PRIORITY,
				parkingLocationIdentifier, parkingMinutesIncresments);
		
		
		
		System.out.println("------------------------------------------------");
	}

}






