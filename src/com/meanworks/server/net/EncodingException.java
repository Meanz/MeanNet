/**
 * 
 */
package com.meanworks.server.net;

/**
 * @author Meanz
 * 
 */
public class EncodingException extends Exception {

	/*
	 * 
	 */
	private static final long serialVersionUID = 6387298367872258136L;

	/**
	 * 
	 */
	public EncodingException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public EncodingException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param exception
	 */
	public EncodingException(Exception exception) {
		super(exception);
	}

}
