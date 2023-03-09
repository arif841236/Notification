package com.template.exception;

/**
 * This class for exception and its extends to runtime exception
 */
public class NotificationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotificationException() {
	}

	public NotificationException(String msg) {
		super(msg);
	}
}
