package com.parq.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * @author GZ
 *
 */
public abstract class AbstractParqDaoParent {

	protected static final String propertyFile = "./ParqDao.properties";
	protected static final String trueStr = "true";
	protected static final String falseStr = "false";

	protected static String url;
	protected static String db;
	protected static String login;
	protected static String pwd;
	protected static String driver;
	// the following are Tomcat connection pool properties
	protected static boolean jmxEnabled;
	protected static boolean testWhileIdle;
	protected static boolean testOnBorrow;
	protected static String validationQuery;
	protected static boolean testOnReturn;
	protected static int validationInterval;
	protected static int timeBetweenEvictionRuns;
	protected static int maxActive;
	protected static int initialSize;
	protected static int maxWait;
	protected static int removeAbandonedTimeout;
	protected static int minEvictableIdleTime;
	protected static int minIdle;
	protected static boolean logAbandoned;
	protected static boolean removeAbandoned;
	protected static String jdbcInterceptors;

	protected static boolean initizationComplete;
	protected static boolean connectionPoolInitialized;
	protected static boolean connectionPoolAvailable;
	protected static boolean cpWarningMessagePosted;
	
	protected static DataSource tomcatDatasource;
	
	
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
					driver = prop.getProperty("db_driver");
					
					// the following properties are for tomcat DB connection pool
					jmxEnabled = prop.getProperty("enabled_jmx").equalsIgnoreCase(trueStr);
					testWhileIdle = prop.getProperty("test_while_idle").equalsIgnoreCase(trueStr);
					testOnBorrow = prop.getProperty("test_on_borrow").equalsIgnoreCase(trueStr);
					validationQuery = prop.getProperty("validation_query");
					testOnReturn = prop.getProperty("test_on_return").equalsIgnoreCase(trueStr);
					validationInterval = new Integer(prop.getProperty("validation_interval"));
					timeBetweenEvictionRuns = new Integer(prop.getProperty("time_between_eviction_runs"));
					maxActive =  new Integer(prop.getProperty("max_active"));
					initialSize =  new Integer(prop.getProperty("initial_size"));
					maxWait =  new Integer(prop.getProperty("max_wait"));
					removeAbandonedTimeout =  new Integer(prop.getProperty("remove_abandoned_timeout"));
					minEvictableIdleTime = new Integer(prop.getProperty("min_evictable_idle_time"));
					minIdle = new Integer(prop.getProperty("min_idle"));
					logAbandoned = prop.getProperty("log_abandoned").equalsIgnoreCase(trueStr);
					removeAbandoned = prop.getProperty("remove_abandoned").equalsIgnoreCase(trueStr);
					jdbcInterceptors = prop.getProperty("jdbc_interceptor");
					
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

	private void loadTomcatConnectionPool() {
		if (!connectionPoolInitialized) {
			PoolProperties poolProp = new PoolProperties();
			poolProp.setUrl(url+db);
			poolProp.setDriverClassName(driver);
			poolProp.setUsername(login);
			poolProp.setPassword(pwd);
			poolProp.setJmxEnabled(jmxEnabled);
			poolProp.setTestWhileIdle(testWhileIdle);
			poolProp.setTestOnBorrow(testOnBorrow);
			poolProp.setValidationQuery(validationQuery);
			poolProp.setTestOnReturn(testOnReturn);
			poolProp.setValidationInterval(validationInterval);
			poolProp.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRuns);
			poolProp.setMaxActive(maxActive);
			poolProp.setInitialSize(initialSize);
			poolProp.setMaxWait(maxWait);
			poolProp.setRemoveAbandonedTimeout(removeAbandonedTimeout);
			poolProp.setMinEvictableIdleTimeMillis(minEvictableIdleTime);
			poolProp.setMinIdle(minIdle);
			poolProp.setLogAbandoned(logAbandoned);
			poolProp.setRemoveAbandoned(removeAbandoned);
			poolProp.setJdbcInterceptors(jdbcInterceptors);
			
			// create the tomcat db pool
			tomcatDatasource = new DataSource();
			tomcatDatasource.setPoolProperties(poolProp);	
			
			connectionPoolInitialized = true;
			connectionPoolAvailable = true;
		}
		
	}	

	/**
	 * Default constructor
	 */
	public AbstractParqDaoParent() {
		initialize();
		loadTomcatConnectionPool();
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
	 * Obtain a DB connection from the JDBC driver to the PARQ DB
	 * 
	 * @return
	 */
	protected Connection getConnection() {
		
		Connection con;
		try {
			if (connectionPoolAvailable) {
				con = tomcatDatasource.getConnection();
			}
			else {
				synchronized (AbstractParqDaoParent.class) {
					con = DriverManager.getConnection(url + db, login, pwd);
					if (!cpWarningMessagePosted) {
						System.out.println("WARNING:----------------------------------------------");
						System.out.println("WARNING: connection pooling IS NOT being used !!!");
						System.out.println("WARNING:----------------------------------------------");
						cpWarningMessagePosted = true;
					}
				}
			}

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
	protected void closeConnection(Connection connection) {
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
}
