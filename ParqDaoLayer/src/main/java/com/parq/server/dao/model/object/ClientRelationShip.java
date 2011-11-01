package com.parq.server.dao.model.object;

import java.io.Serializable;

public class ClientRelationShip implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -40320537616098840L;
	private int clientId = -1;
	private int roleId = -1;
	private int adminId = -1;
	private int relationShipId = -1;

	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the roleId
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the adminId
	 */
	public int getAdminId() {
		return adminId;
	}

	/**
	 * @param adminId
	 *            the adminId to set
	 */
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	/**
	 * @return the relationShipId
	 */
	public int getRelationShipId() {
		return relationShipId;
	}

	/**
	 * @param relationShipId
	 *            the relationShipId to set
	 */
	public void setRelationShipId(int relationShipId) {
		this.relationShipId = relationShipId;
	}
}
