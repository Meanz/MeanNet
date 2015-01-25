/**
 * 
 */
package com.meanworks.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import com.meanworks.server.net.Connection;
import com.meanworks.server.net.ConnectionException;
import com.meanworks.server.net.ConnectionHandler;
import com.meanworks.server.net.DecodingException;
import com.meanworks.server.net.EncodingException;
import com.meanworks.server.net.IPacketDecoder;
import com.meanworks.server.net.IPacketEncoder;
import com.meanworks.server.net.Packet;
import com.meanworks.server.net.impl.SimplePacketDecoder;
import com.meanworks.server.net.impl.SimplePacketEncoder;

/**
 * @author Meanz
 * 
 */
public class ServerHandler {

	/**
	 * The server handler
	 */
	private List<ServerListener> listeners;

	/**
	 * The strategy for this server
	 */
	private ServerStrategy strategy;

	/**
	 * The synchronization object for this server handler
	 */
	private Object syncObj;

	/**
	 * Wether this server handler is running or not
	 */
	private boolean running;

	/**
	 * The logger for this server
	 */
	private ServerLogger logger = new ServerLogger("ServerHandler");

	/**
	 * The default decoder
	 */
	private IPacketDecoder defaultDecoder = new SimplePacketDecoder();

	/**
	 * The default encoder
	 */
	private IPacketEncoder defaultEncoder = new SimplePacketEncoder();

	/**
	 * Construct a new ServerHandler
	 */
	public ServerHandler() {
		listeners = new LinkedList<ServerListener>();
		strategy = ServerStrategy.WORKER_THREADS; // Default to this one
		syncObj = new Object();
		running = false;
	}

	/**
	 * Set the default decoder for this server handler
	 * 
	 * @param decoder
	 */
	public void setDefaultDecoder(IPacketDecoder decoder) {
		this.defaultDecoder = decoder;
	}

	/**
	 * Set the default encoder for this server handler
	 * 
	 * @param encoder
	 */
	public void setDefaultEncoder(IPacketEncoder encoder) {
		this.defaultEncoder = encoder;
	}

	/**
	 * Get whether this server handler is running or not
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Add a listener to this server handler
	 * 
	 * @param sl
	 */
	public void addListener(ServerListener sl) {
		if (sl == null) {
			throw new RuntimeException(
					"Tried to add null listener to server handler");
		}
		synchronized (syncObj) {
			sl.setServerHandler(this);
			listeners.add(sl);
		}
	}

	/**
	 * 
	 * @param channel
	 * @throws IOException
	 * @throws ConnectionException
	 */
	public void onConnection(SocketChannel channel, ConnectionHandler handler)
			throws ConnectionException, IOException {
		synchronized (syncObj) {
			/*
			 * if (!handler.canConnect(channel.socket().getInetAddress())) {
			 * channel.close(); return; }
			 */
			Connection connection = new Connection(channel);

			handler.addConnection(connection);

			// Push the connection event to the connection handler
			handler.onConnected(connection);
		}
	}

	/**
	 * 
	 * @throws ServerException
	 */
	public void onDisconnect(Connection connection, ConnectionHandler handler)
			throws ConnectionException {
		if (handler == null) {
			logger.error("ConnectionHandler is null");
			throw new ConnectionException("null handler");
		}
		if (connection == null) {
			// ERM
			logger.error("Connection is null");
			throw new ConnectionException("null connection");
		} else {
			handler.onDisconnect(connection);
			handler.removeConnection(connection);
		}

	}

	/**
	 * 
	 * @param connection
	 * @param packet
	 * @throws ConnectionException
	 */
	public void onPacket(Connection connection, ConnectionHandler handler,
			Packet packet) throws ConnectionException {
		if (handler == null) {
			logger.error("ConnectionHandler is null");
			throw new ConnectionException("null handler");
		}
		if (connection == null) {
			// ERM
			logger.error("Connection is null");
			throw new ConnectionException("null connection");
		} else {
			handler.onPacket(connection, packet);
		}
	}

	/**
	 * Start this server handler
	 * 
	 * @throws ServerException
	 */
	public void start() throws ServerException {

		/**
		 * Activate all listeners
		 */
		synchronized (syncObj) {
			for (ServerListener listener : listeners) {
				listener.startListening(true); // Use new thread per listener
			}
		}

		/**
		 * Create our worker group
		 */
		running = true;

		logger.info("Starting workers...");
		doWork();
	}

	/**
	 * Organizes workers
	 */
	private void doWork() {

		// Remove list
		List<Connection> removeList = new LinkedList<Connection>();

		// Should be called from server pool
		while (running) {
			ConnectionHandler lastHandler = null;
			Connection lastConnection = null;
			// Do simple things
			try {

				try {
					// Loop through all connections
					for (ServerListener listener : listeners) {
						lastHandler = listener.getConnectionHandler();

						List<Connection> connections = listener
								.getConnectionHandler().getConnections();

						List<Connection> toRemove = new LinkedList<Connection>();
						for (Connection connection : connections) {
							lastConnection = connection;
							if (connection.isDisconnected()) {
								if (connection.getChannel().isOpen()) {
									connection.getChannel().close();
								}
								toRemove.add(connection);
							} else {
								// Try a read
								handleConnection(connection, lastHandler);
							}
						}
						for (Connection connection : toRemove) {
							onDisconnect(connection, lastHandler);
						}
					}
					// Sleep for 10 ms
					try {
						Thread.sleep(1);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				} catch (IOException iex) {
					if (lastConnection != null && lastHandler != null) {
						onDisconnect(lastConnection, lastHandler);
					}
					logger.error("ConnectionException " + iex.getMessage());
				}
			} catch (ConnectionException ce) {
				logger.error("ConnectionException " + ce.getMessage());
				ce.printStackTrace();
			}

		}
	}

	public void handleConnection(Connection connection,
			ConnectionHandler handler) throws IOException, ConnectionException {
		// Do write before read
		Packet packet = null;
		if ((packet = connection.getNextPacket()) != null) {
			if (connection.getPacketEncoder() != null) {
				try {
					connection.getPacketEncoder().encode(connection, packet);
				} catch (EncodingException ee) {
					ee.printStackTrace();
				}
			} else {
				try {
					defaultEncoder.encode(connection, packet);
				} catch (EncodingException ee) {
					ee.printStackTrace();
				}
			}

		}
		packet = null; // Put packet to null for reading
		if (connection.getPacketDecoder() != null) {
			try {
				packet = connection.getPacketDecoder().decode(connection);
			} catch (DecodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				packet = defaultDecoder.decode(connection);
			} catch (DecodingException e) {
				// TODO Auto-generated catch block
				connection.disconnect();
			}
		}
		if (packet != null) {
			onPacket(connection, handler, packet);
		}
	}
}
