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
		"SELECT R.Location_ID, R.Space_ID, R.Parking_Rate_Cents, R.Priority, R.Time_Increment_Mins, R.Max_Park_Mins " +
			" FROM ParkingLocation AS B, ParkingSpace AS S, ParkingRate AS R " +
			" WHERE B.Location_ID = S.Location_ID " +
			" AND R.Location_ID = B.Location_ID " +
			" AND R.Space_ID IN(NULL, S.Space_ID) " +
			" AND B.Location_Identifier = ? " +
			" AND S.Space_Identifier = ? " +
			" AND B.Is_Deleted IS NOT TRUE " +
			" AND S.Is_Deleted IS NOT TRUE " +
			" ORDER BY R.Priority DESC"; 
	
	private static final String sqlGetParkingLocationParkingRate = 
		"SELECT R.Location_ID, R.Space_ID, R.Parking_Rate_Cents, R.Priority, R.Time_Increment_Mins, R.Max_Park_Mins " +
			" FROM ParkingLocation AS B, ParkingRate AS R " +
			" WHERE R.Location_ID = B.Location_ID " +
			" AND B.Location_Identifier = ? " +
			" AND B.Is_Deleted IS NOT TRUE " +
			" ORDER BY R.Priority DESC"; 
	
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
			
			System.out.println(pstmt);
			
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

		int parkingLocationId = rs.getInt("Location_ID");
		int spaceId = rs.getInt("Space_ID");
		int rate = rs.getInt("Parking_Rate_Cents");
		int timeIncrements = rs.getInt("Time_Increment_Mins");
		int maxParkTime = rs.getInt("Max_Park_Mins");
		
		ParkingRate parkingRate = new ParkingRate();
		parkingRate.setRateType(RateType.Client);
		parkingRate.setParkingRateCents(rate);
		parkingRate.setTimeIncrementsMins(timeIncrements);
		parkingRate.setMaxParkMins(maxParkTime);
		
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
