/**
 * 
 */
package com.meanworks.server.net;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A simple container for the SocketChannel, Also allows attributes to follow
 * the SocketChannel
 * 
 * @author Meanz
 * 
 */
public class Connection {

	/**
	 * The socket channel for this connection
	 */
	private SocketChannel channel;

	/**
	 * Attributes for the connection
	 */
	private HashMap<String, Object> attributes;

	/**
	 * The packet queue
	 */
	private LinkedList<Packet> packetQueue;

	/**
	 * 
	 */
	private boolean disconnect;

	/**
	 * The encoder for this connection or null if default one is in use
	 */
	private IPacketEncoder packetEncoder;

	/**
	 * The decoder for this connection or null if default one is in use
	 */
	private IPacketDecoder packetDecoder;

	/**
	 * Construct a new Connection, for internal use only
	 * 
	 * @param channel
	 */
	public Connection(SocketChannel channel) {
		this.channel = channel;
		this.attributes = new HashMap<String, Object>();
		packetQueue = new LinkedList<Packet>();
		disconnect = false;
		packetEncoder = null;
		packetDecoder = null;
	}

	/**
	 * Get the packet encoder for this connection
	 * 
	 * @return
	 */
	public IPacketEncoder getPacketEncoder() {
		return packetEncoder;
	}

	/**
	 * Set the packet encoder for this connection
	 * 
	 * @param packetEncoder
	 */
	public void setPacketEncoder(IPacketEncoder packetEncoder) {
		this.packetEncoder = packetEncoder;
	}

	/**
	 * Get the packet decoder for this connection
	 * 
	 * @return
	 */
	public IPacketDecoder getPacketDecoder() {
		return packetDecoder;
	}

	/**
	 * Set the packet decoder for this connection
	 * 
	 * @param packetDecoder
	 */
	public void setPacketDecoder(IPacketDecoder packetDecoder) {
		this.packetDecoder = packetDecoder;
	}

	/**
	 * Check whether this connection is marked for disconnect or not
	 * 
	 * @return
	 */
	public boolean isDisconnected() {
		return disconnect;
	}

	/**
	 * Mark this connection for disconnect
	 */
	public void disconnect() {
		this.disconnect = true;
	}

	/**
	 * Get the address of this connection
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getAddress() {
		//try {
			if (getChannel().isOpen()) {
				return getChannel().socket().getInetAddress().toString();
			} else {
				return "null (closed)";
			}
		/*} catch (IOException iex) {
			iex.printStackTrace();
		}*/
		//return "null (?)";
	}

	/**
	 * Get the socket channel for this connection
	 * 
	 * @return
	 */
	public SocketChannel getChannel() {
		return channel;
	}

	/**
	 * Get the next packet for sending
	 * 
	 * @return
	 */
	public Packet getNextPacket() {
		synchronized (packetQueue) {
			if (packetQueue.size() > 0) {
				return packetQueue.pop();
			} else {
				return null;
			}
		}
	}

	/**
	 * Queues the given packet for encoding and sending
	 * 
	 * @param packet
	 */
	public void writePacket(Packet packet) {
		synchronized (packetQueue) {
			packetQueue.addFirst(packet);
		}
	}

	/**
	 * Set an attribute for this connection
	 * 
	 * @param key
	 * @param obj
	 * @return
	 */
	public Object setAttribute(String key, Object obj) {
		return attributes.put(key, obj);
	}

	/**
	 * Get an attribute from this connection
	 * 
	 * @param key
	 * @return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T getAttribute(String key) {
		Object obj = attributes.get(key);
		if (obj != null) {
			return (T) obj;
		} else {
			return null;
		}
	}

}
