/**
 * 
 */
package com.meanworks.server.net;


/**
 * @author Meanz
 *
 */
public interface IPacketDecoder {

	/**
	 * Decode a packet from the given socket
	 * @param socket
	 * @return
	 * @throws DecodingException
	 */
	public Packet decode(Connection connection) throws DecodingException;
	
}
