/**
 * 
 */
package com.meanworks.server.test;

import java.net.SocketAddress;

import com.meanworks.server.ServerLogger;
import com.meanworks.server.io.PacketBuffer;
import com.meanworks.server.net.Connection;
import com.meanworks.server.net.ConnectionException;
import com.meanworks.server.net.ConnectionHandler;
import com.meanworks.server.net.Packet;

/**
 * @author Meanz
 * 
 */
public class TestConnectionHandler extends ConnectionHandler {

	/**
	 * Called to check if a connection is allowed to be opened
	 */
	@Override
	public boolean canConnect(SocketAddress address) throws ConnectionException {
		// TODO Auto-generated method stub
		if (address.toString().equals("google.com")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Called when a user has connected, Good for sending welcome messages or
	 * registering the user in your systems
	 */
	@Override
	public void onConnected(Connection connection) throws ConnectionException {
		// TODO Auto-generated method stub
		super.onConnected(connection);
		ServerLogger.getDefault().debug(
				"Client " + connection.getAddress().toString()
						+ " has connected.");
	}

	/**
	 * Called when a socket is being closed
	 */
	@Override
	public void onDisconnect(Connection connection) throws ConnectionException {
		// TODO Auto-generated method stub
		super.onDisconnect(connection);
		ServerLogger.getDefault().debug(
				"Client " + connection.getAddress().toString()
						+ " has disconnected.");
	}

	/**
	 * Called when a packet is retrieved
	 */
	@Override
	public void onPacket(Connection connection, Packet packet)
			throws ConnectionException {
		// TODO Auto-generated method stub
		super.onPacket(connection, packet);

		PacketBuffer buf = packet.getBuffer();
		if (packet.getId() == 5) {
			// Auth packet
			int magicId = buf.getInt();
			if (magicId == 13371337) {
				ServerLogger.getDefault().debug("Auth OK!");
			} else {
				ServerLogger.getDefault().debug(
						"Auth BAD! Got \"" + magicId
								+ "\" Disconnecting client");
				connection.disconnect();
			}
		} else if (packet.getId() == 6) {
			// CMD Packet
			String packetCmd = buf.getString();
			ServerLogger.getDefault().debug("CMD: " + packetCmd);
		} else if (packet.getId() == 7) {
			int option = buf.get();
			ServerLogger.getDefault().debug("Option: " + option);
		}

	}

}
