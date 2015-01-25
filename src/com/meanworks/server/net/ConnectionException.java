/**
 * 
 */
package com.meanworks.server.net;

/**
 * @author Meanz
 * 
 */
public class ConnectionException extends Exception {

	/*
	 * 
	 */
	private static final long serialVersionUID = 7447983081715534938L;

	/**
	 * 
	 */
	public ConnectionException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public ConnectionException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public ConnectionException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * 
	 * @param exception
	 */
	public ConnectionException(Exception exception) {
		super(exception);
	}

}
