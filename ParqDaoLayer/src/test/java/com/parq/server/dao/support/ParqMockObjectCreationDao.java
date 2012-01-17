package com.parq.server.dao.support;

/**
 * @author GZ
 *
 */
public class ParqMockObjectCreationDao extends DaoForTestingPurposes {
	
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
		"INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level) " + 
		" VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = ? ), ?, ?, ?)";

	private static final String sqlInsertLocationParkingRate = 
		"INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins) " +
		" VALUE ( ?, ?, (SELECT location_id FROM parkinglocation WHERE location_identifier = ? ), ?)";
	
	
	private static final String insertSpaceParkingRate = 
		"INSERT INTO parkingrate (parking_rate_cents, priority, location_id, space_id, time_increment_mins) " +
		" VALUE (?, ?, (SELECT location_id FROM parkinglocation WHERE location_identifier = ?), " +  
		" (SELECT space_id FROM parkingspace WHERE space_identifier = ?), ?)";

	private static final String createNewAdmin = 
		"INSERT INTO admin (email, password) VALUES (?, ?)";
	private static final String asscoiateAdminWithClient = 
		"INSERT INTO adminclientrelationship " +
		"	(admin_id, client_id, adminrole_id) VALUES(" +
		"	(SELECT admin_id FROM admin WHERE email = ?), " +
		"	(SELECT client_id FROM client WHERE name = ?), " +
		"	(SELECT adminrole_id FROM adminrole WHERE role_name = ?)) ";
	
	
	public boolean createNewClient(String clientName, String clientAddress,
			String clientDesc) {
		return executeSqlStatement(sqlInsertClientMain, new Object[] {
				clientName, clientAddress, clientDesc });
	}

	public boolean createNewParkingLocation(String clientName,
			String locationIdentifier, String locationName) {
		return executeSqlStatement(sqlInsertParkingLocationMain, new Object[] {
				clientName, locationIdentifier, locationName });
	}
	
	public boolean setGeoCoordinateForParkingLocation(
			String locationIdentifier, double latitude, double logitude) {
		return executeSqlStatement(sqlInsertGeoLocation, new Object[] {
				locationIdentifier, latitude, logitude });
	}

	public boolean insertParkingSpace(String locationIdentifier,
			String spaceIdentifier, String spaceName, String parkingLevel) {
		return executeSqlStatement(sqlInsertPaqkingSpace, new Object[] {
				locationIdentifier, spaceIdentifier, spaceName, parkingLevel});
	}

	public boolean setParkingLocationRate(int parkingRateInCents,
			int priority, String locationIdentifier, int parkingMinuteIncrement) {
		return executeSqlStatement(sqlInsertLocationParkingRate, new Object[] {
				parkingRateInCents, priority, locationIdentifier,
				parkingMinuteIncrement });
	}
	
	public boolean setParkingSpaceRate(int parkingRateInCents,
			int priority, String locationIdentifier, String spaceIdentifier, 
			int parkingMinuteIncrement) {
		return executeSqlStatement(insertSpaceParkingRate, new Object[] {
				parkingRateInCents, priority, locationIdentifier,
				spaceIdentifier, parkingMinuteIncrement });
	}
	
	public boolean createAdmin(String adminEmail, String adminPassword, String adminRoleName, String clientName) {
		boolean success = executeSqlStatement(createNewAdmin, 
				new Object[] {adminEmail, adminPassword});
		success = executeSqlStatement(asscoiateAdminWithClient, 
				new Object[] {adminEmail, clientName, adminRoleName}) && success;
		
		return success;
	}
}
