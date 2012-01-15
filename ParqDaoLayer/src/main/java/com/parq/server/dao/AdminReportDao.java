package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
		"		l.location_id, l.location_name, l.location_identifier, u.email " +
		" FROM parkinginstance AS pi, parkingspace AS s, parkinglocation AS l, user AS u" +
		" WHERE pi.space_id = s.space_id " +
		" AND s.location_id = l.location_id " +
		" AND u.user_id = pi.user_id " +
		" AND l.location_id = ? " +
		" AND pi.park_began_time > ? " +
		" AND pi.park_began_time < ? " +
		" ORDER BY pi.park_began_time";
	
	private static final String sqlGetUserPaymentInfo = 
		"SELECT p.payment_id, p.payment_type, p.payment_ref_num, p.payment_datetime, " +
		"       p.amount_paid_cents, l.location_id, l.location_name, l.location_identifier," +
		"		pi.park_began_time, pi.park_end_time, pi.parkingrefnumber " +
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
			entry.setUserEmail(rs.getString("email"));
			
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
			entry.setParkingBeganTime(rs.getTimestamp("park_began_time"));
			entry.setParkingEndTime(rs.getTimestamp("park_end_time"));
			entry.setParkingRefNumber(rs.getString("parkingrefnumber"));
			
			report.addPaymentEntry(entry);
		}
		return report;
	}
	
	/**
	 * Get a report of the user parking activities. Unlike the
	 * <code>getUserPaymentReport</code> method, this method put all the parking
	 * payment and refill payment together into one entry
	 * 
	 * @param userId
	 * @param reportStartDate
	 * @param reportEndDate
	 * @return
	 */
	public UserPaymentReport getConsolidatedUserParkingReport(long userId, Date reportStartDate, Date reportEndDate) {
		UserPaymentReport report = getUserPaymentReport(userId, reportStartDate, reportEndDate);
		UserPaymentReport consolidatedReport = consolidateUserPaymentReport(report);
		return consolidatedReport;
	}
	

	private UserPaymentReport consolidateUserPaymentReport(
			UserPaymentReport report) {
		
		// only consolidate payment report if there are payment entries
		if (report == null || report.getPaymentEntries() == null || report.getPaymentEntries().isEmpty()) {
			return report;
		}
		
		// sore the paymentEntries, so that all the parkingRefNum entries are group together
		// and of the group of parkingRefNum, the earliest start date come first.
		List<UserPaymentEntry> paymentEntries = report.getPaymentEntries();
		Collections.sort(paymentEntries, new PaymentEntryComparitor());
		
		List<UserPaymentEntry> consolidatedPayments = new ArrayList<UserPaymentReport.UserPaymentEntry>();
		
		// set the newEntry to be the first entry in the payment report
		UserPaymentEntry newEntry = paymentEntries.get(0);
		for (int i = 1; i < paymentEntries.size(); i++) {
			UserPaymentEntry curEntry = paymentEntries.get(i);
			
			// if the newEntry (last payment entry) equals the curEntry's parking reference number, it mean that
			// the curEntry is a refill of the newEntry, so we sum up the amount paid for parking
			// and send the refill's parking end date as the new parking end date for the newEntry
			if (newEntry.getParkingRefNumber().equals(curEntry.getParkingRefNumber())) {
				newEntry.setParkingEndTime(curEntry.getParkingEndTime());
				newEntry.setAmountPaidCents(newEntry.getAmountPaidCents() + curEntry.getAmountPaidCents());
				newEntry.setPaymentId(-1);
				newEntry.setPaymentRefNumber(null);
			}
			// if the newEntry and the curEntry parking reference number is different, then we are dealing
			// with 2 different parking instance, so we added the current newEntry into the consolidated payment
			// list, and start a new newEntry (with curEntry as the newEntry's value)
			else {
				consolidatedPayments.add(newEntry);
				newEntry = curEntry;
			}
		}
		// picket fence problem, add the last instance to the list.
		if (newEntry != null) {
			consolidatedPayments.add(newEntry);
		}
		
		// we first empty the report's payment entries.
		report.getPaymentEntries().clear();
		// then we add all the consolidated entry into the report's entry
		report.getPaymentEntries().addAll(consolidatedPayments);
		return report;
	}


	
	
	
	
	
	
	
	// compare 2 payment entry by payment reference number, then by parking start date
	private class PaymentEntryComparitor implements Comparator<UserPaymentEntry>{
		@Override
		public int compare(UserPaymentEntry o1, UserPaymentEntry o2) {
			int compareVal = o1.getParkingRefNumber().compareTo(o2.getParkingRefNumber());
			if (compareVal == 0) {
				compareVal = o1.getParkingBeganTime().compareTo(o2.getParkingBeganTime());
			}
			return compareVal;
		}
	}
}
