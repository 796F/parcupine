package com.parq.server.dao.model.object;

import java.io.Serializable;

/**
 * @author GZ
 *
 */
public class Admin implements Serializable {
	
	/**
	 * Auto generated Serial ID
	 */
	private static final long serialVersionUID = 3904655145858653904L;
	private long adminId = -1;
	private String password;
	private String email;
	private long clientId = -1;
	private AdminRole adminRole;
	
	
	/**
	 * @return the adminId
	 */
	public long getAdminId() {
		return adminId;
	}
	/**
	 * @param adminId the adminId to set
	 */
	public void setAdminId(long adminId) {
		this.adminId = adminId;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the clientId
	 */
	public long getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return the adminRole
	 */
	public AdminRole getAdminRole() {
		return adminRole;
	}
	/**
	 * @param adminRole the adminRole to set
	 */
	public void setAdminRole(AdminRole adminRole) {
		this.adminRole = adminRole;
	}


}
