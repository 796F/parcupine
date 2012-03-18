package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.Grid;
import com.parq.server.dao.model.object.ParkingLocation;

/**
 * @author GZ
 *
 */
public class ParkingLocationDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "ParkingLocationDao";
	private static Cache myCache;
	
	private static final String boundingBoxCache = "getCloseByParkingLocation:";
	private static final String locationIdCache = "getLocationById:";
	
	private static final String sqlSelectLocationPiece = 
		"SELECT pl.location_id, pl.latitude, pl.longitude, pl.location_identifier, pl.client_id" +
		" FROM parkinglocation AS pl" +
		" WHERE pl.is_deleted IS NOT TRUE ";
	
	private static final String sqlGetParkingLocationByBoundingBox = 
		sqlSelectLocationPiece +
		" AND pl.latitude > ? " +
		" AND pl.latitude < ? " +
		" AND pl.longitude > ? " +
		" AND pl.longitude < ? ";
	
	private static final String sqlGetParkingLocationById =
		sqlSelectLocationPiece +
		" AND pl.location_id = ? ";
	
	private static final String sqlCreateLocation = 
		"INSERT INTO parkinglocation (location_identifier, client_id, grid_id, location_name, latitude, longitude) " +
		" VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String sqlGetParkingLocationByLocationIdentifier = 
		sqlSelectLocationPiece +
		" AND pl.location_identifier = ? ";
	
	private static final String sqlCreateGrid = 
		"INSERT INTO parkinggrid (center_latitude, center_longitude) " +
		" VALUES (?, ?)";
	
	private final String sqlDeleteGrid = 
		"UPDATE parkinggrid SET is_deleted = TRUE WHERE grid_id = ?";
	
	private final String sqlDeleteParkingLocation =
		"UPDATE parkinglocation SET is_deleted = TRUE WHERE location_id = ?";

	private final String sqlGetAllGrids = 
		"SELECT grid_id, center_latitude, center_longitude FROM parkinggrid " +
		" WHERE is_deleted IS NOT TRUE";
	
	private final String sqlGetGridById = 
		"SELECT grid_id, center_latitude, center_longitude FROM parkinggrid " +
		" WHERE is_deleted IS NOT TRUE" +
		" AND grid_id = ?";

	public ParkingLocationDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ParkingLocation> findCloseByParkingLocation(double latitudeMin,
			double latitudeMax, double longitudeMin, double longitudeMax) {

		// the cache key for this method call;
		StringBuilder cacheKeyBuilder = new StringBuilder(boundingBoxCache);
		cacheKeyBuilder.append(latitudeMin);
		cacheKeyBuilder.append(":");
		cacheKeyBuilder.append(latitudeMax);
		cacheKeyBuilder.append(":");
		cacheKeyBuilder.append(longitudeMin);
		cacheKeyBuilder.append(":");
		cacheKeyBuilder.append(longitudeMax);
		
		String cacheKey = cacheKeyBuilder.toString();
		
		List<ParkingLocation> geoLocations = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			geoLocations = (List<ParkingLocation>) cacheEntry.getValue();
			return geoLocations;
		}

		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingLocationByBoundingBox);
			pstmt.setDouble(1, latitudeMin);
			pstmt.setDouble(2, latitudeMax);
			pstmt.setDouble(3, longitudeMin);
			pstmt.setDouble(4, longitudeMax);
			ResultSet rs = pstmt.executeQuery();

			geoLocations = createParkingLocationList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, geoLocations));
		
		return geoLocations;
	}
	
	public ParkingLocation getLocationById(long locationId)
	{
		String cacheKey = locationIdCache + locationId;
		ParkingLocation result = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			result = (ParkingLocation) cacheEntry.getValue();
			return result;
		}
		
		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingLocationById);
			pstmt.setLong(1, locationId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs != null && rs.isBeforeFirst()) {
				rs.next();
				result = createParkingLocationModelObject(rs);
			}

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, result));
		
		return result;
	}

	private List<ParkingLocation> createParkingLocationList(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<ParkingLocation> geoLocationList = new ArrayList<ParkingLocation>();

		while (rs.next()) {
			geoLocationList.add(createParkingLocationModelObject(rs));
		}		
		return geoLocationList;
	}
	
	private ParkingLocation createParkingLocationModelObject(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		
		ParkingLocation parkingLocation = new ParkingLocation();
		parkingLocation.setLocationId(rs.getLong("location_id"));
		parkingLocation.setLocationIdentifier(rs.getString("location_identifier"));
		parkingLocation.setLatitude(rs.getDouble("latitude"));
		parkingLocation.setLongitude(rs.getDouble("longitude"));
		
		return parkingLocation;
	}
	
	/**
	 * manually clear out the cache
	 * @return
	 */
	public boolean clearParkingLocationCache() {
		myCache.removeAll();
		return true;
	}
	
	public boolean createLocation(String locationIdentifier, long clientId, 
			long gridId, String locationName, double latitude, double longitude) {
		if (locationIdentifier == null || locationIdentifier.isEmpty() 
				|| clientId <= 0 || gridId <= 0
				|| locationName == null || locationName.isEmpty()
				|| latitude == 0.00 || longitude == 0.00) {
			throw new IllegalStateException("Invalid parking location create request");
		}

		clearParkingLocationCache();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newLocationCreated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlCreateLocation);
			pstmt.setString(1, locationIdentifier);
			pstmt.setLong(2, clientId);
			pstmt.setLong(3, gridId);
			pstmt.setString(4, locationName);
			pstmt.setDouble(5, latitude);
			pstmt.setDouble(6, longitude);
			newLocationCreated = pstmt.executeUpdate() == 1;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return newLocationCreated;
	}
	
	public ParkingLocation getParkingLocationByLocationIdentifier(String identifer) {
		
		ParkingLocation result = null;
		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingLocationByLocationIdentifier);
			pstmt.setString(1, identifer);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs != null && rs.isBeforeFirst()) {
				rs.next();
				result = createParkingLocationModelObject(rs);
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
	
	public boolean createGrid(double latitude, double longitude) {
		if (latitude == 0.00 || longitude == 0.00) {
			throw new IllegalStateException("Invalid grid create request");
		}
		
		clearParkingLocationCache();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newGridCreated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlCreateGrid);
			pstmt.setDouble(1, latitude);
			pstmt.setDouble(2, longitude);
			newGridCreated = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
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
		return gridDeleted;
	}
	
	public List<Grid> getAllGrids() { 
		
		List<Grid> parkingGrids = null;
		
		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllGrids);
			ResultSet rs = pstmt.executeQuery();

			parkingGrids = createParkingGridList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return parkingGrids;
	}
	
	private List<Grid> createParkingGridList(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<Grid> gridList = new ArrayList<Grid>();
		while (rs.next()) {
			gridList.add(createGridModelObject(rs));
		}		
		return gridList;
	}

	private Grid createGridModelObject(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		Grid parkingGrid = new Grid();
		parkingGrid.setGridId(rs.getLong("grid_id"));
		parkingGrid.setCenterLatitude(rs.getDouble("center_latitude"));
		parkingGrid.setCenterLongitude(rs.getDouble("center_longitude"));
		return parkingGrid;
	}

	public Grid getGridById(long gridId) {
		
		Grid result = null;
		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetGridById);
			pstmt.setLong(1, gridId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs != null && rs.isBeforeFirst()) {
				rs.next();
				result = createGridModelObject(rs);
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
	
	public boolean deleteParkingLocation(long locationId) {
		if (locationId <= 0) {
			throw new IllegalStateException("Invalid grid delete request");
		}
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean locationDeleted = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlDeleteParkingLocation);
			pstmt.setLong(1, locationId);
			locationDeleted = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return locationDeleted;
	}
}
