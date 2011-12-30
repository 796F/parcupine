package com.parq.server.dao.model.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GZ
 *
 */
public class Admin implements Serializable {
	
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -4671173403978454253L;
	private long adminId = -1;
	private String password;
	private String email;
	
	private List<ClientRelationShip> clientRelationships;
	
	public Admin() {
		clientRelationships = new ArrayList<ClientRelationShip>();
	}

	/**
	 * @return the adminId
	 */
	public long getAdminId() {
		return adminId;
	}

	/**
	 * @param adminId
	 *            the adminId to set
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
	 * @param password
	 *            the password to set
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
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the clientRelationships
	 */
	public List<ClientRelationShip> getClientRelationships() {
		return clientRelationships;
	}
}
