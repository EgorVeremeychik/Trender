package com.trender.solr.exception;

public class SolrException extends Exception {

	private static final long serialVersionUID = -6456501015920578270L;

	public SolrException() {}

	public SolrException(String msg) {
		super(msg);
	}

	public SolrException(Throwable cause) {
		super(cause);
	}

	public SolrException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
