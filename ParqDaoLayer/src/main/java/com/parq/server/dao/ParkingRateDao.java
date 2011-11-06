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
		"SELECT R.Client_ID, R.Location_ID, R.Space_ID, R.Default_Parking_Rate, R.Priority " +
			" FROM Client AS C, ParkingLocation AS B, ParkingSpace AS S, ClientDefaultRate AS R " +
			" WHERE C.Client_ID = B.Client_ID " +
			" AND B.Location_ID = S.Location_ID " +
			" AND R.Client_ID = C.Client_ID " +
			" AND R.Location_ID IN(NULL, B.Location_ID) " +
			" AND R.Space_ID IN(NULL, S.Space_ID) " +
			" AND C.Name = ? " +
			" AND B.Location_Name = ? " +
			" AND S.Space_Name = ? " +
			" AND C.Is_Deleted IS NOT TRUE " +
			" AND B.Is_Deleted IS NOT TRUE " +
			" AND S.Is_Deleted IS NOT TRUE " +
			" ORDER BY R.Priority DESC"; 
	
	private static final String sqlGetParkingLocationParkingRate = 
		"SELECT R.Client_ID, R.Location_ID, R.Space_ID, R.Default_Parking_Rate, R.Priority " +
			" FROM Client AS C, ParkingLocation AS B, ClientDefaultRate AS R " +
			" WHERE C.Client_ID = B.Client_ID " +
			" AND R.Client_ID = C.Client_ID " +
			" AND R.Location_ID IN(NULL, B.Location_ID) " +
			" AND C.Name = ? " +
			" AND B.Location_Name = ? " +
			" AND C.Is_Deleted IS NOT TRUE " +
			" AND B.Is_Deleted IS NOT TRUE " +
			" ORDER BY R.Priority DESC"; 
	
	private static final String sqlGetClientParkingRate = 
		"SELECT R.Client_ID, R.Location_ID, R.Space_ID, R.Default_Parking_Rate, R.Priority " +
			" FROM Client AS C, ParkingLocation AS B, ClientDefaultRate AS R " +
			" WHERE R.Client_ID = C.Client_ID " +
			" AND C.Name = ? " +
			" AND C.Is_Deleted IS NOT TRUE " +
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
	public ParkingRate getParkingRateByName(String clientName,
			String parkingLocationName, String spaceName) {
		
		// the cache key for this method call;
		String cacheKey = parkingRateByNamePrefix + clientName + ":" + parkingLocationName + ":" + spaceName;
		
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
			
			String stmtToUse = sqlGetClientParkingRate;
			if (parkingLocationName != null && !parkingLocationName.isEmpty()) {
				stmtToUse = sqlGetParkingLocationParkingRate;
			}
			if (spaceName != null && !spaceName.isEmpty()) {
				stmtToUse = sqlGetSpaceParkingRate;
			}
			
			pstmt = con.prepareStatement(stmtToUse);
			pstmt.setString(1, clientName);
			if (parkingLocationName != null && !parkingLocationName.isEmpty()) {
				pstmt.setString(2, parkingLocationName);
			}
			if (spaceName != null && !spaceName.isEmpty()) {
				pstmt.setString(3, spaceName);
			}
			
			System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery();
			rate = createParkingRate(clientName, parkingLocationName, spaceName, rs);

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

	private ParkingRate createParkingRate(String clientName,
			String parkingLocationName, String spaceName, ResultSet rs) throws SQLException {
		
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		rs.first();
		
		int clientId = rs.getInt("Client_ID");
		int parkingLocationId = rs.getInt("Location_ID");
		int spaceId = rs.getInt("Space_ID");
		double rate = rs.getDouble("Default_Parking_Rate");
		
		ParkingRate parkingRate = new ParkingRate();
		parkingRate.setRateType(RateType.Client);
		parkingRate.setClientId(clientId);
		parkingRate.setClientName(clientName);
		parkingRate.setParkingRate(rate);
		
		if (parkingLocationId > 0) {
			parkingRate.setRateType(RateType.ParkingLocation);
			parkingRate.setLocationId(parkingLocationId);
			parkingRate.setParkingLocationName(parkingLocationName);
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
