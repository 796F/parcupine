package com.parq.server.dao.exception;

public class DuplicateEmailException extends IllegalStateException {

	public DuplicateEmailException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6331691369057723036L;

}
