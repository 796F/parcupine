package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.parq.server.dao.model.object.ParkingSpace;

public class ParkingSpaceDao extends AbstractParqDaoParent {

	private static final String sqlGetParkingSpaceBySpaceId =
		"SELECT space_id, space_identifier, location_id, parking_level, space_name, segment, latitude, longitude " +
		" FROM parkingspace " +
		" WHERE space_id = ? " +
		" AND is_deleted IS NOT TRUE"; 
	
	private static final String sqlGetParkingSpaceBySpaceIdentifier = 
		"SELECT s.space_id, s.space_identifier, s.location_id, s.parking_level, s.space_name, s.segment, s.latitude, s.longitude " +
		" FROM parkingspace s, parkinglocation l " +
		" WHERE s.location_id = l.location_id " +
		" AND location_identifier = ? " +
		" AND space_identifier = ? " +
		" AND s.is_deleted IS NOT TRUE " +
		" AND l.is_deleted IS NOT TRUE "; 
	
	private static final String sqlInsertParkingSpace = 
		"INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude, segment) " + 
		" VALUE (?, ?, ?, ?, ?, ?, ?)";
	
	
	public ParkingSpace getParkingSpaceBySpaceIdentifier(String locationIdentifer, String spaceIdentifier) {
		ParkingSpace space = null;		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingSpaceBySpaceIdentifier);
			pstmt.setString(1, locationIdentifer);
			pstmt.setString(2, spaceIdentifier);

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
		curSpace.setSpaceName(rs.getString("space_name"));
		curSpace.setLatitude(rs.getDouble("latitude"));
		curSpace.setLongitude(rs.getDouble("longitude"));
		curSpace.setSegment(rs.getInt("segment"));
		return curSpace;
	}
	
	public boolean insertNewParkingSpace(ParkingSpace newSpace) {
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean insertSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlInsertParkingSpace);
			pstmt.setLong(1, newSpace.getLocationId());
			pstmt.setString(2, newSpace.getSpaceIdentifier());
			pstmt.setString(3, newSpace.getSpaceName());
			pstmt.setString(4, newSpace.getParkingLevel());
			pstmt.setDouble(5, newSpace.getLatitude());
			pstmt.setDouble(6, newSpace.getLongitude());
			pstmt.setInt(7, newSpace.getSegment());
			// System.out.println(pstmt);
			
			insertSuccessful = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return insertSuccessful;
	}
}
