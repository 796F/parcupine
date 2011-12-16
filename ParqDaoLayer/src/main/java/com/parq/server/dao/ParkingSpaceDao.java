package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.parq.server.dao.model.object.ParkingSpace;

public class ParkingSpaceDao extends AbstractParqDaoParent {

	private static final String sqlGetParkingSpaceBySpaceId =
		"SELECT space_id, space_identifier, location_id, parking_level " +
		" FROM parkingspace " +
		" WHERE space_id = ? "; 
	
	private static final String sqlGetParkingSpaceBySpaceIdentifier = 
		"SELECT space_id, space_identifier, location_id, parking_level " +
		" FROM parkingspace " +
		" WHERE space_identifier = ? "; 
	
	
	public ParkingSpace getParkingSpaceBySpaceIdentifier(String spaceIdentifier) {
		ParkingSpace space = null;		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingSpaceBySpaceIdentifier);
			pstmt.setString(1, spaceIdentifier);

			// System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			space = createParkingSpace(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return space;
	}
	
	public ParkingSpace getParkingSpaceBySpaceId(long spaceId) {
		ParkingSpace space = null;		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingSpaceBySpaceId);
			pstmt.setLong(1, spaceId);

			// System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			space = createParkingSpace(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return space;
	}

	private ParkingSpace createParkingSpace(ResultSet rs) throws SQLException {
		
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		rs.first();
		ParkingSpace curSpace = new ParkingSpace();
		curSpace.setLocationId(rs.getLong("location_id"));
		curSpace.setSpaceId(rs.getLong("space_id"));
		curSpace.setParkingLevel(rs.getString("parking_level"));
		curSpace.setSpaceIdentifier(rs.getString("space_identifier"));
		return curSpace;
	}
}
