/**
 * 
 */
package com.meanworks.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.meanworks.server.net.ConnectionException;
import com.meanworks.server.net.ConnectionHandler;

/**
 * @author Meanz
 * 
 */
public class ServerListener {

	/**
	 * The holy grail of this system
	 */
	private ServerSocketChannel serverSocket;

	/**
	 * The port for this listener
	 */
	private int port;

	/**
	 * The thread this server is listening on
	 */
	private Thread listeningThread;

	/**
	 * Whether or not this server is listening
	 */
	private boolean listening;

	/**
	 * A callback so one can receive information about the listener thread
	 */
	private ServerListenerCallback listenerCallback;

	/**
	 * If synchronization is needed we can use this
	 */
	private Object syncObj;

	/**
	 * The connection handler for this listener
	 */
	private ConnectionHandler connectionHandler;

	/**
	 * The server handler for this listener
	 */
	private ServerHandler serverHandler;

	/**
	 * The logger for this server
	 */
	private ServerLogger logger = new ServerLogger("ServerListener");

	/**
	 * Construct a new Server
	 * 
	 * @param port
	 * @throws ServerException
	 */
	public ServerListener(int port) throws ServerException {
		this.port = port;
		syncObj = new Object(); // Not used yet

		// Attempt to open the socket
		try {
			serverSocket = ServerSocketChannel.open();
		} catch (IOException iex) {
			logger.error("Could not create server socket " + iex.getMessage());
			throw new ServerException("Could not create server socket ",
					iex.getCause());
		}
	}

	/**
	 * Set the listener callback for this listener
	 * 
	 * @param slc
	 */
	public void setListenerCallback(ServerListenerCallback slc) {
		this.listenerCallback = slc;
	}

	/**
	 * Set the server handler for this listener
	 * 
	 * @param serverHandler
	 */
	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	/**
	 * Get the server handler for this listener
	 * 
	 * @return
	 */
	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	/**
	 * Get the connection handler for this listener
	 * 
	 * @return
	 */
	public ConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}

	/**
	 * Set the connection handler for this server
	 * 
	 * @param handler
	 */
	public void setConnectionHandler(ConnectionHandler handler) {
		this.connectionHandler = handler;
	}

	/**
	 * Starts listening on this server
	 * 
	 * @param createDaemonThread
	 * @throws ServerException
	 */
	public void startListening(boolean createDaemonThread,
			final ServerListenerCallback slc) throws ServerException {
		this.listenerCallback = slc;
		if (connectionHandler == null) {
			logger.error("No connection Handler is set");
			throw new ServerException("No Connection Handler is set.");
		}

		// Try binding
		try {
			serverSocket.socket().bind(new InetSocketAddress(port));
			serverSocket.configureBlocking(true);
			logger.debug("Bound to port " + port);
		} catch (IOException iex) {
			logger.error("Could not bind to port");
			throw new ServerException("Could not bind to port " + port,
					iex.getCause());
		}

		listening = true;
		if (createDaemonThread) {
			listeningThread = new Thread("ServerSocket Listener Daemon Thread") {
				public void run() {
					try {
						if (listenerCallback != null) {
							listenerCallback.onListeningStart();
						}
						internalListen();
					} catch (ServerException se) {
						// ???
						se.printStackTrace();
					}
					if (listenerCallback != null) {
						listenerCallback.onListeningStop();
					}
				}
			};
			listeningThread.setDaemon(true);
			listeningThread.start();
		} else {
			if (listenerCallback != null) {
				listenerCallback.onListeningStart();
			}
			listeningThread = Thread.currentThread();
			internalListen();
			if (listenerCallback != null) {
				listenerCallback.onListeningStop();
			}
		}
	}

	/**
	 * Starts listening on this server
	 * 
	 * @param createDaemonThread
	 */
	public void startListening(boolean createDaemonThread)
			throws ServerException {
		startListening(createDaemonThread, listenerCallback);
	}

	/**
	 * Interrupts the listening thread
	 */
	public void stopListening() {
		listening = false;
		listeningThread.interrupt();
	}

	/**
	 * Listening function
	 * 
	 * @throws ServerException
	 */
	private void internalListen() throws ServerException {
		while (listening) {
			try {
				try {
					try {
						SocketChannel socket = serverSocket.accept();
						if (socket != null) {
							socket.configureBlocking(false);
							getServerHandler().onConnection(socket,
									connectionHandler);
						}
					} catch (IOException iex) {
						iex.printStackTrace();
					}
				} catch (ConnectionException ie) {
					// This means we are stopping the listening
					ie.printStackTrace();
					throw new ServerException(ie);
				}
			} catch (ServerException se) {
				// This is serious enough to stop listening
				se.printStackTrace();
				throw new ServerException(se);
			}
		}
	}
}
