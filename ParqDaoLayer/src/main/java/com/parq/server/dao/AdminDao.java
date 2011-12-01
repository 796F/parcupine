package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.parq.server.dao.model.object.Admin;
import com.parq.server.dao.model.object.ClientRelationShip;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class AdminDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "AdminCache";
	private static Cache myCache;

	private static final String sqlGetAdminStatement = 
		"SELECT a.Admin_Id, a.UserName, a.Password, a.eMail, r.AC_Rel_Id, r.client_id, r.AdminRole_id " +
		" FROM Admin AS a, AdminClientRelationship AS r " +
		" WHERE r.Admin_Id = a.Admin_Id " +
		" AND a.Is_Deleted IS NOT TRUE ";
	private static final String sqlGetAdminById = sqlGetAdminStatement + " AND a.Admin_Id = ? ";
	private static final String sqlGetAdminByUserName = sqlGetAdminStatement + " AND a.UserName = ? ";
	private static final String sqlGetAdminByEmail = sqlGetAdminStatement + " AND a.eMail = ? ";

	private static final String emailCache = "getAdminByEmail:";
	private static final String idCache = "getAdminById:";
	private static final String userNameCache = "getAdminByUserName:";
	

	public AdminDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}
	
	/**
	 * Create the User model object from the DB query result set.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Admin createUserObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		Admin admin = new Admin();
		rs.first();
		admin.setAdminId(rs.getInt("Admin_Id"));
		admin.setUserName(rs.getString("UserName"));
		admin.setPassword(rs.getString("Password"));
		admin.setEmail(rs.getString("eMail"));
		
		ClientRelationShip relationship = new ClientRelationShip();
		admin.getClientRelationships().add(relationship);
		relationship.setAdminId(rs.getInt("Admin_Id"));
		relationship.setClientId(rs.getInt("client_id"));
		relationship.setRelationShipId(rs.getInt("AC_Rel_Id"));
		relationship.setRoleId(rs.getInt("AdminRole_id"));
		
		return admin;
	}

	public Admin getAdminById(int adminId) {
		// the cache key for this method call;
		String cacheKey = idCache + adminId;
		
		Admin admin = null;
		if (myCache.get(cacheKey) != null) {
			admin = (Admin) myCache.get(cacheKey).getValue();
			return admin;
		}

		// query the DB for the Admin object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAdminById);
			pstmt.setInt(1, adminId);
			ResultSet rs = pstmt.executeQuery();

			admin = createUserObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, admin));
		
		return admin;
	}

	public Admin getAdminByUserName(String adminUserName) {
		// the cache key for this method call;
		String cacheKey = userNameCache + adminUserName;

		Admin admin = null;
		if (myCache.get(cacheKey) != null) {
			admin = (Admin) myCache.get(cacheKey).getValue();
			return admin;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAdminByUserName);
			pstmt.setString(1, adminUserName);
			ResultSet rs = pstmt.executeQuery();

			admin = createUserObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, admin));
		
		return admin;
	}

	public Admin getAdminByEmail(String emailAddress) {
		// the cache key for this method call;
		String cacheKey = emailCache + emailAddress;

		Admin admin = null;
		if (myCache.get(cacheKey) != null) {
			admin = (Admin) myCache.get(cacheKey).getValue();
			return admin;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAdminByEmail);
			pstmt.setString(1, emailAddress);
			ResultSet rs = pstmt.executeQuery();

			admin = createUserObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, admin));
		
		return admin;
	}
	
	/**
	 * manually clear out the cache
	 * @return
	 */
	public boolean clearAdminCache() {
		myCache.removeAll();
		return true;
	}
}
