package com.parq.server.dao.model.object;

import java.io.Serializable;

/**
 * @author GZ
 *
 */
public class ClientRelationShip implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -40320537616098840L;
	private long clientId = -1;
	private long roleId = -1;
	private long adminId = -1;
	private long relationShipId = -1;

	/**
	 * @return the clientId
	 */
	public long getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
	 * @return the relationShipId
	 */
	public long getRelationShipId() {
		return relationShipId;
	}

	/**
	 * @param relationShipId
	 *            the relationShipId to set
	 */
	public void setRelationShipId(long relationShipId) {
		this.relationShipId = relationShipId;
	}
}
