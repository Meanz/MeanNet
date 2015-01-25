/**
 * 
 */
package com.meanworks.server.net;

/**
 * @author Meanz
 * 
 */
public class DecodingException extends Exception {

	/*
	 * 
	 */
	private static final long serialVersionUID = -487788360855929985L;

	/**
	 * 
	 */
	public DecodingException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public DecodingException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param exception
	 */
	public DecodingException(Exception exception) {
		super(exception);
	}

}
