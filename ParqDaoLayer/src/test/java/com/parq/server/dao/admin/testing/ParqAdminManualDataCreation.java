package com.parq.server.dao.admin.testing;

import junit.framework.TestCase;

/**
 * @author GZ
 *
 */
public class ParqAdminManualDataCreation extends TestCase {

	private static final String clientName = "ParqTestClient";
	private static final String parkingLocationIdentifier = "ParqTestLocation_1";
	private static final double latitude = 5.23;
	private static final double logitude = -8.58;
	private static final String parkingSpaceIdentifier = "ParqTestSpace_1";
	private static final int parkingRateInCents = 100;
	private static final int parkingMinutesIncresments = 10;

	public void testCreateTestDataSqlScripts() {

		System.out.println("------------------------------------------------");
		
		ParqAdminDataCreationHelper helper = new ParqAdminDataCreationHelper();

		helper.createNewClient(clientName);
		helper.createNewParkingLocation(clientName, parkingLocationIdentifier);
		helper.setGeoCoordinateForParkingLocation(parkingLocationIdentifier,
				latitude, logitude);
		helper.insertParkingSpace(parkingLocationIdentifier,
				parkingSpaceIdentifier);
		helper.setParkingLocationRate(parkingRateInCents,
				parkingLocationIdentifier, parkingMinutesIncresments);
		
		
		
		System.out.println("------------------------------------------------");
	}

}






