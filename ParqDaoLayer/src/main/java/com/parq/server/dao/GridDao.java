package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.parq.server.dao.model.object.Grid;
import com.parq.server.dao.model.object.ParkingLocation;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.SimpleGrid;

public class GridDao extends AbstractParqDaoParent {

	private static final String sqlCreateGrid = "INSERT INTO parkinggrid (grid_name, latitude, longitude) "
			+ " VALUES (?, ?, ?)";

	private final String sqlDeleteGrid = "UPDATE parkinggrid SET is_deleted = TRUE WHERE grid_id = ?";

	private final String sqlGetAllSimpleGrids = "SELECT grid_id, latitude, longitude FROM parkinggrid "
			+ " WHERE is_deleted IS NOT TRUE";

	private final String sqlGetSimpleGridById = "SELECT grid_id, latitude, longitude FROM parkinggrid "
			+ " WHERE is_deleted IS NOT TRUE AND grid_id = ?";

	private final String sqlGetAllGrids = "SELECT g.grid_id, g.latitude, g.longitude, "
			+ " pl.location_id, pl.location_identifier, pl.location_type, pl.client_id, "
			+ " s.space_id, s.space_identifier, s.parking_level, s.space_name, s.latitude, s.longitude "
			+ " FROM parkinggrid g, parkinglocation AS pl, parkingspace s "
			+ " WHERE g.grid_id = pl.grid_id AND pl.location_id = s.location_id"
			+ " AND g.is_deleted IS NOT TRUE AND pl.is_deleted IS NOT TRUE AND s.is_deleted IS NOT TRUE"
			+ " ORDER BY g.grid_id,  pl.location_id";
	
	private final String sqlSearchForGridsByBoundingBox = 
		" SELECT g.grid_id, g.latitude, g.longitude" +
		" 	FROM parkinggrid AS g " +
		" 	WHERE g.latitude > ? " +
		" 	AND   g.latitude < ? " +
		" 	AND   g.longitude > ? " +
		" 	AND   g.longitude < ? " +
		"	AND   g.is_deleted IS NOT TRUE " +
		" 	ORDER BY latitude, longitude";

	public boolean createGrid(String gridIdentifier, double latitude,
			double longitude) {
		if (latitude == 0.00 || longitude == 0.00) {
			throw new IllegalStateException("Invalid grid create request");
		}

		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newGridCreated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlCreateGrid);
			pstmt.setString(1, gridIdentifier);
			pstmt.setDouble(2, latitude);
			pstmt.setDouble(3, longitude);
			newGridCreated = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		if (newGridCreated) {
			ParkingLocationDao.clearParkingLocationCache();
		}

