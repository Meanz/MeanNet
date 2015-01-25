/**
 * 
 */
package com.meanworks.server;

/**
 * @author Meanz
 * 
 */
public interface ServerListenerCallback {

	/**
	 * Called right before serverSocket.accept() is called
	 */
	public void onListeningStart();

	/**
	 * Called when the listening thread is done
	 */
	public void onListeningStop();

}
