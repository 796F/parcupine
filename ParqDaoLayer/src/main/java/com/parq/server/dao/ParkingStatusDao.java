package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.mysql.jdbc.Statement;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.Payment;

public class ParkingStatusDao extends AbstractParqDaoParent{
	
	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "ParkingStatusCache";
	private static Cache myCache;
	
	private static final String sqlGetLatestParkingStatusBySpaceIdsSelectPart = 
		"SELECT pi.ParkingInst_id, pi.user_id, pi.space_id, pi.park_began_time, " +
		"       pi.park_end_time, pi.is_paid_parking, " +
		"       p.payment_id, p.payment_type, p.payment_ref_num, p.payment_datetime, " +
		"       p.amount_paid_cents " +
		" FROM ParkingInstance as pi, Payment as p " +
		" WHERE p.ParkingInst_id = pi.ParkingInst_id " +
		" AND pi.ParkingInst_id IN (SELECT MAX(ParkingInst_id) FROM ParkingInstance WHERE space_id IN( ";
	private static final String sqlOrderByPart =	") GROUP BY space_id) ORDER BY pi.space_id;";
	
	private static final String sqlGetParkingStatusByUserId = 
		"SELECT pi.ParkingInst_id, pi.user_id, pi.space_id, pi.park_began_time, " +
		"       pi.park_end_time, pi.is_paid_parking, " +
		"       p.payment_id, p.payment_type, p.payment_ref_num, p.payment_datetime, " +
		"       p.amount_paid_cents " +
		" FROM ParkingInstance as pi, Payment as p " +
		" WHERE p.ParkingInst_id = pi.ParkingInst_id " +
		" AND pi.user_id = ? " +
		" ORDER BY pi.ParkingInst_id DESC " + 
		" LIMIT 1";
	
	private static final String sqlInsertParkingInstance = 
		"INSERT INTO ParkingInstance (User_ID, Space_ID, Park_Began_Time, Park_End_Time, Is_Paid_Parking) " + 
		" VALUES (?, ?, ?, ?, ?)";
	private static final String sqlInsertPayment = 
		"INSERT INTO Payment (ParkingInst_ID, Payment_Type, Payment_Ref_Num, Payment_DateTime, Amount_Paid_Cents) " +
		" VALUES ((SELECT MAX(ParkingInst_ID) FROM ParkingInstance WHERE space_id = ?), ?, ?, ?, ?)";
	
	private static final String sqlUpdateParkingEndTime =
		"UPDATE ParkingInstance SET Park_End_Time = ? WHERE ParkingInst_id = ? AND Space_ID = ? ";
	
	private static final String getParkingStatusBySpaceIdsCacheKey = "spaceId:";
	private static final String getUserParkingStatusCacheKey = "userId:";
	private static final String getSpaceIdByUserId = "spaceIdToUserId:";
	
