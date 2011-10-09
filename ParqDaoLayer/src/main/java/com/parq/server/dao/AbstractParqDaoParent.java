package com.parq.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

abstract class AbstractParqDaoParent {

	protected Connection con;
	protected static final String driver = "com.mysql.jdbc.Driver";
	protected static String propertyFile = "/ParqDao.properties";

	protected static String url;
	protected static String db;
	protected static String login;
	protected static String pwd;
	protected static boolean initizationComplete;

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
					throw new IllegalStateException("Properties File: " + propertyFile
							+ " not found");
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

	protected synchronized void connect() {
		try {
			con = DriverManager.getConnection(url + db, login, pwd);
			System.out.println(url + db + " connected");

		} catch (SQLException e) {
			System.err.println("Failed to connect to MySQL DB: " + e);
			throw new RuntimeException(e);
		}
	}

	protected synchronized void close() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			System.err
					.println("Error closing active mySQL DB connection: " + e);
			throw new RuntimeException(e);
		}
	}
}
