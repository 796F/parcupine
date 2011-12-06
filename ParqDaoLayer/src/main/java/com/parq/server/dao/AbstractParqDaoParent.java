package com.parq.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public abstract class AbstractParqDaoParent {

	private Connection con;
	protected static final String driver = "com.mysql.jdbc.Driver";
	protected static String propertyFile = "./ParqDao.properties";

	protected static String url;
	protected static String db;
	protected static String login;
	protected static String pwd;
	protected static boolean initizationComplete;

	// Initialize the db connection
	protected void initialize() {
		// properties file loading only need to happen once per JVM startup,
		// once the properties file is loaded, those properties are in memory
		// for rest of the jvm session
		if (!initizationComplete) {
			InputStream is = null;
			try {
				Properties prop = new Properties();
				is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(propertyFile);

				if (is == null) {
					System.err.println("Properties File: " + propertyFile
							+ " not found");
					throw new IllegalStateException("Properties File: "
							+ propertyFile + " not found");
				} else {
					prop.load(is);

					url = prop.getProperty("url");
					db = prop.getProperty("db");
					login = prop.getProperty("login");
					pwd = prop.getProperty("pwd");
					url = prop.getProperty("url");
				}

				// dynamically load the mysql driver
				Class.forName(driver);

			} catch (IOException e) {
				System.err.println("Error reading property file: "
						+ propertyFile + " " + e);
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				System.err.println("MySQL driver class loading issue: " + e);
				throw new RuntimeException(e);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					System.err.println("Error closing property file reader: "
							+ propertyFile + " " + e);
					throw new RuntimeException(e);
				}
			}
			initizationComplete = true;
		}
	}

	// configure and setup the cache objects
	protected Cache setupCache(String cacheName) {
		CacheManager.create();
		Cache myCache = CacheManager.getInstance().getCache(cacheName);

		if (myCache == null) {
			throw new IllegalStateException(
					"No cache configuration exist under this cache name: "
							+ cacheName);
		}
		return myCache;
	}

	/**
	 * Default constructor
	 */
	public AbstractParqDaoParent() {
		initialize();
	}

	/**
	 * Obtain a DB connection from the JDBC driver to the PARQ DB
	 * 
	 * @return
	 */
	protected synchronized Connection getConnection() {
		try {
			con = DriverManager.getConnection(url + db, login, pwd);
			// System.out.println(url + db + " connected");

		} catch (SQLException e) {
			System.err.println("Failed to connect to MySQL DB: " + e);
			throw new RuntimeException(e);
		}
		return con;
	}

	/**
	 * Close the DB connection to the PARQ DB
	 * 
	 * @param connection
	 */
	protected synchronized void closeConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err
					.println("Error closing active mySQL DB connection: " + e);
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * A short cut method to close the connection if somehow the caller lost the
	 * reference to the original DB connection
	 */
	protected void closeConnection() {
		closeConnection(con);
	}

	/**
	 * Revoke an cache entry based on the cache, and 2 piece of the cache key.
	 * The full cache key is constructed by the string concatenation of the
	 * keyPrefix with the key value
	 * 
	 * @param cache the cache that contain the entry to be revoked
	 * @param keyPrefix the first part of the cache key.
	 * @param value the second part of the cache key
	 * @return <code>true</code> if the cache entry revocation is successful, <code>false</code> otherwise
	 */
	protected boolean revokeCache(Cache cache, String keyPrefix, String value) {
		return revokeCache(cache, keyPrefix + value);
	}
	
	/**
	 * Revoke an cache entry based on the cache and the cache key.
	 * 
	 * @param cache the cache that contain the entry to be revoked
	 * @param fullKey key to the cache entry that is going to revoked
	 * @return <code>true</code> if the cache entry revocation is successful, <code>false</code> otherwise
	 */
	protected boolean revokeCache(Cache cache, String fullKey) {
		if (cache == null || fullKey == null) {
			return false;
		}	
		return cache.remove(fullKey);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		closeConnection();
	}
	
	
}
