package com.parq.server.dao.model.object;

import java.io.Serializable;

public class UserPrePaidAccountBalance implements Serializable{

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = -5534237804879231485L;
	private long userId = -1;
	private int accountBalance = Integer.MIN_VALUE;
	
	
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the accountBalancce
	 */
	public int getAccountBalance() {
		return accountBalance;
	}
	/**
	 * @param accountBalancce the accountBalancce to set
	 */
	public void setAccountBalance(int accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	
}
