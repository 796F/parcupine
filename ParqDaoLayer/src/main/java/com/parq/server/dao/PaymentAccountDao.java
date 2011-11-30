package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.PaymentAccount;

public class PaymentAccountDao extends AbstractParqDaoParent {
	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "PaymentAccountCache";
	private static Cache myCache;

	private static final String sqlCreatePaymentAccount = 
		"INSERT INTO PaymentAccount (User_ID, Customer_ID, Payment_Method_ID, CC_stub, Is_Default_Payment) " + 
		"	VALUES (?, ?, ?, ?, ?)";
	private static final String sqlDeletePaymentAccountById =
		"UPDATE PaymentAccount SET Is_Deleted = TRUE WHERE Account_ID = ?";
	private static final String sqlGetAccountByUserId = 
		"SELECT Account_ID, User_ID, Customer_ID, Payment_Method_ID, CC_stub, Is_Default_Payment " +
		"	FROM PaymentAccount " +
		"	WHERE User_ID = ? " +
		" 	AND Is_Deleted IS NOT TRUE"; 
	private static final String sqlClearDefaultPaymentMethodByUserId = 
		"UPDATE PaymentAccount SET Is_Default_Payment = FALSE " +
		" 	WHERE User_ID = ?";
	private static final String sqlGetUserIdByAccountId = 
		"SELECT User_ID FROM PaymentAccount WHERE Account_ID = ?";
	

	private static final String paymentMethodsCache = "getAllPaymentMethodForUser:";

	public PaymentAccountDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}
	
	/**
	 * @param paymentRequest
	 * @return
	 */
	public boolean createNewPaymentMethod(PaymentAccount request) {
		
		if (request == null || request.getUserId() <= 0) {
			throw new IllegalStateException("Invalid PaymentAccount create request");
		}
		
		// clear out the cache entry for user that is going to be updated
		revokeCache(myCache, paymentMethodsCache, request.getUserId() + "");
		 
		if (request.isDefaultPaymentMethod()) {
			clearDefaultPayment(request.getUserId());
		}

		PreparedStatement pstmt = null;
		Connection con = null;
		boolean newPaymentAccountCreated = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlCreatePaymentAccount);
			pstmt.setInt(1, request.getUserId());
			pstmt.setString(2, request.getCustomerId());
			pstmt.setString(3, request.getPaymentMethodId());
			pstmt.setString(4, request.getCcStub());
			pstmt.setBoolean(5, request.isDefaultPaymentMethod());
			
			newPaymentAccountCreated = pstmt.executeUpdate() == 1;
	
		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return newPaymentAccountCreated;
	}
	
	private boolean clearDefaultPayment(int userId) {
	
		if (userId <= 0) {
			throw new IllegalStateException("Invalid ClearDefaultPayment request for userId:" + userId);
		}
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean updateSuccessful = false;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlClearDefaultPaymentMethodByUserId);
			pstmt.setInt(1, userId);
			updateSuccessful = pstmt.executeUpdate() >= 0;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return updateSuccessful;
	}

	public boolean deletePaymentMethod(int accountId) {

		if (accountId <= 0) {
			throw new IllegalStateException("Invalid PaymentAccount delete request");
		}
		// clear out the cache entry for user that is going to be updated
		int userId = getUserForPaymentAccountByAccountId(accountId);
		revokeCache(myCache, paymentMethodsCache, userId + "");
		
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean deleteSuccessful = false;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlDeletePaymentAccountById);
			pstmt.setInt(1, accountId);
			deleteSuccessful = pstmt.executeUpdate() > 0;

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return deleteSuccessful;
	}

	private int getUserForPaymentAccountByAccountId(int accountId) {
		
		int userId = -1;
		
		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetUserIdByAccountId);
			pstmt.setInt(1, accountId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.isBeforeFirst() && rs.next()) {
				userId = rs.getInt("User_ID");
			}

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}
		
		return userId;
	}

	@SuppressWarnings("unchecked")
	public List<PaymentAccount> getAllPaymentMethodForUser(int userId) {
		if (userId <= 0) {
			throw new IllegalStateException("Invalide UserId: " + userId);
		}
		
		// the cache key for this method call;
		String cacheKey = paymentMethodsCache + userId;

		List<PaymentAccount> paymentAccounts = null;
		if (myCache.get(cacheKey) != null) {
			paymentAccounts = (List<PaymentAccount>) myCache.get(cacheKey).getValue();
			return paymentAccounts;
		}

		// query the DB for the user object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetAccountByUserId);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			
			paymentAccounts = createPaymentAccountObject(rs);

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		myCache.put(new Element(cacheKey, paymentAccounts));
		
		return paymentAccounts;
	}

	private List<PaymentAccount> createPaymentAccountObject(ResultSet rs) throws SQLException {
		if (rs == null || !rs.isBeforeFirst()) {
			return Collections.emptyList();
		}
		
		List<PaymentAccount> paymentAccounts = new ArrayList<PaymentAccount>();
		
		while (rs.next()) {
			PaymentAccount pa = new PaymentAccount();
			int accountId = rs.getInt("Account_ID");
			int userId = rs.getInt("User_ID");
			String customerId = rs.getString("Customer_ID");
			String paymentMethodId = rs.getString("Payment_Method_ID");
			String ccStub = rs.getString("CC_stub");
			boolean isDefaultPaymentMethod = rs.getBoolean("Is_Default_Payment");
			
			pa.setAccountId(accountId);
			pa.setUserId(userId);
			pa.setCustomerId(customerId);
			pa.setPaymentMethodId(paymentMethodId);
			pa.setCcStub(ccStub);
			pa.setDefaultPaymentMethod(isDefaultPaymentMethod);
			
			paymentAccounts.add(pa);
		}
			
		return paymentAccounts;
	}
}
