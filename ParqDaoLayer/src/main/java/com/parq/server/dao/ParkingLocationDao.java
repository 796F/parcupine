package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.GeoPoint;
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
		"SELECT pl.grid_id, pl.location_id, pl.location_identifier, pl.client_id, pl.location_type, " +
		"		gp.latitude, gp.longitude, gp.sort_order " +
		" FROM parkinglocation AS pl, geopoint AS gp" +
		" WHERE pl.is_deleted IS NOT TRUE " +
		" AND pl.location_id = gp.location_id "; 
	
	private static final String sqlParkingLocationOrderBy = 
		" ORDER BY pl.location_id";
	
	private static final String sqlGetParkingLocationByBoundingBox = 
		sqlSelectLocationPiece + " AND pl.location_id IN ( " +
		" SELECT  gp2.location_id " +
		" 	FROM geopoint AS gp2 " +
		" 	WHERE gp2.latitude > ? " +
		" 	AND   gp2.latitude < ? " +
		" 	AND   gp2.longitude > ? " +
		" 	AND   gp2.longitude < ? )" +
		sqlParkingLocationOrderBy;
	
	private static final String sqlGetParkingLocationById =
		sqlSelectLocationPiece +
		" AND pl.location_id = ? " +
		sqlParkingLocationOrderBy;
	
	private static final String sqlCreateLocation = 
		"INSERT INTO parkinglocation (location_identifier, client_id, grid_id, location_name, location_type) " +
		" VALUES (?, ?, ?, ?, ?)";
	
	private final String sqlDeleteParkingLocation =
		"UPDATE parkinglocation SET is_deleted = TRUE WHERE location_id = ?";
	
	private static final String sqlGetParkingLocationByLocationIdentifier = 
		sqlSelectLocationPiece +
		" AND pl.location_identifier = ? " +
		sqlParkingLocationOrderBy;
	
	private final String sqlAddGeoPointForLocation = 
		"INSERT INTO geopoint (location_id, latitude, longitude, sort_order) " +
		" VALUES((SELECT MAX(location_id) FROM parkinglocation), ?, ?, ?)";
	
	private static final String sqlGetAllLocationsWithGeoPoints = 
		"SELECT pl.grid_id, pl.location_id, pl.location_identifier, pl.client_id, pl.location_type, " +
		"		gp.latitude, gp.longitude, gp.sort_order " +
		" FROM parkinglocation AS pl, geopoint AS gp" +
		" WHERE pl.is_deleted IS NOT TRUE " +
		" AND pl.location_id = gp.location_id " +
		sqlParkingLocationOrderBy;

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
		
		List<ParkingLocation> parkingLocations = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			parkingLocations = (List<ParkingLocation>) cacheEntry.getValue();
			return parkingLocations;
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

			parkingLocations = createParkingLocationModelObjectList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, parkingLocations));
		
		return parkingLocations;
	}
	


	public ParkingLocation getLocationById(long locationId) {
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
	
	
	private List<ParkingLocation> createParkingLocationModelObjectList(
			ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		
		List<ParkingLocation> parkingLocationList = new ArrayList<ParkingLocation>();
		long lastLocationId = -1;
		ParkingLocation parkingLocation = null;
		
		while (rs.next()) {
			long curLocationId = rs.getLong("location_id");
			if (lastLocationId != curLocationId) {
				// create the parking location
				parkingLocation = new ParkingLocation();
				parkingLocation.setLocationId(curLocationId);
				parkingLocation.setLocationIdentifier(rs.getString("location_identifier"));
				parkingLocation.setLocationType(rs.getString("location_type"));
				parkingLocation.setClientId(rs.getLong("client_id"));
				parkingLocation.setGridId(rs.getLong("grid_id"));
				
				lastLocationId = curLocationId;
				parkingLocationList.add(parkingLocation);
			}
			// set up the GeoPoint
			GeoPoint gp = new GeoPoint();
			gp.setLatitude(rs.getDouble("latitude"));
			gp.setLongitude(rs.getDouble("longitude"));
			gp.setSortOrder(rs.getInt("sort_order"));
			// add the geo point to the parking location
			parkingLocation.getGeoPoints().add(gp);
		}
		
		// sort the geo points
		GeoPointComparator geoPointComparator = new GeoPointComparator();
		for (ParkingLocation pl : parkingLocationList) {
			Collections.sort(pl.getGeoPoints(), geoPointComparator);
		}
		
		return parkingLocationList;
	}

	private ParkingLocation createParkingLocationModelObject(ResultSet rs) throws SQLException {
		List <ParkingLocation> parkingLocations = createParkingLocationModelObjectList(rs);
		if (parkingLocations.isEmpty()) {
			return null;
		}
		return parkingLocations.get(0);
	}
	
	/**
	 * manually clear out the cache
	 * @return
	 */
	public static boolean clearParkingLocationCache() {
		myCache.removeAll();
		return true;
	}
	
	public boolean createLocation(String locationIdentifier, long clientId, 
			long gridId, String locationName, String locationType,  List<GeoPoint> geoPoints) {
		if (locationIdentifier == null || locationIdentifier.isEmpty() 
				|| clientId <= 0 || gridId <= 0
				|| locationName == null || locationName.isEmpty()
				|| locationType == null || locationType.isEmpty()
				|| geoPoints == null || geoPoints.isEmpty()) {
			throw new IllegalStateException("Invalid parking location create request");
		}

		clearParkingLocationCache();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newLocationCreated = false;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlCreateLocation);
			pstmt.setString(1, locationIdentifier);
			pstmt.setLong(2, clientId);
			pstmt.setLong(3, gridId);
			pstmt.setString(4, locationName);
			pstmt.setString(5, locationType);
			newLocationCreated = pstmt.executeUpdate() == 1;
			
			for (int i = 0; i < geoPoints.size() && newLocationCreated; i++) {
				newLocationCreated = insertGeoPoints(con, geoPoints.get(i));
			}

			if (!newLocationCreated) {
				con.rollback();
			} else {
				con.commit();
			}
			
		} catch (SQLException sqle) {
			try {
				con.rollback();
			} catch (SQLException e) {}
			
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {}
			
			closeConnection(con);
		}
		return newLocationCreated;
	}
	
	private boolean insertGeoPoints(Connection con, GeoPoint geoPoint) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement(sqlAddGeoPointForLocation);
		pstmt.setDouble(1, geoPoint.getLatitude());
		pstmt.setDouble(2, geoPoint.getLongitude());
		pstmt.setInt(3, geoPoint.getSortOrder());
		return pstmt.executeUpdate() == 1;
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
	
	public List<ParkingLocation> getAllLocationWithGeoPoints() {
		// query the DB for the parking location
		PreparedStatement pstmt = null;
		Connection con = null;
		List<ParkingLocation> parkingLocations = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllLocationsWithGeoPoints);
			ResultSet rs = pstmt.executeQuery();
			parkingLocations = createParkingLocationModelObjectList(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return parkingLocations;
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

	private class GeoPointComparator implements Comparator<GeoPoint>{
		@Override
		public int compare(GeoPoint arg0, GeoPoint arg1) {
			return arg0.getSortOrder() - arg1.getSortOrder();
		}
		
	}
}
