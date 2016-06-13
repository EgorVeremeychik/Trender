package com.trender.service.exception;

/**
 * Created by EgorVeremeychik on 14.06.2016.
 */
public class ServiceException extends Exception {
    private static final long serialVersionUID = -6456345433455958270L;

    public ServiceException() {}

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
