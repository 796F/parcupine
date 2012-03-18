package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.exception.DuplicateEmailException;
import com.parq.server.dao.model.object.Admin;
import com.parq.server.dao.model.object.AdminRole;

/**
 * @author GZ
 *
 */
public class AdminDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "AdminCache";
	private static Cache myCache;

	private static final String sqlGetAdminStatement = 
		"SELECT a.admin_id, a.password, a.email, a.client_id, a.admin_role " +
		" FROM admin AS a " +
		" WHERE a.is_deleted IS NOT TRUE ";
	private static final String sqlGetAdminById = sqlGetAdminStatement + " AND a.admin_id = ? ";
	private static final String sqlGetAdminByEmail = sqlGetAdminStatement + " AND a.email = ? ";
	
	private static final String sqlCreateAdmin = "INSERT INTO admin (email, password, client_id, admin_role) " +
			" VALUES (?, ?, ?, ?)";

	private static final String sqlUpdateAdmin = "UPDATE admin SET email = ?, password = ?, admin_role = ? "
		+ " WHERE admin_id = ?";
	private static final String sqlDeleteAdmin = "UPDATE admin SET is_deleted = TRUE, email = ? WHERE admin_id = ?";
	
	private static final String emailCache = "getAdminByEmail:";
	private static final String idCache = "getAdminById:";
	

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
	private Admin createAdminObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		Admin admin = new Admin();
		rs.first();
		admin.setAdminId(rs.getLong("admin_id"));
		admin.setPassword(rs.getString("password"));
		admin.setEmail(rs.getString("email"));
		admin.setClientId(rs.getLong("client_id"));
		
		String role = rs.getString("admin_role");
		if (role != null && !role.isEmpty()) {
			admin.setAdminRole(AdminRole.valueOf(role));
		}
		else {
			// set a default admin role
			admin.setAdminRole(AdminRole.admin);
		}
		
		return admin;
	}

	public Admin getAdminById(long adminId) {
		// the cache key for this method call;
		String cacheKey = idCache + adminId;
		
		Admin admin = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			admin = (Admin) cacheEntry.getValue();
			return admin;
		}

		// query the DB for the Admin object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAdminById);
			pstmt.setLong(1, adminId);
			ResultSet rs = pstmt.executeQuery();

			admin = createAdminObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		if (admin != null) {
			// only put none null value into cache
			myCache.put(new Element(cacheKey, admin));
		}
		
		return admin;
	}

	public Admin getAdminByEmail(String emailAddress) {
		// the cache key for this method call;
		String cacheKey = emailCache + emailAddress;

		Admin admin = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			admin = (Admin) cacheEntry.getValue();
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

			admin = createAdminObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		if (admin != null) {
			// only put none null value into cache
			myCache.put(new Element(cacheKey, admin));
		}
		
		return admin;
	}
	
	/**
	 * Create a new admin user
	 * 
	 * @param admin
	 * @return
	 */
	public boolean createAdmin(Admin admin) {
		if (admin == null || admin.getClientId() <= 0 || admin.getAdminRole() == null || admin.getEmail() == null 
				|| admin.getPassword() == null) {
			throw new IllegalStateException("Invalid admin create request");
		}
		// test to make sure no duplicate email is used
		else if(getAdminByEmail(admin.getEmail()) != null) {
			throw new DuplicateEmailException("Email: " + admin.getEmail() + " already exist");
		}
		clearAdminCache();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newAdminCreated = false;
		try {
			con = getConnection();
			
			pstmt = con.prepareStatement(sqlCreateAdmin);
			pstmt.setString(1, admin.getEmail());
			pstmt.setString(2, admin.getPassword());
			pstmt.setLong(3, admin.getClientId());
			pstmt.setString(4, admin.getAdminRole().name());
			newAdminCreated = pstmt.executeUpdate() == 1;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return newAdminCreated;
	}


	/**
	 * Update an existing admin user
	 * @param admin
	 * @return
	 */
	public boolean updateAdmin(Admin admin) {
		if (admin == null || admin.getEmail() == null || admin.getAdminId() <= 0) {
			throw new IllegalStateException("Invalid admin update request");
		}
		// test to make sure no duplicate email is used
		Admin tempAdmin = getAdminByEmail(admin.getEmail());
		if(tempAdmin != null && tempAdmin.getAdminId() != admin.getAdminId()) {
			throw new DuplicateEmailException("Email: " + admin.getEmail() + " already exist");
		}
		// clear out the cache entries
		clearAdminCache();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean updateSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlUpdateAdmin);
			pstmt.setString(1, admin.getEmail());
			pstmt.setString(2, admin.getPassword());
			pstmt.setString(3, admin.getAdminRole().name());
			pstmt.setLong(4, admin.getAdminId());
			updateSuccessful = pstmt.executeUpdate() > 0;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return updateSuccessful;
	}


	/**
	 * Delete an existing admin user
	 * @param admin
	 * @return
	 */
	public boolean deleteAdmin(long adminId) {
		if (adminId <= 0) {
			throw new IllegalStateException("Invalid user delete request");
		}
		Admin delAdmin = getAdminById(adminId);
		// clear out the cache entry for deleted user
		clearAdminCache();
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean deleteSuccessful = false;
		if (delAdmin != null) {
			try {
				con = getConnection();
				pstmt = con.prepareStatement(sqlDeleteAdmin);
				String deletedEmail = delAdmin.getEmail() + " deleted_On:" + System.currentTimeMillis();
				pstmt.setString(1, deletedEmail);
				pstmt.setLong(2, adminId);
				deleteSuccessful = pstmt.executeUpdate() > 0;
				
			} catch (SQLException sqle) {
				System.out.println("SQL statement is invalid: " + pstmt);
				sqle.printStackTrace();
				throw new RuntimeException(sqle);
			} finally {
				closeConnection(con);
			}
		}
		return deleteSuccessful;
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
