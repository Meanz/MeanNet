/**
 * 
 */
package com.meanworks.server.net;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.meanworks.server.ServerLogger;
import com.meanworks.server.net.packet.PacketParser;

/**
 * @author Meanz
 * 
 */
public abstract class ConnectionHandler {

	/**
	 * The list of connections this connection handler holds
	 */
	private LinkedList<Connection> connections = new LinkedList<Connection>();

	/**
	 * Add a connection to this handler
	 * 
	 * @param connection
	 */
	public void addConnection(Connection connection) {
		synchronized (connections) {
			connections.add(connection);
		}
	}

	/**
	 * Remove a connection form this connection handler
	 * 
	 * @param connection
	 */
	public void removeConnection(Connection connection) {
		synchronized (connections) {
			connections.remove(connection);
		}
	}

	/**
	 * Broadcast the packet to all the connections in this connection handler
	 * 
	 * @param packet
	 */
	public void broadcast(Packet packet) {
		for (Connection c : connections) {
			c.writePacket(packet);
		}
	}

	/**
	 * Write the packet to the given connection
	 * 
	 * @param connection
	 * @param packet
	 */
	public boolean sendPacket(Connection connection, Object packet) {
		try {
			connection.writePacket(PacketParser.parsePacket(packet));
			return true;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Write the packet to the given connection
	 * 
	 * @param connection
	 * @param packet
	 */
	public void sendPacket(Connection connection, Packet packet) {
		connection.writePacket(packet);
	}

	/**
	 * Get the list of connections
	 * 
	 * @return
	 */
	public List<Connection> getConnections() {
		return connections;
	}

	/**
	 * Check whether this connection can connect or not, can be used to add
	 * connection filters
	 * 
	 * @return
	 */
	public boolean canConnect(SocketAddress address) throws ConnectionException {
		return true;
	}

	/**
	 * Called when a socket is connected
	 * 
	 * @param acceptor
	 * @param socket
	 */
	public void onConnected(Connection connection) throws ConnectionException {
		ServerLogger.getDefault().debug("onConnected not handled by custom ConnectionHandler");
	}

	/**
	 * Called when a socket is disconnected
	 * 
	 * @param acceptor
	 * @param socket
	 */
	public void onDisconnect(Connection connection) throws ConnectionException {
		ServerLogger.getDefault().debug("onDisconnect not handled by custom ConnectionHandler");
	}

	/**
	 * 
	 * @param connection
	 * @param packet
	 * @throws ConnectionException
	 */
	public void onPacket(Connection connection, Packet packet)
			throws ConnectionException {

	}

}
