package com.trender.dao.exception;

public class DaoException extends Exception {

	private static final long serialVersionUID = -6456501015920578270L;

	public DaoException() {}

	public DaoException(String msg) {
		super(msg);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
