package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.PendingCharge;
import com.parq.server.dao.model.object.Payment.PaymentType;

public class BatchCCProcessingDao extends AbstractParqDaoParent {

	private static final String sqlGetPendingCharges = "SELECT p.payment_id, p.parkinginst_id, p.payment_type, p.account_id, p.payment_ref_num, "
			+ "		p.payment_datetime, p.amount_paid_cents, a.user_id, a.customer_id, a.payment_method_id, "
			+ "		a.cc_stub, a.is_default_payment "
			+ " FROM payment AS p, paymentaccount AS a "
			+ " WHERE p.account_id = a.account_id "
			+ " AND p.payment_ref_num = ? "
			+ " AND p.payment_datetime > ? "
			+ " AND p.payment_datetime < ? ";

	private static final String sqlUpdatePaymentInfo = "UPDATE payment " +
			" SET payment_ref_num = ?, amount_paid_cents = ? " +
			" WHERE payment_id = ? ";

	/**
	 * Get all the pending charges that still need to be proceeded through the
	 * CC payment company
	 * 
	 * @param ccToken
	 *            the string token placeholder that signified the payment has
	 *            not be proceeded yet
	 * @param ccChargeStartDate
	 *            the began date to search for the payment to be proceeded
	 * @param ccChargeEndDate
	 *            the end date to search for the payment to be processed
	 * @return <code>List</code> of <code>PendingCharges</code> that need to be
	 *         proceeded through the CC payment company
	 */
	public Map<PaymentAccount, List<PendingCharge>> getPendingCharge(
			String ccToken, Date ccChargeStartDate, Date ccChargeEndDate) {

		if (ccToken == null || ccToken.isEmpty() || ccChargeStartDate == null
				|| ccChargeEndDate == null
				|| !ccChargeEndDate.after(ccChargeStartDate)) {
			throw new IllegalStateException(
					"Invalid getPendingCharge request. ccToken: " + ccToken
							+ " Charge Start Date: " + ccChargeStartDate
							+ " Charge End Date: " + ccChargeEndDate);
		}

		Map<PaymentAccount, List<PendingCharge>> pendingChargesMap = null;
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetPendingCharges);
			pstmt.setString(1, ccToken);
			pstmt.setTimestamp(2, new Timestamp(ccChargeStartDate.getTime()));
			pstmt.setTimestamp(3, new Timestamp(ccChargeEndDate.getTime()));

			// System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			List<PendingCharge> pendingCharges = createPendingChargesList(rs);
			pendingChargesMap = createPendingChargesMap(pendingCharges);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		return pendingChargesMap;
	}

	private List<PendingCharge> createPendingChargesList(ResultSet rs)
			throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}

		List<PendingCharge> pendingCharges = new ArrayList<PendingCharge>();

		while (rs.next()) {
			PendingCharge charge = new PendingCharge();
			charge.setAmountPaid(rs.getInt("amount_paid_cents"));
			charge.setParkingInstId(rs.getLong("parkinginst_id"));
			charge.setPaymentDateTime(rs.getDate("payment_datetime"));
			charge.setPaymentId(rs.getLong("payment_id"));
			charge.setPaymentRefNumber(rs.getString("payment_ref_num"));
			charge.setPaymentType(PaymentType.valueOf(rs
					.getString("payment_type")));

			PaymentAccount account = new PaymentAccount();
			account.setAccountId(rs.getLong("account_id"));
			account.setCcStub(rs.getString("cc_stub"));
			account.setCustomerId(rs.getString("customer_id"));
			account
					.setDefaultPaymentMethod(rs
							.getBoolean("is_default_payment"));
			account.setPaymentMethodId(rs.getString("payment_method_id"));
			account.setUserId(rs.getLong("user_id"));

			charge.setPaymentAccount(account);
			pendingCharges.add(charge);
		}
		return pendingCharges;
	}

	/**
	 * Create a
	 * 
	 * @param pendingCharges
	 * @return
	 */
	private Map<PaymentAccount, List<PendingCharge>> createPendingChargesMap(
			List<PendingCharge> pendingCharges) {

		Map<PaymentAccount, List<PendingCharge>> pendingChargesMap = new HashMap<PaymentAccount, List<PendingCharge>>();

		for (int i = 0; i < pendingCharges.size(); i++) {
			PendingCharge curCharge = pendingCharges.get(i);
			PaymentAccount key = curCharge.getPaymentAccount();

			if (!pendingChargesMap.containsKey(key)) {
				pendingChargesMap.put(key, new ArrayList<PendingCharge>());
			}
			List<PendingCharge> accountChargesList = pendingChargesMap.get(key);
			accountChargesList.add(curCharge);
		}
		return pendingChargesMap;
	}

	/**
	 * Once the credit card batch payment processing is finished, update the
	 * payment table with the unique Payment_Ref_Number. This method support
	 * batch processing that take a list <code>PendingCharge</code> to update
	 * the payment table.
	 * 
	 * @param chargesToUpdate
	 *            <code>List</code> of <code>PendingCharge</code>
	 * @return <code>true</code> if all the update was processed successfully,
	 *         if any of the payment did not process correct <code>false</code>
	 *         is returned.
	 */
	public boolean batchUpdatePaymentInfo(List<PendingCharge> chargesToUpdate) {
		if (chargesToUpdate == null || chargesToUpdate.isEmpty()) {
			throw new IllegalStateException(
					"Invalid batchUpdatePaymentInfo request. chargesToUpdate: "
							+ chargesToUpdate);
		}
		
		validateBatchPaymentUpdate(chargesToUpdate);

		boolean allUpdateSuccessful = true;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();

			for (int i = 0; i < chargesToUpdate.size(); i++) {

				PendingCharge charge = chargesToUpdate.get(i);

				pstmt = con.prepareStatement(sqlUpdatePaymentInfo);
				pstmt.setString(1, charge.getPaymentRefNumber());
				pstmt.setInt(2, charge.getAmountPaid());
				pstmt.setLong(3, charge.getPaymentId());
				allUpdateSuccessful = allUpdateSuccessful
						&& pstmt.executeUpdate() > 0;
			}

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		return allUpdateSuccessful;
	}

	private void validateBatchPaymentUpdate(List<PendingCharge> chargesToUpdate) {
		for (PendingCharge pc: chargesToUpdate) {
			if (pc.getPaymentRefNumber() == null || pc.getPaymentRefNumber().isEmpty()) {
				throw new RuntimeException("Update Charges must have a valid new payment reference number");
			}
			else if (pc.getPaymentId() <= 0 ) {
				throw new RuntimeException("Update Charges must have a valid payment id");
			}
			else if (pc.getAmountPaid() < 0) {
				throw new RuntimeException("Update Charges cannot have amount paid less then 0");
			}
		}
		
	}
}
