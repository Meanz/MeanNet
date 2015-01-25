/**
 * 
 */
package com.meanworks.server;

/**
 * @author Meanz
 * 
 */
public class ServerException extends Exception {

	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ServerException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public ServerException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 
	 * @param exception
	 */
	public ServerException(Exception exception) {
		super(exception);
	}

}
