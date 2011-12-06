package com.parq.server.dao.support;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.parq.server.dao.AbstractParqDaoParent;

/**
 * Dao class created just for unit testing purposes
 * 
 * @author GZ
 *
 */
public class DaoForTestingPurposes extends AbstractParqDaoParent {
		
	public boolean executeSqlStatement(String sql, Object[] parms) {
		
		CallableStatement stmt = null;
		Connection con = null;
		boolean executionResult = false;
		try {
			con = getConnection();
			stmt = con.prepareCall(sql);
			// set the parameter of the sql statement
			if (parms != null) {
				for (int i = 0; i < parms.length; i++) {
					stmt.setObject(i + 1, parms[i]);
				}
			}
			// System.out.println("  " + stmt.toString());
			
			// run the sql statement
			executionResult = stmt.executeUpdate() > 0;
			
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + stmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return executionResult;
	}
	
	public boolean executeSqlStatement(String sql) {
		return executeSqlStatement(sql, null);
	}
}
