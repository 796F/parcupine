package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.LicensePlate;
import com.parq.server.dao.model.object.User;

public class LicensePlateDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "LicensePlateCache";
	private static Cache myCache;
	
	private static final String licensePlateCache = "getLicensePlateByUserId:";
	private static final String userCache = "getUserByPlateNumber:";
	
	private static final String sqlGetLicensePlateByUserId =
			"SELECT plate_id, user_id, plate_number, is_primary " +
			" FROM licenseplate lp " +
			" WHERE lp.user_id = ? " +
			" AND lp.is_deleted IS NOT TRUE";
	
	private static final String sqlGetUserByPlateNumber = 
			"SELECT u.user_id, u.password, u.email, u.phone_number " +
			" FROM user u, licenseplate lp " +
			" WHERE u.user_id = lp.user_id " +
			" AND lp.plate_number = ? " +
			" AND u.is_deleted IS NOT TRUE " + 
			" AND lp.is_deleted IS NOT TRUE";
	
	private static final String sqlInsertNewPlateNumber = 
			"INSERT INTO licenseplate (user_id, plate_number, is_primary) " +
			" VALUES (?, ?, ?)";
	
	private static final String sqlDeletePlateInfomationByPlateId = 
			"UPDATE licenseplate SET is_deleted = TRUE WHERE plate_id = ?";
	
	
	public LicensePlateDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}
	
	/**
	 * Get the list of <code>LicensePlate</code> that belong to this user. If
	 * the user does not have any license plate on record, then
	 * <code>null</code> is returned
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LicensePlate> getLicensePlateByUserId(long userId) {
		if (userId < 0 ) {
			return null;
		}
		
		// the cache key for this method call
		String cacheKey = licensePlateCache + userId;
		
		List<LicensePlate> plates = null;	
		Element cacheEntry = myCache.get(cacheKey);
		if (cacheEntry  != null) {
			plates = (List<LicensePlate>) cacheEntry.getValue();
			return plates;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetLicensePlateByUserId);
			pstmt.setLong(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			plates = createPlateLists(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		// put the plates list into cache
		if (plates != null) {
			// only put none null value into cache
			myCache.put(new Element(cacheKey, plates));
		}
		
		return plates;
	}
	
	private List<LicensePlate> createPlateLists(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		List<LicensePlate> plates = new ArrayList<LicensePlate>();
		
		while (rs.next()) {
			LicensePlate plate = new LicensePlate();
			plate.setPlateID(rs.getLong("plate_id"));
			plate.setUserID(rs.getLong("user_id"));
			plate.setPlateNum(rs.getString("plate_number"));
			plate.setDefault(rs.getBoolean("is_primary"));
			plates.add(plate);
		}
		return plates;
	}

	/**
	 * Get the List of users that is the owner of this license plate number, if there is
	 * no owner on record, the method return <code>null</code>
	 * 
	 * @param plateNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserByPlateNumber(String plateNum) {
		if (plateNum == null || plateNum.isEmpty()) {
			return null;
		}
		// concert the string to be in all uppercase, not sure if this is necessary, but just to be safe.
		String upperCasePlateNum = plateNum.toUpperCase();
		
		// the cache key for this method call
		String cacheKey = userCache + upperCasePlateNum;
		
		List<User> users = null;	
		Element cacheEntry = myCache.get(cacheKey);
		if (cacheEntry  != null) {
			users = (List<User>) cacheEntry.getValue();
			return users;
		}
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetUserByPlateNumber);
			pstmt.setString(1, upperCasePlateNum);
			ResultSet rs = pstmt.executeQuery();
			users = createUserDataObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		// put the plates list into cache
		if (users != null) {
			// only put none null value into cache
			myCache.put(new Element(cacheKey, users));
		}
		
		return users;
	}

	private List<User> createUserDataObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		List<User> users = new ArrayList<User>();
		while (rs.next()) {
			User user = new User();
			rs.first();
			user.setUserID(rs.getLong("user_id"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			user.setPhoneNumber(rs.getString("phone_number"));
			users.add(user);
		}
		
		return users;
	}
	
	/**
	 * Add a new license plate information for a user. Note the userId must be present and valid
	 * 
	 * @param newPlateInfo
	 * @return
	 */
	public boolean addLicensePlateForUser(LicensePlate newPlateInfo) {
		if (newPlateInfo == null || newPlateInfo.getUserID() <= 0 || 
				newPlateInfo.getPlateNum() == null || newPlateInfo.getPlateNum().isEmpty()) {
			throw new IllegalStateException("Invalid addLicensePlateForUser request");
		}
		
		// clear out the cache entry for user that is going to be updated
		revokePlateCache(newPlateInfo.getUserID(), newPlateInfo.getPlateNum());

		PreparedStatement pstmt = null;
		Connection con = null;
		boolean plateInfoInsertedSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlInsertNewPlateNumber);
			pstmt.setLong(1, newPlateInfo.getUserID());
			pstmt.setString(2, newPlateInfo.getPlateNum());
			pstmt.setBoolean(3, newPlateInfo.isDefault());
			plateInfoInsertedSuccessful = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return plateInfoInsertedSuccessful;
	}
	
	/**
	 * Remove the license plate information from a user's account. Note: both plateId and userId must be present and valid.
	 * 
	 * @param deletePlateInfo
	 * @return
	 */
	public boolean deleteLicensePlateInfo(LicensePlate deletePlateInfo) {
		if (deletePlateInfo == null 
				|| deletePlateInfo.getPlateID() <= 0 || deletePlateInfo.getUserID() <= 0) {
			throw new IllegalStateException("Invalid removeLicensePlateInfo request");
		}
		
		// clear out the cache entry for deleted user
		revokePlateCache(deletePlateInfo.getUserID(), deletePlateInfo.getPlateNum());
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean deleteSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlDeletePlateInfomationByPlateId);
			pstmt.setLong(1, deletePlateInfo.getPlateID());
			deleteSuccessful = pstmt.executeUpdate() > 0;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return deleteSuccessful;
	}

	
	/**
	 * Remove the plateId and userId cache. If the id(s) value are <= 0, then the id value is ignored.
	 * Method can be use to remove both cache, or one cache at a time.
	 * 
	 * @param plateID
	 * @param userID
	 */
	private void revokePlateCache(long userID, String plateNum) {
		if (userID > 0) {
			revokeCache(myCache, licensePlateCache, "" + userID);
		}
		if (plateNum != null && !plateNum.isEmpty()) {
			revokeCache(myCache, userCache, plateNum.toUpperCase());
		}
	}
}
