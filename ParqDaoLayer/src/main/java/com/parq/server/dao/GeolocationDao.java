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

import com.parq.server.dao.model.object.Geolocation;

public class GeolocationDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "GeoLocationCache";
	private static Cache myCache;
	
	private static final String boundingBoxCache = "getCloseByParkingLocation:";
	private static final String locationIdCache = "getLocationById:";
	
	private static final String sqlGetParkingLocationByBoundingBox = 
		"SELECT gl.geolocation_id, gl.location_id, gl.latitude, gl.longitude, pl.location_identifier" +
		" FROM parkinglocation AS pl, geolocation AS gl " +
		" WHERE pl.location_id = gl.location_id" +
		" AND pl.is_deleted IS NOT TRUE " +
		" AND gl.latitude > ? " +
		" AND gl.latitude < ? " +
		" AND gl.longitude > ? " +
		" AND gl.longitude < ? ";
	
	private static final String sqlGetParkingLocationById =
		"SELECT gl.geolocation_id, gl.location_id, gl.latitude, gl.longitude, pl.location_identifier" +
		" FROM parkinglocation AS pl, geolocation AS gl " +
		" WHERE pl.location_id = gl.location_id" +
		" AND pl.is_deleted IS NOT TRUE " +
		" AND gl.location_id = ? ";

	public GeolocationDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Geolocation> findCloseByParkingLocation(double latitudeMin,
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
		
		List<Geolocation> geoLocations = null;
		if (myCache.get(cacheKey) != null) {
			geoLocations = (List<Geolocation>) myCache.get(cacheKey).getValue();
			return geoLocations;
		}

		// query the DB for the user object
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

			geoLocations = createGeolocationList(rs);

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
	
	public Geolocation getLocationById(int locationId)
	{
		String cacheKey = locationIdCache + locationId;
		Geolocation result = null;
		if (myCache.get(cacheKey) != null) {
			result = (Geolocation) myCache.get(cacheKey).getValue();
			return result;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingLocationById);
			pstmt.setInt(1, locationId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs != null && rs.isBeforeFirst()) {
				rs.next();
				result = createGeolocation(rs);
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

	private List<Geolocation> createGeolocationList(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<Geolocation> geoLocationList = new ArrayList<Geolocation>();

		while (rs.next()) {
			geoLocationList.add(createGeolocation(rs));
		}		
		return geoLocationList;
	}
	
	private Geolocation createGeolocation(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		
		Geolocation geoLocation = new Geolocation();
		geoLocation.setGeolocationId(rs.getInt("geolocation_id"));
		geoLocation.setLocationId(rs.getInt("location_id"));
		geoLocation.setLocationIdentifier(rs.getString("location_identifier"));
		geoLocation.setLatitude(rs.getDouble("latitude"));
		geoLocation.setLongitude(rs.getDouble("longitude"));
		
		return geoLocation;
	}
	
	/**
	 * manually clear out the cache
	 * @return
	 */
	public boolean clearGeolocationCache() {
		myCache.removeAll();
		return true;
	}
}
