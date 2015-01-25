/**
 * 
 */
package com.meanworks.server.test;

import com.meanworks.server.ServerException;
import com.meanworks.server.ServerHandler;
import com.meanworks.server.ServerListener;
import com.meanworks.server.ServerListenerCallback;
import com.meanworks.server.ServerLogger;

/**
 * @author Meanz
 * 
 */
public class TestServer {

	private ServerHandler server;

	public TestServer() {
		try {
			server = new ServerHandler();
			ServerListener listener = new ServerListener(5554);
			listener.setConnectionHandler(new TestConnectionHandler());
			listener.setListenerCallback(new ServerListenerCallback() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.meanworks.server.ServerListenerCallback#onListeningStart
				 * ()
				 */
				@Override
				public void onListeningStart() {
					// TODO Auto-generated method stub
					ServerLogger.getDefault().debug("Server Started on port " + 5554 + "...");
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.meanworks.server.ServerListenerCallback#onListeningStop()
				 */
				@Override
				public void onListeningStop() {
					// TODO Auto-generated method stub
					ServerLogger.getDefault().debug("Server Stopped...");
				}

			});
			server.addListener(listener);
			server.start(); // Starts the server
		} catch (ServerException se) {
			se.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TestServer();
	}

}
