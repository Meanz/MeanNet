/**
 * 
 */
package com.meanworks.server.net;

import com.meanworks.server.io.PacketBuffer;
import com.meanworks.server.io.RawPacketBuffer;

/**
 * @author Meanz
 * 
 */
public class Packet {

	/**
	 * Creates a packet with buffer of the given length and id
	 * 
	 * @param packetId
	 * @param packetLength
	 * @param buffer
	 */
	public static Packet createRaw(int packetId, int packetLength) {
		RawPacketBuffer rpb = new RawPacketBuffer(packetLength);
		return new Packet(packetId, packetLength, rpb);
	}

	/**
	 * The buffer of this packet
	 */
	private PacketBuffer buffer;

	/**
	 * The id of this packet
	 */
	private int packetId;

	/**
	 * The length of this packet
	 */
	private int packetLength;

	/**
	 * Used for decoders
	 */
	public int readPos = 0;

	/**
	 * Used for encoders
	 */
	public int writePos = 0;

	/**
	 * Construct a new packet
	 */
	public Packet(int packetId, int packetLength, PacketBuffer buffer) {
		this.packetId = packetId;
		this.packetLength = packetLength;
		this.buffer = buffer;
	}

	/**
	 * Construct a new packet with a fixed size
	 * 
	 * @param packetId
	 * @param packetLength
	 */
	public Packet(int packetId, int packetLength) {
		this.packetId = packetId;
		this.packetLength = packetLength;
		// Hmm
		buffer = new RawPacketBuffer(packetLength);
	}

	/**
	 * Construct a new packet with a dynamic size
	 * 
	 * @param packetId
	 */
	public Packet(int packetId) {
		this.packetId = packetId;
		this.packetLength = -1;
		buffer = new RawPacketBuffer(packetLength, true);
	}

	/**
	 * Get the id of this packet
	 * 
	 * @return
	 */
	public int getId() {
		return packetId;
	}

	/**
	 * Get the length of this packet
	 * 
	 * @return
	 */
	public int getLength() {
		return packetLength == -1 ? buffer.size() : packetLength;
	}

	/**
	 * Get the buffer of this packet
	 * 
	 * @return
	 */
	public PacketBuffer getBuffer() {
		return buffer;
	}

}
