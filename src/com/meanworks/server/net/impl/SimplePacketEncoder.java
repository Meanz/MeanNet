/**
 * 
 */
package com.meanworks.server.net.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.meanworks.server.ServerLogger;
import com.meanworks.server.net.Connection;
import com.meanworks.server.net.EncodingException;
import com.meanworks.server.net.IPacketEncoder;
import com.meanworks.server.net.Packet;

/**
 * @author Meanz
 * 
 */
public class SimplePacketEncoder implements IPacketEncoder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.meanworks.server.net.IPacketEncoder#encode(com.meanworks.server.net
	 * .Packet)
	 */
	@Override
	public boolean encode(Connection connection, Packet packet)
			throws EncodingException {
		try {
			SocketChannel channel = connection.getChannel();
			// Write header
			ByteBuffer headerBuffer = ByteBuffer.allocate(4);
			headerBuffer.putShort((short) packet.getId());
			headerBuffer.putShort((short) packet.getLength());
			headerBuffer.flip();
			while (headerBuffer.hasRemaining()) {
				channel.write(headerBuffer);
			}
			// For now write the whole shabang, later we shall split it into
			// chunks
			ByteBuffer buffer = ByteBuffer.wrap(packet.getBuffer().getCaret());
			while (buffer.hasRemaining()) {
				channel.write(buffer);
			}
			return true;
		} catch (IOException iex) {
			throw new EncodingException(iex);
		}

	}

}
