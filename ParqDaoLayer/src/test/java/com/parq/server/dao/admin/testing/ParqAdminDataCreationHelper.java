package com.parq.server.dao.admin.testing;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.parq.server.dao.support.ParqMockObjectCreationDao;

/**
 * @author GZ
 *
 */
final class ParqAdminDataCreationHelper extends ParqMockObjectCreationDao {


	
	// -------------------------------------------------------------------------------
	// public methods for use for manual data creation for testing purposes
	// -------------------------------------------------------------------------------

	@Override
	public boolean executeSqlStatement(String sql, Object[] parms) {

		CallableStatement stmt = null;
		Connection con = null;
		try {
			con = getConnection();
			stmt = con.prepareCall(sql);
			// set the parameter of the sql statement
			if (parms != null) {
				for (int i = 0; i < parms.length; i++) {
					stmt.setObject(i + 1, parms[i]);
				}
			}

			String toStringStmt = stmt.toString();
			String[] toStringStmtParts = toStringStmt.split(": ");
			String sqlStringStmt = toStringStmt.substring(toStringStmtParts[0].length() + 2);
			System.out.println(sqlStringStmt + ";");

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + stmt);
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		return false;
	}
}
