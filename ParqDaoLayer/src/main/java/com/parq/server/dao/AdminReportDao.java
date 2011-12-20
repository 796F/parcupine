package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.ParkingLocationUsageReport;
import com.parq.server.dao.model.object.ParkingLocationUsageReport.ParkingLocationUsageEntry;
import com.parq.server.dao.model.object.UserPaymentReport;
import com.parq.server.dao.model.object.UserPaymentReport.UserPaymentEntry;

/**
 * @author GZ
 *
 */
public class AdminReportDao extends AbstractParqDaoParent {

	private static final String sqlParkingLocationUsageStastics =
		"SELECT pi.parkinginst_id, pi.user_id, pi.space_id, pi.park_began_time, " +
		"       pi.park_end_time, pi.is_paid_parking, pi.parkingrefnumber, " +
		"		l.location_id, l.location_name, l.location_identifier " +
		" FROM parkinginstance AS pi, parkingspace AS s, parkinglocation AS l" +
		" WHERE pi.space_id = s.space_id " +
		" AND s.location_id = l.location_id " +
		" AND l.location_id = ? " +
		" AND pi.park_began_time > ? " +
		" AND pi.park_began_time < ? " +
		" ORDER BY pi.park_began_time";
	
	private static final String sqlGetUserPaymentInfo = 
		"SELECT p.payment_id, p.payment_type, p.payment_ref_num, p.payment_datetime, " +
		"       p.amount_paid_cents, l.location_id, l.location_name, l.location_identifier " +
		" FROM payment AS p, paymentaccount AS pa, parkinginstance AS pi, " +
		"      parkingspace AS s, parkinglocation AS l, user u " +
		" WHERE u.user_id = pa.user_id " +
		" AND pa.account_id = p.account_id " +
		" AND p.parkinginst_id = pi.parkinginst_id " +
		" AND pi.space_id = s.space_id " +
		" AND s.location_id = l.location_id " +
		" AND u.user_id = ? " +
		" AND p.payment_datetime > ? " +
		" AND p.payment_datetime < ? " +
		" ORDER BY p.payment_datetime";
	
	/**
	 * Get the parking location usage report (in unformated data form) narrow by
	 * the reportStartData and reportEndDate for a specific ParkingLocation
	 * based on the locationId
	 * 
	 * @param locationId
	 * @param reportStartDate
	 * @param reportEndDate
	 * @return
	 */
	public ParkingLocationUsageReport getParkingLocationUsageReport(
			long locationId, Date reportStartDate, Date reportEndDate) {

		if (locationId <= 0 || reportStartDate == null || reportEndDate == null 
				|| !reportEndDate.after(reportStartDate)) {
			throw new IllegalStateException(
					"Invalid getParkingLocationUsageReport request loactionId: " + locationId + 
							" Report Start Date: " + reportStartDate + 
							" Report End Date: " + reportEndDate);
		}
		
		ParkingLocationUsageReport report = null;
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlParkingLocationUsageStastics);
			pstmt.setLong(1, locationId);
			pstmt.setTimestamp(2, new Timestamp(reportStartDate.getTime()));
			pstmt.setTimestamp(3, new Timestamp(reportEndDate.getTime()));

			// System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();

			report = createParkingLocationUsageReport(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return report;
	}

	private ParkingLocationUsageReport createParkingLocationUsageReport(
			ResultSet rs) throws SQLException {
		
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}
		ParkingLocationUsageReport report = new ParkingLocationUsageReport();
		while (rs.next()) {
			
			ParkingLocationUsageEntry entry = new ParkingLocationUsageEntry(); 
			entry.setParkingInstId(rs.getLong("parkinginst_id"));
			entry.setUserId(rs.getLong("user_id"));
			entry.setSpaceId(rs.getLong("space_id"));
			entry.setParkingBeganTime(rs.getTimestamp("park_began_time"));
			entry.setParkingEndTime(rs.getTimestamp("park_end_time"));
			entry.setParkingRefNumber(rs.getString("parkingrefnumber"));
			entry.setPaidParking(rs.getBoolean("is_paid_parking"));
			entry.setLocationId(rs.getLong("location_id"));
			entry.setLocationIdentifier(rs.getString("location_identifier"));
			entry.setLocationName(rs.getString("location_name"));
			
			report.addUsageReportEntry(entry);
		}
		return report;
	}

	/**
	 * Get the user parking payment report (in unformated data form) narrowed by
	 * the reportStartData and reportEndDate for a specific User
	 * based on the userId
	 * 
	 * @param userId
	 * @param reportStartDate
	 * @param reportEndDate
	 * @return
	 */
	public UserPaymentReport getUserPaymentReport(long userId, Date reportStartDate, Date reportEndDate) {
		if (userId <= 0 || reportStartDate == null || reportEndDate == null 
				|| !reportEndDate.after(reportStartDate)) {
			throw new IllegalStateException(
					"Invalid getUserPaymentReport request userId: " + userId + 
							" Report Start Date: " + reportStartDate + 
							" Report End Date: " + reportEndDate);
		}
		
		
		UserPaymentReport report = null;
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetUserPaymentInfo);
			pstmt.setLong(1, userId);
			pstmt.setTimestamp(2, new Timestamp(reportStartDate.getTime()));
			pstmt.setTimestamp(3, new Timestamp(reportEndDate.getTime()));
			
			// System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			report = createUserPaymentReport(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return report;
	}

	private UserPaymentReport createUserPaymentReport(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return null;
		}

		UserPaymentReport report = new UserPaymentReport();
		while (rs.next()) {
			
			UserPaymentEntry entry = new UserPaymentEntry(); 
			entry.setPaymentId(rs.getLong("payment_id"));
			entry.setPaymentType(Payment.PaymentType.valueOf(rs.getString("payment_type")));
			entry.setPaymentRefNumber(rs.getString("payment_ref_num"));
			entry.setPaymentDateTime(rs.getTimestamp("payment_datetime"));
			entry.setAmountPaidCents(rs.getInt("amount_paid_cents"));
			entry.setLocationId(rs.getLong("location_id"));
			entry.setLocationIdentifier(rs.getString("location_identifier"));
			entry.setLocationName(rs.getString("location_name"));
			
			report.addPaymentEntry(entry);
		}
		return report;
	}
}
