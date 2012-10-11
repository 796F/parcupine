package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.parq.server.dao.model.object.UserActionLog;
import com.parq.server.dao.model.object.UserSelfReporting;

public class MiscellaneousDao extends AbstractParqDaoParent {

	private static final String sqlUpdateCount = "INSERT INTO count (tempValue) VALUE ('')";
	private static final String sqlGetHighestCount = "SELECT count_id FROM count ORDER BY count_id DESC LIMIT 1";
	
	private static final String sqlInsertUserReport = "INSERT INTO userselfreporting (user_id, "
			+ " space1_id, space1_status, space2_id, space2_status, space3_id, space3_status, "
			+ " space4_id, space4_status, space5_id, space5_status, space6_id, space6_status, "
			+ " score1, score2, score3, score4, score5, score6)"
			+ " VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String sqlGetAllUserReport = "SELECT "
			+ " report_id, user_id, report_datetime, "
			+ " space1_id, space1_status, space1_id, space1_status, space2_id, space2_status, "
			+ " space3_id, space3_status, space4_id, space4_status, space5_id, space5_status, "
			+ " space6_id, space6_status,"
			+ " score1, score2, score3, score4, score5, score6"
			+ " FROM userselfreporting WHERE user_id = ? ORDER BY report_id DESC";
	
	private static final String sqlInsertUserActionLog = "INSERT INTO useractionlogs "
			+ "(user_id, log) VALUE (?, ?)";
	
	/**
	 * Get the next highest count number
	 * @return <B>long</B>
	 */
	public long getNextCount() {
		PreparedStatement pstmt = null;
		Connection con = null;
		long countId = 0;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlUpdateCount);
			if (pstmt.executeUpdate() == 1) {
				pstmt = con.prepareStatement(sqlGetHighestCount);
				ResultSet rs = pstmt.executeQuery();
				rs.first();
				countId = rs.getLong("count_id");
			}
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return countId;
	}
	
	public boolean insertUserSelfReporting(UserSelfReporting report) {
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean updateSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlInsertUserReport);
			
			pstmt.setLong(1, report.getUserId());
			
			for (int i = 0; i < 6; i++) {
				if (i < report.getSpaceIds().size()) {
					pstmt.setLong(i * 2 + 2, report.getSpaceIds().get(i));
					pstmt.setString(i * 2 + 3, report.getParkingSpaceStatus().get(i));
				} else {
					pstmt.setNull(i * 2 + 2, Types.BIGINT);
					pstmt.setNull(i * 2 + 3, Types.VARCHAR);
				}
			}
			pstmt.setInt(14, report.getScore1());
			pstmt.setInt(15, report.getScore2());
			pstmt.setInt(16, report.getScore3());
			pstmt.setInt(17, report.getScore4());
			pstmt.setInt(18, report.getScore5());
			pstmt.setInt(19, report.getScore6());
						
			updateSuccessful = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return updateSuccessful;
	}
	
	public List<UserSelfReporting> getUserSelfReportingHistoryForUser(long userId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		List<UserSelfReporting> reportHistory = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAllUserReport);
			pstmt.setLong(1, userId);
			ResultSet rs = pstmt.executeQuery();
			reportHistory = createUserReportingHistory(rs);
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return reportHistory;
	}

	private List<UserSelfReporting> createUserReportingHistory(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		List<UserSelfReporting> reportHistory = new ArrayList<UserSelfReporting>();
		while (rs.next()) {
			UserSelfReporting report = new UserSelfReporting();
			report.setReportId(rs.getLong("report_id"));
			report.setUserId(rs.getLong("user_id"));
			report.setSpaceIds(new ArrayList<Long>());
			report.setParkingSpaceStatus(new ArrayList<String>());
			for (int i = 1; i < 7; i++) {
				report.getSpaceIds().add(rs.getLong("space" + i + "_id"));
				report.getParkingSpaceStatus().add(rs.getString("space" + i + "_status"));
			}
			report.setReportDateTime(rs.getTimestamp("report_datetime"));
			report.setScore1(rs.getInt("score1"));
			report.setScore2(rs.getInt("score2"));
			report.setScore3(rs.getInt("score3"));
			report.setScore4(rs.getInt("score4"));
			report.setScore5(rs.getInt("score5"));
			report.setScore6(rs.getInt("score6"));
			reportHistory.add(report);
		}
		return reportHistory;
	}
	
	public boolean insertUserActionLogging(UserActionLog userLog) {
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean insertSuccessful = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlInsertUserActionLog);
			pstmt.setLong(1, userLog.getUserId());
			pstmt.setString(2, userLog.getLog());
			insertSuccessful = pstmt.executeUpdate() == 1;
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return insertSuccessful;
	}
	
}
