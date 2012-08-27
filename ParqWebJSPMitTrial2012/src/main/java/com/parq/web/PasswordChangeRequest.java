package com.parq.web;

public class PasswordChangeRequest {
	private String oldPassword;
	private String newPassword1;
	private String newPassword2;

	private boolean invalidPassword = false;
	private boolean wrongOldPassword = false;
	private boolean newPasswordNotMatch = false;

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword1
	 */
	public String getNewPassword1() {
		return newPassword1;
	}

	/**
	 * @param newPassword1
	 *            the newPassword1 to set
	 */
	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	/**
	 * @return the newPassword2
	 */
	public String getNewPassword2() {
		return newPassword2;
	}

	/**
	 * @param newPassword2
	 *            the newPassword2 to set
	 */
	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	/**
	 * @return the invalidPassword
	 */
	public boolean isInvalidPassword() {
		return invalidPassword;
	}

	/**
	 * @param invalidPassword
	 *            the invalidPassword to set
	 */
	public void setInvalidPassword(boolean invalidPassword) {
		this.invalidPassword = invalidPassword;
	}

	/**
	 * @return the wrongOldPassword
	 */
	public boolean isWrongOldPassword() {
		return wrongOldPassword;
	}

	/**
	 * @param wrongOldPassword
	 *            the wrongOldPassword to set
	 */
	public void setWrongOldPassword(boolean wrongOldPassword) {
		this.wrongOldPassword = wrongOldPassword;
	}

	/**
	 * @return the newPasswordNotMatch
	 */
	public boolean isNewPasswordNotMatch() {
		return newPasswordNotMatch;
	}

	/**
	 * @param newPasswordNotMatch
	 *            the newPasswordNotMatch to set
	 */
	public void setNewPasswordNotMatch(boolean newPasswordNotMatch) {
		this.newPasswordNotMatch = newPasswordNotMatch;
	}

}