	public ParkingStatusDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}
	
	/**
	 * Get the parking status of multiple spaces, including summary of payment informations
	 * 
	 * @param spaceIds
	 * @return
	 */
	public List<ParkingInstance> getParkingStatusBySpaceIds(int[] spaceIds) {

		if (spaceIds == null || spaceIds.length == 0) {
			return null;
		}
		
		// create the list of spaces id to check with the cache first
		List<Integer> spaceIdsToCheck = new ArrayList<Integer>();
		for (int s : spaceIds) {
			spaceIdsToCheck.add(s);
		}
		
		
		List<ParkingInstance> results = new ArrayList<ParkingInstance>();
		List<Integer> spaceIdsToCheckInDB = new ArrayList<Integer>();
		// check the cache for the spaces, space by space.
		for (int spaceId : spaceIdsToCheck)
		{
			ParkingInstance parkInst = getCachedParkingInstanceBySpaceId(spaceId);
			if (parkInst != null) {
				results.add(parkInst);
			}
			else {
				spaceIdsToCheckInDB.add(spaceId);
			}
		}
		
		// for any space information not in the cache, we hit the DB
		if (!spaceIdsToCheckInDB.isEmpty())
		{
			// query the DB for the user object
			Statement stmt = null;
			Connection con = null;
			
			try {
				con = getConnection();
				// build the query statement.
				StringBuilder sqlQuery = new StringBuilder(sqlGetLatestParkingStatusBySpaceIdsSelectPart);
				int numSpaces = spaceIdsToCheckInDB.size();
				for (int i = 0; i < numSpaces - 1; i++) {
					sqlQuery.append(spaceIdsToCheckInDB.get(i));
					sqlQuery.append(", ");
				}
				sqlQuery.append(spaceIdsToCheckInDB.get(numSpaces - 1));
				sqlQuery.append(sqlOrderByPart);
				
				stmt = (Statement) con.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery.toString());
	
				results.addAll(createParkingStatusObject(rs));
	
			} catch (SQLException sqle) {
				System.out.println("SQL statement is invalid: " + stmt);
				sqle.printStackTrace();
				throw new RuntimeException(sqle);
			} finally {
				closeConnection(con);
			}
		}
		
		// put the individual ParkingStatus into cache
		for (ParkingInstance pi : results) {
			String cacheKey = createCacheKey(getParkingStatusBySpaceIdsCacheKey, pi.getSpaceId());
			if (pi != null) {
				myCache.put(new Element(cacheKey, pi));
			}
		}
		
		return results;
	}
	
	
	private String createCacheKey(String cacheKey, int id) {
		return cacheKey + id;
	}

	public ParkingInstance getUserParkingStatus(int userId) {
		ParkingInstance userParkingStatus = getCacheParkingInstanceByUserId(userId);

		// if cache match is found, return result
		if (userParkingStatus != null) {
			return userParkingStatus;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetParkingStatusByUserId);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			List<ParkingInstance> piList = createParkingStatusObject(rs);
			if (piList != null && !piList.isEmpty()) {
				userParkingStatus = piList.get(0);
			}

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		//put result into cache
		if (userParkingStatus != null) {
			// first create a spaceId to userId cache entry
			myCache.put(new Element(createCacheKey(getSpaceIdByUserId,
					userParkingStatus.getSpaceId()), userId));
			// then create a userId to parking instance cache entry
			myCache.put(new Element(createCacheKey(
					getUserParkingStatusCacheKey, userId), userParkingStatus));
		}
		
		return userParkingStatus;
		
	}

	/**
	 * Create the parking status object
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<ParkingInstance> createParkingStatusObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<ParkingInstance> parkingInstList = new ArrayList<ParkingInstance>();

		while (rs.next()) {
			ParkingInstance parkingInst = new ParkingInstance();
			parkingInst.setParkingInstId(rs.getInt("ParkingInst_id"));
			parkingInst.setUserId(rs.getInt("user_id"));
			parkingInst.setSpaceId(rs.getInt("space_id"));
			parkingInst.setParkingBeganTime(rs.getTimestamp("park_began_time"));
			parkingInst.setParkingEndTime(rs.getTimestamp("park_end_time"));
			
			Payment paymentInfo = new Payment();
			paymentInfo.setPaymentId(rs.getInt("payment_id"));
			paymentInfo.setParkingInstId(parkingInst.getParkingInstId());
			paymentInfo.setPaymentType(Payment.PaymentType.valueOf(rs.getString("payment_type")));
			paymentInfo.setPaymentRefNumber(rs.getString("payment_ref_num"));
			paymentInfo.setPaymentDateTime(rs.getTimestamp("payment_datetime"));
			paymentInfo.setAmountPaidCents(rs.getInt("amount_paid_cents"));
			
			parkingInst.setPaymentInfo(paymentInfo);
			
			parkingInstList.add(parkingInst);
		}
		return parkingInstList;
	}
	
	/**
	 * Insert parking status into the DB <BR>
	 * on the <code>ParkingInstance</code> object,
	 * the userId, spaceId, parkingBeganTime, parkingEndTime, isPaidParking field
	 *  must be set, and the <code>Payment</code> object must not be null <BR>
	 * <BR>
	 * On the <code>Payment</code> object,
	 * the paymentType, paymentRefNumber, paymentDateTime, and amountPaid field must be set <BR>
	 * 
	 * @param parkingInst
	 * @return
	 */
	public boolean addPaymentForParking(ParkingInstance parkingInst) {
		
		int parkingSpaceId = parkingInst.getSpaceId();
		if (parkingSpaceId < 1) {
			throw new IllegalStateException("Parking Space Id is invalid: " + parkingSpaceId);
		}
		revokeSpaceCacheById(parkingSpaceId);
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean parkingInstanceCreated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlInsertParkingInstance);
			pstmt.setInt(1, parkingInst.getUserId());
			pstmt.setInt(2, parkingInst.getSpaceId());
			pstmt.setTimestamp(3, new Timestamp(parkingInst.getParkingBeganTime().getTime()));
			pstmt.setTimestamp(4, new Timestamp(parkingInst.getParkingEndTime().getTime()));
			pstmt.setBoolean(5, parkingInst.isPaidParking());
			if (pstmt.executeUpdate() == 1)
			{
				pstmt = con.prepareStatement(sqlInsertPayment);
				pstmt.setInt(1, parkingInst.getSpaceId());
				pstmt.setString(2, parkingInst.getPaymentInfo().getPaymentType().toString());
				pstmt.setString(3, parkingInst.getPaymentInfo().getPaymentRefNumber());
				pstmt.setTimestamp(4, new Timestamp(parkingInst.getPaymentInfo().getPaymentDateTime().getTime()));
				pstmt.setInt(5, parkingInst.getPaymentInfo().getAmountPaidCents());
				
				parkingInstanceCreated = pstmt.executeUpdate() == 1;
			}
			
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return parkingInstanceCreated;
	}
	
	public boolean updateParkingEndTimeBySpaceId(int spaceId, int parkingInstanceId, Date endTime) {
		
		if (spaceId < 1 || parkingInstanceId < 1) {
			throw new IllegalStateException(
					"updateParkingEndTimeBySpaceId(...) method parmeters is invalid, spaceId: "
							+ spaceId + ", parkingInstanceId: "
							+ parkingInstanceId);
		}
		revokeSpaceCacheById(spaceId);
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean parkingEndTimeUpdated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlUpdateParkingEndTime);
			pstmt.setTimestamp(1, new Timestamp(endTime.getTime()));
			pstmt.setInt(2, parkingInstanceId);
			pstmt.setInt(3, spaceId);
			if (pstmt.executeUpdate() == 1) {
				parkingEndTimeUpdated = true;
			}
			
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return parkingEndTimeUpdated;
	}
	
	/**
	 * Revoke all the cache instance of this User by id, username, and email address.
	 * @param userID
	 */
	private synchronized void revokeSpaceCacheById(int spaceId) {
		if (spaceId < 1) {
			return;
		}
		
		// revoke the spaceId to Parking Instance cache
		ParkingInstance parkingInst = getCachedParkingInstanceBySpaceId(spaceId);
		if (parkingInst != null) {
			revokeCache(myCache, createCacheKey(getParkingStatusBySpaceIdsCacheKey, spaceId));
		}

		// revoke the userId to Parking Instance cache
		int userId = getCachedSpaceIdToUserId(spaceId);
		if (userId > 0) {
			revokeCache(myCache, createCacheKey(getUserParkingStatusCacheKey, userId));
			// remove the spaceId to userId relationship
			revokeCache(myCache, createCacheKey(getSpaceIdByUserId, spaceId));
		}
	}

	private ParkingInstance getCachedParkingInstanceBySpaceId(int spaceId) {
		String cacheKey = createCacheKey(getParkingStatusBySpaceIdsCacheKey, spaceId);
		ParkingInstance parkInst = null;
		
		if (myCache.get(cacheKey) != null) {
			parkInst = (ParkingInstance) myCache.get(cacheKey).getValue();
		}
		return parkInst;
	}
	
	private ParkingInstance getCacheParkingInstanceByUserId(int userId) {
		String cacheKey = createCacheKey(getUserParkingStatusCacheKey, userId);

		ParkingInstance userParkingStatus = null;
		if (myCache.get(cacheKey) != null) {
			userParkingStatus = (ParkingInstance) myCache.get(cacheKey).getValue();
		}
		return userParkingStatus;
	}
	
	private int getCachedSpaceIdToUserId(int spaceId) {
		String cacheKey = createCacheKey(getSpaceIdByUserId, spaceId);
		
		int userId = -1;
		if (myCache.get(cacheKey) != null && myCache.get(cacheKey).getValue() != null) {
			userId = (Integer) myCache.get(cacheKey).getValue();
		}
		return userId;
	}
}
