package com.parq.server.dao.support;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;
import com.parq.server.dao.AbstractParqDaoParent;

/**
 * Dao class created just for unit testing purposes
 * 
 * @author GZ
 *
 */
public class DaoForTestingPurposes extends AbstractParqDaoParent {
		
	public boolean executeSqlStatement(String sql) {
		
		System.out.println("Test SQL Statement:\n   " + sql);
		Statement stmt = null;
		Connection con = null;
		boolean executionResult = false;
		try {
			con = getConnection();
			stmt = (Statement) con.createStatement();
			
			executionResult = stmt.execute(sql);
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + stmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return executionResult;
	}
}
