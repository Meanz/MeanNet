/**
 * 
 */
package com.meanworks.server.net.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.meanworks.server.ServerLogger;
import com.meanworks.server.io.RawPacketBuffer;
import com.meanworks.server.net.Connection;
import com.meanworks.server.net.DecodingException;
import com.meanworks.server.net.IPacketDecoder;
import com.meanworks.server.net.Packet;

/**
 * @author Meanz
 * 
 */
public class SimplePacketDecoder implements IPacketDecoder {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.net.IPacketDecoder#decode(java.net.Socket)
	 */
	@Override
	public Packet decode(Connection connection) throws DecodingException {
		// Get the input stream of the socket
		try {
			//
			SocketChannel channel = connection.getChannel();
			// Try to read the header
			Packet packet = null;
			if ((packet = connection.getAttribute("tempPacket")) != null) {
				int packetLength = packet.getLength();
				int remaining = packetLength - packet.readPos;

				//ServerLogger.getDefault().debug(
				//		"Decode :: " + remaining + " of " + packetLength
				//				+ " bytes");
				if (remaining > 0) {
					// Allocate temporary chunk!
					ByteBuffer buffer = ByteBuffer
							.allocate(remaining > 512 ? 512 : remaining);

					// Try to read the chunk
					int read = channel.read(buffer);
					if (read == -1) {
						connection.disconnect();
						ServerLogger
								.getDefault()
								.debug("EOS in SimplePacketDecoder while reading a packet.");
						return null;
					}
					packet.readPos += read;
					remaining -= read;

					// Move the read bytes to the packet buffer
					packet.getBuffer()
							.put(buffer.array(), 0, buffer.position());

				}
				// Did we finish reading?
				if (remaining == 0) {
					// The packet is complete
					connection.setAttribute("headerBuffer", null);
					connection.setAttribute("tempPacket", null);
					packet.getBuffer().rewind(); // Move position to 0
					return packet;
				}
			} else {
				/*
				 * Attempt to read header
				 */
				ByteBuffer headerBuffer = null;
				if ((headerBuffer = connection.getAttribute("headerBuffer")) == null) {
					connection.setAttribute("headerBuffer",
							(headerBuffer = ByteBuffer.allocate(4)));
				}

				int read = channel.read(headerBuffer);
				if (read == -1) {
					connection.disconnect();
					ServerLogger.getDefault().debug(
							"EOS in SimplePacketDecoder");
					return null;
				}

				//ServerLogger.getDefault().debug("Read: " + read);

				if (headerBuffer.position() == 4) {

					// We have a full packet header!
					short packetId = (short) ((short) (headerBuffer.array()[0] << 8) + (short) (headerBuffer
							.array()[1]));
					short packetLength = (short) ((short) (headerBuffer.array()[2] << 8) + (short) (headerBuffer
							.array()[3]));

					packet = new Packet(packetId, packetLength,
							new RawPacketBuffer(packetLength));

					connection.setAttribute("tempPacket", packet);
				}
			}
			return null;
		} catch (IOException iex) {
			throw new DecodingException(iex);
		}
	}
}
