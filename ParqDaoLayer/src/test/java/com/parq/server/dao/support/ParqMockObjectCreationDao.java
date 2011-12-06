package com.parq.server.dao.support;

public class ParqMockObjectCreationDao extends DaoForTestingPurposes {
	
	private static final String CLIENT_ADDRESS = "ParqTestAddress";
	private static final String CLIENT_DESCRIPTION = "ParqTestClient";
	private static final String PARKING_LOCATION_NAME = "ParqTestingParkingLocation";
	private static final String PARKING_SPACE_LEVEL = "1";
	private static final int PARKING_RATE_INCREMENT = 5;
	private static final int LOCATION_RATE_PRIORITY = -20;
	// private static final int PARKING_SPACE_RATE_PRIORITY = -100;
	
	private static final String sqlInsertClientMain = 
		"INSERT INTO client (name, address, client_desc) VALUES (?, ?, ?)";
	
	private static final String sqlInsertParkingLocationMain = 
		"INSERT INTO parkinglocation (client_id, location_identifier, location_name) " + 
		" VALUE ((SELECT client_id FROM client WHERE name= ?), ?, ?)" ;
	
	private static final String sqlInsertGeoLocation = 
		"INSERT INTO geolocation (location_id, latitude, longitude) " + 
		" VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = ? ), " +
		" ?, ?)" ;
	
	private static final String sqlInsertPaqkingSpace = 
		"INSERT INTO parkingspace (location_id, space_identifier, parking_level) " + 
		" VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = ? ), ?, ?)";

	private static final String sqlInsertLocationParkingRate = 
		"INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins) " +
		" VALUE ( ?, ?, (SELECT location_id FROM parkinglocation WHERE location_identifier = ? ), ?)";
	
	
	private static final String insertSpaceParkingRate = 
		"INSERT INTO parkingrate (parking_rate_cents, priority, location_id, space_id, time_increment_mins) " +
		" VALUE (?, ?, (SELECT location_id FROM parkinglocation WHERE location_identifier = ?), " +  
		" (SELECT space_id FROM parkingspace WHERE space_identifier = ?), ?)";

	
	boolean createNewClient(String clientName, String clientAddress,
			String clientDesc) {
		return executeSqlStatement(sqlInsertClientMain, new Object[] {
				clientName, clientAddress, clientDesc });
	}

	boolean createNewParkingLocation(String clientName,
			String locationIdentifier, String locationName) {
		return executeSqlStatement(sqlInsertParkingLocationMain, new Object[] {
				clientName, locationIdentifier, locationName });
	}
	
	boolean setGeoLocationForParkingLocation(
			String locationIdentifier, double latitude, double logitude) {
		return executeSqlStatement(sqlInsertGeoLocation, new Object[] {
				locationIdentifier, latitude, logitude });
	}

	boolean insertParkingSpace(String locationIdentifier,
			String spaceIdentifier, String parkingLevel) {
		return executeSqlStatement(sqlInsertPaqkingSpace, new Object[] {
				locationIdentifier, spaceIdentifier, parkingLevel });
	}

	boolean setParkingLocationRate(int parkingRateInCents,
			int priority, String locationIdentifier, int parkingMinuteIncrement) {
		return executeSqlStatement(sqlInsertLocationParkingRate, new Object[] {
				parkingRateInCents, priority, locationIdentifier,
				parkingMinuteIncrement });
	}
	
	boolean setParkingSpaceRate(int parkingRateInCents,
			int priority, String locationIdentifier, String spaceIdentifier, 
			int parkingMinuteIncrement) {
		return executeSqlStatement(insertSpaceParkingRate, new Object[] {
				parkingRateInCents, priority, locationIdentifier,
				spaceIdentifier, parkingMinuteIncrement });
	}

	// -------------------------------------------------------------------------------
	// public methods for use for manual data creation for testing purposes
	// -------------------------------------------------------------------------------

	public boolean createNewClient(String clientName) {
		return createNewClient(clientName, CLIENT_ADDRESS, CLIENT_DESCRIPTION);
	}
	
	public boolean createNewParkingLocation(String clientName,
			String locationIdentifier) {
		return createNewParkingLocation(clientName, locationIdentifier, PARKING_LOCATION_NAME);
	}
	
	public boolean setGeoCoordinateForParkingLocation(
			String locationIdentifier, double latitude, double logitude) {
		return setGeoLocationForParkingLocation(locationIdentifier, latitude, logitude);
	}

	public boolean insertParkingSpace(String locationIdentifier,
			String spaceIdentifier) {
		return insertParkingSpace(locationIdentifier,
				spaceIdentifier, PARKING_SPACE_LEVEL);
	}

	public boolean setParkingLocationRate(int parkingRateInCents, String locationIdentifier) {
		return setParkingLocationRate(parkingRateInCents,
				LOCATION_RATE_PRIORITY, locationIdentifier, PARKING_RATE_INCREMENT);
	}
}
