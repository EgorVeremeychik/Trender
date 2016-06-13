package com.trender.dao.exception;

public class DAOException extends Exception {

	private static final long serialVersionUID = -6456501015920578270L;

	public DAOException() {}

	public DAOException(String msg) {
		super(msg);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

	public DAOException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
