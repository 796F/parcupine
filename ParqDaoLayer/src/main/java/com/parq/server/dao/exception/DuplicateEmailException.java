package com.parq.server.dao.exception;

/**
 * Runtime Typed exception that signified that during user creation or update
 * method, the email already exist in the system
 * 
 * @author GZ
 * 
 */
public class DuplicateEmailException extends IllegalStateException {

	public DuplicateEmailException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6331691369057723036L;

}