		return newGridCreated;
	}

	public boolean deleteGrid(long gridId) {
		if (gridId <= 0) {
			throw new IllegalStateException("Invalid grid delete request");
		}

		PreparedStatement pstmt = null;
		Connection con = null;
		boolean gridDeleted = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlDeleteGrid);
			pstmt.setLong(1, gridId);
			gridDeleted = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		if (gridDeleted) {
			ParkingLocationDao.clearParkingLocationCache();
		}

		return gridDeleted;
	}

	public List<SimpleGrid> getAllSimpleGrids() {
		List<SimpleGrid> parkingGrids = null;

		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllSimpleGrids);
			ResultSet rs = pstmt.executeQuery();

			parkingGrids = createSimpleParkingGridList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return parkingGrids;
	}

	private List<SimpleGrid> createSimpleParkingGridList(ResultSet rs)
			throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<SimpleGrid> gridList = new ArrayList<SimpleGrid>();
		while (rs.next()) {
			gridList.add(createSimpleGridModelObject(rs));
		}
		return gridList;
	}

	private SimpleGrid createSimpleGridModelObject(ResultSet rs)
			throws SQLException {
		if (rs == null) {
			return null;
		}
		SimpleGrid parkingGrid = new SimpleGrid();
		parkingGrid.setGridId(rs.getLong("grid_id"));
		parkingGrid.setLatitude(rs.getDouble("latitude"));
		parkingGrid.setLongitude(rs.getDouble("longitude"));
		return parkingGrid;
	}

	public SimpleGrid getSimpleGridById(long gridId) {

		SimpleGrid result = null;
		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetSimpleGridById);
			pstmt.setLong(1, gridId);
			ResultSet rs = pstmt.executeQuery();

			if (rs != null && rs.isBeforeFirst()) {
				rs.next();
				result = createSimpleGridModelObject(rs);
			}
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return result;
	}

	/**
	 * Get the Complete grid list.
	 * @return
	 */
	public List<Grid> getAllCompleteGridHiarchy() {
		List<Grid> parkingGrids = null;

		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllGrids);
			ResultSet rs = pstmt.executeQuery();

			parkingGrids = createGridList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return parkingGrids;
	}

	private List<Grid> createGridList(ResultSet rs)
			throws SQLException {
		if (rs == null || rs.isAfterLast()) {
			return Collections.emptyList();
		}

		// start the row iteration
		rs.first();
		List<Grid> grids = new ArrayList<Grid>();

		// iterate the grid list
		while (!rs.isAfterLast()) {
			long currentGridId = rs.getLong("grid_id");

			Grid parkingGrid = new Grid();
			parkingGrid.setGridId(currentGridId);
			parkingGrid.setLatitude(rs.getDouble("g.latitude"));
			parkingGrid.setLongitude(rs.getDouble("g.longitude"));
			
			// add all the parking location to this gridId;
			parkingGrid.getParkingLocations().addAll(createLocations(currentGridId, rs));	
			grids.add(parkingGrid);
		}
		return grids;
	}
	
	private List<ParkingLocation> createLocations(long gridId, ResultSet rs) throws SQLException {
		if (rs == null || rs.isAfterLast()) {
			return Collections.emptyList();
		}
		
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		while (!rs.isAfterLast()) {
			ParkingLocation curLocation = new ParkingLocation();
			curLocation.setLocationId(rs.getLong("location_id"));
			curLocation.setLocationIdentifier(rs
					.getString("location_identifier"));
			curLocation.setLocationType(rs.getString("location_type"));
			curLocation.setClientId(rs.getLong("client_id"));
			// add the spaces to the parking location
			curLocation.getSpaces().addAll(createParkingSpaces(rs));
			locations.add(curLocation);
			
			if (rs.isAfterLast()
					|| gridId != rs.getLong("grid_id")) {
				break;
			}
		}
		return locations;
	}
	
	private List<ParkingSpace> createParkingSpaces(ResultSet rs)
			throws SQLException {
		if (rs == null || rs.isAfterLast()) {
			return Collections.emptyList();
		}

		List<ParkingSpace> spaces = new ArrayList<ParkingSpace>();
		while (!rs.isAfterLast()) {
			ParkingSpace curSpace = new ParkingSpace();
			curSpace.setLocationId(rs.getLong("location_id"));
			curSpace.setSpaceId(rs.getLong("space_id"));
			curSpace.setParkingLevel(rs.getString("parking_level"));
			curSpace.setSpaceIdentifier(rs.getString("space_identifier"));
			curSpace.setSpaceName(rs.getString("space_name"));
			curSpace.setLatitude(rs.getDouble("s.latitude"));
			curSpace.setLongitude(rs.getDouble("s.longitude"));
			// add the space to the parking location
			spaces.add(curSpace);

			rs.next();
			// check if we are still dealing with the same location on the new
			// row, if not, break out of the ParkingSpaceLoop
			if (rs.isAfterLast()
					|| curSpace.getLocationId() != rs.getLong("location_id")) {
				break;
			}
		}
		return spaces;
	}
	
	/**
	 * Search for Parking Grids inside the bounding box. Note the return type is a list of 
	 * <code>SimpleGrid</code>, which only contain a the grid id, latitude, longitude
	 * 
	 * @return if no parking grid are found inside the bounding box, a empty list is returned.
	 */
	public List<SimpleGrid> findSimpleGridNearBy(double latitudeMin,
			double longitudeMin, double latitudeMax, double longitudeMax) {
	
		List<SimpleGrid> simpleGrids = null;
		// query the DB for the parking grids
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlSearchForGridsByBoundingBox);
			pstmt.setDouble(1, latitudeMin);
			pstmt.setDouble(2, latitudeMax);
			pstmt.setDouble(3, longitudeMin);
			pstmt.setDouble(4, longitudeMax);
			ResultSet rs = pstmt.executeQuery();

			simpleGrids = createSimpleParkingGridList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		return simpleGrids;
	}
}
