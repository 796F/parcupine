package com.parq.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.parq.server.dao.model.object.PaymentMethod;

public class PaymentMethodDao extends AbstractParqDaoParent {

	/**
	 * Name of the local cache use by this dao
	 */
	private static final String cacheName = "PaymentMethodCache";
	private static Cache myCache;
	
	private static final String sqlGetPaymentMethod = 
	"SELECT c.payment_method " +
	" FROM client AS c, parkingspace AS ps, parkinglocation AS pl " +
	" WHERE ps.location_id = pl.location_id " +
	" AND c.client_id = c.client_id " + 
	" AND ps.space_id = ? ";
	
	private static final String paymentMethodCache = "getPaymentMethodBySpotId:";
	
	public PaymentMethodDao() {
		super();
		if (myCache == null) {
			// create the cache.
			myCache = setupCache(cacheName);
		}
	}
	
	/**
	 * Get the payment method use by the client that own this parking spot. 
	 * Currently the possible return values are:
	 * PaymentMethod.NORMAL - User is charged per parking payment
	 * PaymentMethod.BATCH - User parking fees are batch together in a once every 2 week charge
	 * PaymentMethod.PREFILL - User have credits on account, 
	 * 						and will be using credit to pay for the parking.
	 * 
	 * @param spotId
	 * @return
	 */
	public PaymentMethod getPaymentMethodBySpotId(long spotId) {
		// the cache key for this method call;
		String cacheKey = paymentMethodCache + spotId;
		
		PaymentMethod method = null;
		Element cacheEntry = myCache.get(cacheKey); 
		if (cacheEntry  != null) {
			method = (PaymentMethod) cacheEntry.getValue();
			return method;
		}

		// query the DB for the Admin object
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sqlGetPaymentMethod);
			pstmt.setLong(1, spotId);
			ResultSet rs = pstmt.executeQuery();

			// if no result from the paymentmethod table, then the payment method is normal
			if (!rs.first()) {
				method = PaymentMethod.NORMAL;
			}
			else {
				method = PaymentMethod.valueOf(rs.getString("payment_method").toUpperCase());
			}

		} catch (SQLException sqle) {
			System.out.println("SQL statement is invalid: " + pstmt);
			sqle.printStackTrace();
			throw new RuntimeException(sqle);
		} finally {
			closeConnection(con);
		}

		// put result into cache
		if (method != null) {
			// only put none null value into cache
			myCache.put(new Element(cacheKey, method));
		}
		
		return method;
	}
}
