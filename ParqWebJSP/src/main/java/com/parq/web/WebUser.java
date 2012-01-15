package com.parq.web;

public class WebUser {
	private boolean isAuthenticated = false;
	private boolean isAdminUser = false;
	private String username;
	private String password;
	private boolean loginFailed = false;
	private long id = -1;

	/**
	 * @return the isAuthenticated
	 */
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	/**
	 * @param isAuthenticated
	 *            the isAuthenticated to set
	 */
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	/**
	 * @return the isAdminUser
	 */
	public boolean isAdminUser() {
		return isAdminUser;
	}

	/**
	 * @param isAdminUser
	 *            the isAdminUser to set
	 */
	public void setAdminUser(boolean isAdminUser) {
		this.isAdminUser = isAdminUser;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the loginFailed
	 */
	public boolean getLoginFailed() {
		return loginFailed;
	}

	/**
	 * @param loginFailed the loginFailed to set
	 */
	public void setLoginFailed(boolean loginFailed) {
		this.loginFailed = loginFailed;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

}
