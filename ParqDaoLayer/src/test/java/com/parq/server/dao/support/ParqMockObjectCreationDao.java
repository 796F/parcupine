package com.parq.server.dao.support;

import com.parq.server.dao.ParkingLocationDao;
import com.parq.server.dao.ParkingSpaceDao;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingSpace;

/**
 * @author GZ
 *
 */
public class ParqMockObjectCreationDao extends DaoForTestingPurposes {
	
	private static final String sqlInsertClientMain = 
		"INSERT INTO client (name, address, client_desc, payment_method) VALUES (?, ?, ?, ?)";
	
	private static final String sqlInsertParkingLocationMain = 
		"INSERT INTO parkinglocation (client_id, location_identifier, location_name) " + 
		" VALUE ((SELECT client_id FROM client WHERE name= ? AND is_deleted IS NOT TRUE), ?, ?)" ;
	
	private static final String sqlInsertGeoLocation = 
		"INSERT INTO geolocation (location_id, latitude, longitude) " + 
		" VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = ? AND is_deleted IS NOT TRUE), " +
		" ?, ?)" ;
	
//	private static final String sqlInsertPaqkingSpace = 
//		"INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level) " + 
//		" VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = ? AND is_deleted IS NOT TRUE), ?, ?, ?)";

	private static final String sqlInsertLocationParkingRate = 
		"INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins) " +
		" VALUE ( ?, ?, (SELECT location_id FROM parkinglocation WHERE location_identifier = ? AND is_deleted IS NOT TRUE), ?)";
	
	
	private static final String insertSpaceParkingRate = 
		"INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins) " +
		" VALUE (?, ?, (SELECT location_id FROM parkinglocation WHERE location_identifier = ? AND is_deleted IS NOT TRUE), ?)";

	private static final String createNewAdmin = 
		"INSERT INTO admin (email, password) VALUES (?, ?)";
	private static final String asscoiateAdminWithClient = 
		"INSERT INTO adminclientrelationship " +
		"	(admin_id, client_id, adminrole_id) VALUES(" +
		"	(SELECT admin_id FROM admin WHERE email = ? AND is_deleted IS NOT TRUE), " +
		"	(SELECT client_id FROM client WHERE name = ? AND is_deleted IS NOT TRUE), " +
		"	(SELECT adminrole_id FROM adminrole WHERE role_name = ? AND is_deleted IS NOT TRUE)) ";
	
	
	public boolean createNewClient(String clientName, String clientAddress,
			String clientDesc, String paymentMethod) {
		return executeSqlStatement(sqlInsertClientMain, new Object[] {
				clientName, clientAddress, clientDesc, paymentMethod });
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
			String spaceIdentifier, String spaceName, String parkingLevel, 
			double latitude, double longitude, int segment) {
		
		ParkingSpace newSpaceRequest = new ParkingSpace();
		newSpaceRequest.setSpaceName(spaceName);
		newSpaceRequest.setSpaceIdentifier(spaceIdentifier);
		newSpaceRequest.setParkingLevel(parkingLevel);
		newSpaceRequest.setLatitude(latitude);
		newSpaceRequest.setLongitude(longitude);
		newSpaceRequest.setSegment(segment);
		
		ParkingLocation location = 
			new ParkingLocationDao().getParkingLocationByLocationIdentifier(locationIdentifier);
		if (location == null) {
			return false;
		}
		newSpaceRequest.setLocationId(location.getLocationId());
		
		return new ParkingSpaceDao().insertNewParkingSpace(newSpaceRequest);
	}

	public boolean setParkingLocationRate(int parkingRateInCents,
			int priority, String locationIdentifier, int parkingMinuteIncrement) {
		return executeSqlStatement(sqlInsertLocationParkingRate, new Object[] {
				parkingRateInCents, priority, locationIdentifier,
				parkingMinuteIncrement });
	}
	
	public boolean setParkingSpaceRate(int parkingRateInCents,
			int priority, String locationIdentifier, int parkingMinuteIncrement) {
		return executeSqlStatement(insertSpaceParkingRate, new Object[] {
				parkingRateInCents, priority, locationIdentifier, parkingMinuteIncrement });
	}
	
	public boolean createAdmin(String adminEmail, String adminPassword, String adminRoleName, String clientName) {
		boolean success = executeSqlStatement(createNewAdmin, 
				new Object[] {adminEmail, adminPassword});
		success = executeSqlStatement(asscoiateAdminWithClient, 
				new Object[] {adminEmail, clientName, adminRoleName}) && success;
		
		return success;
	}
}
