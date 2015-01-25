/**
 * 
 */
package com.meanworks.server.net;

import java.net.Socket;

/**
 * @author Meanz
 * 
 */
public interface IPacketEncoder {

	/**
	 * Encode the given packet
	 * @param connection
	 * @param packet
	 * @return
	 * @throws EncodingException
	 */
	public boolean encode(Connection connection, Packet packet) throws EncodingException;

}
