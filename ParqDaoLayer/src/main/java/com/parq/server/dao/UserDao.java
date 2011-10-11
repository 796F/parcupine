package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.User;

/**
 * Dao class responsible for accessing and updating the User Table
 * 
 * @author GZ
 * 
 */
public class UserDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private String cacheName = "UserCache";
	private static Cache myCache;

	private String sqlGetUserStatement = "SELECT User_ID, UserName, Password, eMail FROM User ";
	private String sqlGetUserById = sqlGetUserStatement + "WHERE User_ID = ?";
	private String sqlGetUserByUserName = sqlGetUserStatement + "WHERE UserName = ?";
	private String sqlGetUserByEmail = sqlGetUserStatement + "WHERE eMail = ?";

	private String sqlDeleteUserById = "DELETE FROM User WHERE User_ID = ?";

	private String sqlUpdateUser = "UPDATE User SET UserName = ?, Password = ?, eMail = ? "
			+ " WHERE User_ID = ?";
	
	private String sqlCreateUser = "INSERT INTO User (UserName, Password, eMail) "
			+ " VALUES (?, ?, ?)";

	public UserDao() {
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
	private User createUserObject(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		User user = new User();
		rs.first();
		user.setUserID(rs.getInt("User_ID"));
		user.setUserName(rs.getString("UserName"));
		user.setPassword(rs.getString("Password"));
		user.setEmail(rs.getString("eMail"));
		return user;
	}

	public User getUserById(int id) {
		// the cache key for this method call;
		String cacheKey = "getUserById:" + id;
		
		User user = null;
		if (myCache.get(cacheKey) != null) {
			user = (User) myCache.get(cacheKey).getValue();
			if (user != null) {
				return user;
			}
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetUserById);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			user = createUserObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		if (user != null) {
			myCache.put(new Element(cacheKey, user));
		}
		
		return user;
	}

	public User getUserByUserName(String userName) {
		// the cache key for this method call;
		String cacheKey = "getUserByUserName:" + userName;

		User user = null;
		if (myCache.get(cacheKey) != null) {
			user = (User) myCache.get(cacheKey).getValue();
			if (user != null) {
				return user;
			}
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetUserByUserName);
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();

			user = createUserObject(rs);

		} catch (SQLException sqle) {
			user = null;
		} finally {
			closeConnection(con);
		}

		// put result into cache
		if (user != null) {
			myCache.put(new Element(cacheKey, user));
		}
		
		return user;
	}

	public User getUserByEmail(String emailAddress) {
		// the cache key for this method call;
		String cacheKey = "getUserByEmail:" + emailAddress;

		User user = null;
		if (myCache.get(cacheKey) != null) {
			user = (User) myCache.get(cacheKey).getValue();
			if (user != null) {
				return user;
			}
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetUserByEmail);
			pstmt.setString(1, emailAddress);
			ResultSet rs = pstmt.executeQuery();

			user = createUserObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		if (user != null) {
			myCache.put(new Element(cacheKey, user));
		}
		
		return user;
	}

	public synchronized boolean deleteUserById(int id) {

		PreparedStatement pstmt = null;
		Connection con = null;
		boolean deleteSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlDeleteUserById);
			pstmt.setInt(1, id);
			deleteSuccessful = pstmt.executeUpdate() > 0;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// clear out the cache when delete is successful
		if (deleteSuccessful) {
			myCache.removeAll();
		}

		return deleteSuccessful;
	}

	public synchronized boolean updateUserById(User user) {

		PreparedStatement pstmt = null;
		Connection con = null;
		boolean updateSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlUpdateUser);
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			pstmt.setInt(4, user.getUserID());
			updateSuccessful = pstmt.executeUpdate() > 0;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// clear out the cache when update is successful
		if (updateSuccessful) {
			myCache.removeAll();
		}

		return updateSuccessful;
	}
	
	public synchronized boolean createNewUser(User user) {

		// test to make sure no duplicate username is created
		if (getUserByUserName(user.getUserName()) != null) {
			throw new IllegalStateException("Userame: " + user.getUserName() + " already exist");
		}
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newUserCreated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlCreateUser);
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			newUserCreated = pstmt.executeUpdate() == 1;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		return newUserCreated;
	}

}
