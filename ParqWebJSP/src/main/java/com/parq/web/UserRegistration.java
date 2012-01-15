package com.parq.web;

public class UserRegistration {
	private boolean emailAlreadyExist = false;
	private boolean invalidPassword = false;
	private boolean passwordDoesNotMatch = false;
	
	private String email;
	private String password1;
	private String password2;
	
	
	/**
	 * @return the emailAlreadyExist
	 */
	public boolean isEmailAlreadyExist() {
		return emailAlreadyExist;
	}
	/**
	 * @param emailAlreadyExist the emailAlreadyExist to set
	 */
	public void setEmailAlreadyExist(boolean emailAlreadyExist) {
		this.emailAlreadyExist = emailAlreadyExist;
	}
	/**
	 * @return the invalidPassword
	 */
	public boolean isInvalidPassword() {
		return invalidPassword;
	}
	/**
	 * @param invalidPassword the invalidPassword to set
	 */
	public void setInvalidPassword(boolean invalidPassword) {
		this.invalidPassword = invalidPassword;
	}
	/**
	 * @return the passwordDoesNotMatch
	 */
	public boolean isPasswordDoesNotMatch() {
		return passwordDoesNotMatch;
	}
	/**
	 * @param passwordDoesNotMatch the passwordDoesNotMatch to set
	 */
	public void setPasswordDoesNotMatch(boolean passwordDoesNotMatch) {
		this.passwordDoesNotMatch = passwordDoesNotMatch;
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
	 * @return the password1
	 */
	public String getPassword1() {
		return password1;
	}
	/**
	 * @param password1 the password1 to set
	 */
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	/**
	 * @return the password2
	 */
	public String getPassword2() {
		return password2;
	}
	/**
	 * @param password2 the password2 to set
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	
}
