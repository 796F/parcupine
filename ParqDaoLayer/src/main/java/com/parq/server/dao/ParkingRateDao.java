package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.ParkingRate.RateType;

public class ParkingRateDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "ParkingRateCache";
	private static Cache myCache;
	
	private static final String parkingRateByNamePrefix = "getParkingRateByName:";

	private static final String sqlGetSpaceParkingRate = 
		"SELECT r.location_id, r.space_id, r.parking_rate_cents, r.priority, r.time_increment_mins, r.max_park_mins, r.min_park_mins " +
			" FROM parkinglocation AS b, parkingspace AS s, parkingrate AS r " +
			" WHERE b.location_id = s.location_id " +
			" AND r.location_id = b.location_id " +
			" AND r.space_id IN(NULL, s.space_id) " +
			" AND b.location_identifier = ? " +
			" AND s.space_identifier = ? " +
			" AND b.is_deleted IS NOT TRUE " +
			" AND s.is_deleted IS NOT TRUE " +
			" ORDER BY r.priority DESC"; 
	
	private static final String sqlGetParkingLocationParkingRate = 
		"SELECT r.location_id, r.space_id, r.parking_rate_cents, r.priority, r.time_increment_mins, r.max_park_mins, r.min_park_mins " +
			" FROM parkinglocation AS b, parkingrate AS r " +
			" WHERE r.location_id = b.location_id " +
			" AND b.location_identifier = ? " +
			" AND b.is_deleted IS NOT TRUE " +
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
	public ParkingRate getParkingRateByName(String locationIdentifier, String spaceIdentifier) {
		
		// the cache key for this method call;
		String cacheKey = parkingRateByNamePrefix + locationIdentifier + ":" + spaceIdentifier;
		
		ParkingRate rate = null;
		if (myCache.get(cacheKey) != null) {
			rate = (ParkingRate) myCache.get(cacheKey).getValue();
			return rate;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			
			String stmtToUse = sqlGetParkingLocationParkingRate;
			if (locationIdentifier == null || locationIdentifier.isEmpty()) {
				throw new IllegalStateException("locationIdentifier cannot be null");
			}
			if (spaceIdentifier != null && !spaceIdentifier.isEmpty()) {
				stmtToUse = sqlGetSpaceParkingRate;
			}
			
			pstmt = con.prepareStatement(stmtToUse);
			pstmt.setString(1, locationIdentifier);
			
			if (spaceIdentifier != null && !spaceIdentifier.isEmpty()) {
				pstmt.setString(2, spaceIdentifier);
			}
			
			// System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			rate = createParkingRate(locationIdentifier, spaceIdentifier, rs);

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

	private ParkingRate createParkingRate(String parkingLocationName, 
			String spaceName, ResultSet rs) throws SQLException {
		
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		rs.first();

		int parkingLocationId = rs.getInt("location_id");
		int spaceId = rs.getInt("space_id");
		int rate = rs.getInt("parking_rate_cents");
		int timeIncrements = rs.getInt("time_increment_mins");
		int maxParkTime = rs.getInt("max_park_mins");
		int minParkTime = rs.getInt("min_park_mins");
		
		ParkingRate parkingRate = new ParkingRate();
		parkingRate.setRateType(RateType.Client);
		parkingRate.setParkingRateCents(rate);
		parkingRate.setTimeIncrementsMins(timeIncrements);
		parkingRate.setMaxParkMins(maxParkTime);
		parkingRate.setMinParkMins(minParkTime);
		
		if (parkingLocationId > 0) {
			parkingRate.setRateType(RateType.ParkingLocation);
			parkingRate.setLocationId(parkingLocationId);
			parkingRate.setLocationName(parkingLocationName);
		}
		if (spaceId > 0) {
			parkingRate.setRateType(RateType.Space);
			parkingRate.setSpaceId(spaceId);
			parkingRate.setSpaceName(spaceName);
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
