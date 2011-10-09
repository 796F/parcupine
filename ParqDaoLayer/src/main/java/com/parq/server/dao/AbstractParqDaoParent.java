package com.parq.server.dao;

import com.mysql.jdbc.Connection;

public class AbstractParqDaoParent {

	protected Connection con;
	protected static final String driver = "com.mysql.jdbc.Driver";
	protected static final String propertyFile = "/ParqDao.properties";
	
	protected static String url;
	protected static String db;
	protected static String login;
	protected static String pwd;
	protected static boolean initizationComplete;
	
	
}
