package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.ParkingRate;

/**
 * @author GZ
 *
 */
public class ParkingRateDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "ParkingRateCache";
	private static Cache myCache;
	
	private static final String parkingRateByNamePrefix = "getParkingRateByName:";
	// private static final String parkingRateByRateIdPrefix = "getParkingRateByRateId:";
	private static final String parkingRateBySpaceIdPrefix = "getParkingRateBySpaceId:";
	
	private static final String sqlGetParkingLocationParkingRate = 
		"SELECT r.rate_id, r.location_id, r.parking_rate_cents, r.priority, r.time_increment_mins, r.max_park_mins, r.min_park_mins, l.location_identifier " +
			" FROM parkinglocation AS l, parkingrate AS r " +
			" WHERE r.location_id = l.location_id " +
			" AND l.location_identifier = ? " +
			" AND l.is_deleted IS NOT TRUE " +
			" ORDER BY r.priority DESC"; 
	
	private static final String sqlGetParkingRateByParkingRateId =
		"SELECT r.rate_id, r.location_id, r.parking_rate_cents, r.priority, r.time_increment_mins, r.max_park_mins, r.min_park_mins " +
		" FROM parkingrate AS r " +
		" WHERE r.rate_id = ?"; 
	
	private static final String sqlGetParkingRateByParkingSpaceId =
		"SELECT r.rate_id, r.location_id, r.parking_rate_cents, r.priority, r.time_increment_mins, r.max_park_mins, r.min_park_mins, l.location_identifier, s.space_identifier " +
		" FROM parkinglocation AS l, parkingspace AS s, parkingrate AS r " +
		" WHERE l.location_id = s.location_id " +
		" AND r.location_id = l.location_id " +
		" AND s.space_id = ? " +
		" AND l.is_deleted IS NOT TRUE " +
		" AND s.is_deleted IS NOT TRUE " +
		" ORDER BY r.priority DESC"; 
	
	public ParkingRateDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}

	/**
	 * This method will try to find the most specific (highest priority)
	 *  parking rate available based on the information give</br>
	 *  
	 *  - parking space rate is the highest priority. </br>
	 *  - parkingLocation rate is medium priority </br>
	 *  - client rate is the lowest priority </br>
	 * 
	 * @param clientName cannot be <code>null</code>
	 * @param parkingLocationName can be <code>null</code> if only want to retrieve parking rate by client
	 * @param spaceName can be <code>null</code> if only want to retrieve parking rate by client or by builidng
	 * @return <code>null</code> if no parking rate is found. 
	 */
	public ParkingRate getParkingRateByName(String locationIdentifier) {
		if (locationIdentifier == null || locationIdentifier.isEmpty()) {
			throw new IllegalStateException("locationIdentifier cannot be null");
		}
		
		// the cache key for this method call;
		String cacheKey = parkingRateByNamePrefix + locationIdentifier;
		
		ParkingRate rate = null;
		Element cacheEntry = myCache.get(cacheKey);
		if (cacheEntry  != null) {
			rate = (ParkingRate) cacheEntry.getValue();
			return rate;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			
			if (locationIdentifier == null || locationIdentifier.isEmpty()) {
				throw new IllegalStateException("locationIdentifier cannot be null");
			}
			
			pstmt = con.prepareStatement(sqlGetParkingLocationParkingRate);
			pstmt.setString(1, locationIdentifier);
			
			// System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			rate = createParkingRate(rs, true);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		// put result into cache
		myCache.put(new Element(cacheKey, rate));
		
		return rate;
	}

	/**
	 * Retrieve the parking rate object by rate id. If the Id is invalid, a <code>NULL</code> value is returned.
	 * @param rateId
	 * @return
	 */
	public ParkingRate getParkingRateByRateId(long rateId) {

		ParkingRate rate = null;		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingRateByParkingRateId);
			pstmt.setLong(1, rateId);

			// System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			rate = createParkingRate(rs, false);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return rate;
	}
	
	/**
	 * Get the parking rate by the parking space Id. If the parking spaceId is
	 * invalid, null will be return for the ParkingRate
	 * 
	 * @param spaceId
	 *            must be a valid parking space Id
	 * @return
	 */
	public ParkingRate getParkingRateBySpaceId(long spaceId) {
		
		// the cache key for this method call;
		String cacheKey = parkingRateBySpaceIdPrefix + spaceId;
		
		ParkingRate rate = null;
		Element cacheEntry = myCache.get(cacheKey);
		if (cacheEntry  != null) {
			rate = (ParkingRate) cacheEntry.getValue();
			return rate;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingRateByParkingSpaceId);
			pstmt.setLong(1, spaceId);

			// System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			rate = createParkingRate(rs, true);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, rate));
		
		return rate;
	}
	
	private ParkingRate createParkingRate(ResultSet rs, boolean setLocation) throws SQLException {
		
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		rs.first();

		long rateId = rs.getLong("rate_id");
		long parkingLocationId = rs.getLong("location_id");
		int rate = rs.getInt("parking_rate_cents");
		int timeIncrements = rs.getInt("time_increment_mins");
		int maxParkTime = rs.getInt("max_park_mins");
		int minParkTime = rs.getInt("min_park_mins");
		
		ParkingRate parkingRate = new ParkingRate();
		parkingRate.setRateId(rateId);
		parkingRate.setParkingRateCents(rate);
		parkingRate.setTimeIncrementsMins(timeIncrements);
		parkingRate.setMaxParkMins(maxParkTime);
		parkingRate.setMinParkMins(minParkTime);
		
		parkingRate.setLocationId(parkingLocationId);
		
		if (setLocation) {
			String locationName = rs.getString("location_identifier");
			parkingRate.setLocationName(locationName);
		}
		
		return parkingRate;
	}
	
	/**
	 * manually clear out the cache
	 * @return
	 */
	public boolean clearRateCache() {
		myCache.removeAll();
		return true;
	}
}
