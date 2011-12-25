package com.parq.server.dao.admin.testing;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.parq.server.dao.support.ParqMockObjectCreationDao;

/**
 * @author GZ
 *
 */
final class ParqAdminDataCreationHelper extends ParqMockObjectCreationDao {

	protected static final String CLIENT_ADDRESS = "ParqTestAddress";
	protected static final String CLIENT_DESCRIPTION = "ParqTestClient";
	protected static final String PARKING_LOCATION_NAME = "ParqTestingParkingLocation";
	protected static final String PARKING_SPACE_LEVEL = "1";
	protected static final int PARKING_RATE_INCREMENT = 5;
	protected static final int LOCATION_RATE_PRIORITY = -20;
	protected static final int PARKING_SPACE_RATE_PRIORITY = -100;
	
	// -------------------------------------------------------------------------------
	// public methods for use for manual data creation for testing purposes
	// -------------------------------------------------------------------------------

	public boolean createNewClient(String clientName) {
		return createNewClient(clientName, CLIENT_ADDRESS, CLIENT_DESCRIPTION);
	}

	public boolean createNewParkingLocation(String clientName,
			String locationIdentifier) {
		return createNewParkingLocation(clientName, locationIdentifier,
				PARKING_LOCATION_NAME);
	}

	public boolean setGeoCoordinateForParkingLocation(
			String locationIdentifier, double latitude, double logitude) {
		return setGeoLocationForParkingLocation(locationIdentifier, latitude,
				logitude);
	}

	public boolean insertParkingSpace(String locationIdentifier,
			String spaceIdentifier, String parkingSpaceName) {
		return insertParkingSpace(locationIdentifier, spaceIdentifier,
				PARKING_SPACE_LEVEL, parkingSpaceName);
	}

	public boolean setParkingLocationRate(int parkingRateInCents,
			String locationIdentifier, int parkingMinutesIncresments) {
		return setParkingLocationRate(parkingRateInCents,
				LOCATION_RATE_PRIORITY, locationIdentifier,
				parkingMinutesIncresments);
	}

	@Override
	public boolean executeSqlStatement(String sql, Object[] parms) {

		CallableStatement stmt = null;
		Connection con = null;
		try {
			con = getConnection();
			stmt = con.prepareCall(sql);
			// set the parameter of the sql statement
			if (parms != null) {
				for (int i = 0; i < parms.length; i++) {
					stmt.setObject(i + 1, parms[i]);
				}
			}

			String toStringStmt = stmt.toString();
			String[] toStringStmtParts = toStringStmt.split(": ");
			String sqlStringStmt = toStringStmt.substring(toStringStmtParts[0].length() + 2);
			System.out.println(sqlStringStmt + ";");

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + stmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		return false;
	}
}
