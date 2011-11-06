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
	
	private static final String sqlGetParkingLocationByBoundingBox = 
		"SELECT GL.geolocation_id, GL.location_id, GL.latitude, GL.longitude, PL.location_name" +
		" FROM ParkingLocation AS PL, Geolocation AS GL " +
		" WHERE PL.location_id = GL.location_id" +
		" AND PL.is_deleted IS NOT TRUE " +
		" AND GL.latitude > ? " +
		" AND GL.latitude < ? " +
		" AND GL.longitude > ? " +
		" AND GL.longitude < ? ";

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

			geoLocations = createGeolocationObject(rs);

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

	private List<Geolocation> createGeolocationObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<Geolocation> geoLocationList = new ArrayList<Geolocation>();

		while (rs.next()) {
			Geolocation geoLocation = new Geolocation();
			geoLocation.setGeolocationId(rs.getInt("geolocation_id"));
			geoLocation.setLocationId(rs.getInt("location_id"));
			geoLocation.setLocationName(rs.getString("location_name"));
			geoLocation.setLatitude(rs.getDouble("latitude"));
			geoLocation.setLongitude(rs.getDouble("longitude"));
			
			geoLocationList.add(geoLocation);
		}		
		return geoLocationList;
	}
}
