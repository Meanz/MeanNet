/**
 * 
 */
package com.meanworks.server.io;

import java.io.DataInputStream;

/**
 * @author Meanz
 * 
 */
public class RawPacketBuffer extends PacketBuffer {

	/**
	 * The caret of this buffer
	 */
	private byte[] caret = null;

	/**
	 * The position in the caret this buffer is at
	 */
	private int position = 0;

	/**
	 * The marked position in the caret
	 */
	private int markPosition = 0;

	/**
	 * Whether this buffer can dynamically expand
	 */
	private boolean writeable = true; // Auto for now

	/**
	 * Construct a new packet buffer with the given size and the given writeable
	 * state
	 * 
	 * @param size
	 * @param writeable
	 */
	public RawPacketBuffer(int size, boolean writeable) {
		caret = new byte[size];
		this.writeable = writeable;
	}

	/**
	 * Construct a new packet buffer with the given size
	 * 
	 * @param size
	 */
	public RawPacketBuffer(int size) {
		this(size, false);
	}

	/**
	 * Construct a new packet buffer
	 * 
	 * @param caret
	 */
	public RawPacketBuffer(byte[] caret) {
		this.caret = caret;
		writeable = false; // This is a read only buffer!
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#flip()
	 */
	@Override
	public void flip() {
	}

	/**
	 * Get the remaining amount of bytes in the backing array
	 * 
	 * @return
	 */
	public int remaining() {
		return size() - position;
	}

	/**
	 * Get the size of the backing array
	 * 
	 * @return
	 */
	public int size() {
		return caret.length;
	}

	/**
	 * Get the backing array for this buffer
	 * 
	 * @return
	 */
	public byte[] getCaret() {
		return caret;
	}

	/**
	 * Get the position of this packet buffer
	 * 
	 * @return
	 */
	public int position() {
		return position;
	}

	/**
	 * Set the position of this packet buffer
	 * 
	 * @param position
	 */
	public void position(int position) {
		this.position = position;
	}

	/**
	 * Marks the given position in this packet buffer
	 */
	public void mark() {
		markPosition = position;
	}

	/**
	 * Moves the position to the given mark
	 */
	public void toMark() {
		position(markPosition);
	}

	/**
	 * Set the position to 0
	 */
	public void rewind() {
		position(0);
	}

	/**
	 * Ensure capacity in this packet buffer
	 * 
	 * @param size
	 */
	public void ensureCapacity(int size) {
		if (caret.length - position >= size) {
			// Evelything is okay!
		} else {
			if (writeable) {
				// Expand buffer
				byte[] temp = caret;
				caret = new byte[(size - (caret.length - position))
						+ caret.length];
				System.arraycopy(temp, 0, caret, 0, temp.length);
			} else {
				throw new RuntimeException("PacketBuffer OutOfBounds");
			}
		}
	}

	/**
	 * Put a byte in this packet buffer
	 * 
	 * @param b
	 */
	public void put(byte b) {
		caret[position++] = b;
	}

	/**
	 * Put a byte in this packet buffer
	 * 
	 * @param i
	 */
	public void put(int i) {
		ensureCapacity(1);
		caret[position++] = (byte) i;
	}

	/**
	 * Put the given byte array in this packet buffer with the given offset and
	 * length
	 * 
	 * @param bytes
	 * @param off
	 * @param len
	 */
	public void put(byte[] bytes, int off, int len) {
		for (int i = off; i < off + len; i++) {
			put(bytes[i]);
		}
	}

	/**
	 * Put the given byte array in this packet buffer
	 * 
	 * @param bytes
	 */
	public void put(byte[] bytes) {
		put(bytes, 0, bytes.length);
	}

	/**
	 * Put a boolean in this packet buffer
	 * 
	 * @param bool
	 */
	public void putBool(boolean bool) {
		put(bool ? 1 : 0);
	}

	/**
	 * Put a short in this packet buffer
	 * 
	 * @param s
	 */
	public void putShort(short s) {
		ensureCapacity(2);
		put((s >>> 8) & 0xFF);
		put(s);
	}

	/**
	 * Put an integer in this packet buffer
	 * 
	 * @param i
	 */
	public void putInt(int i) {
		ensureCapacity(4);
		put(i >> 24);
		put(i >> 16);
		put(i >> 8);
		put(i);
	}

	/**
	 * Put a float in this packet buffer
	 * 
	 * @param f
	 */
	public void putFloat(float f) {
		putInt(Float.floatToIntBits(f));
	}

	/**
	 * Put a double in this packet buffer
	 * 
	 * @param d
	 */
	public void putDouble(double d) {
		putLong(Double.doubleToLongBits(d));
	}

	/**
	 * Put a long into this packet buffer
	 * 
	 * @param l
	 */
	public void putLong(long l) {
		ensureCapacity(8);
		put((byte) (l >> 56));
		put((byte) (l >> 48));
		put((byte) (l >> 40));
		put((byte) (l >> 32));
		put((byte) (l >> 24));
		put((byte) (l >> 16));
		put((byte) (l >> 8));
		put((byte) (l));
	}

	/**
	 * Put a string into this packet buffer
	 * 
	 * @param s
	 */
	public void putString(String s) {
		ensureCapacity(s.length() + 1);
		for (int i = 0; i < s.length(); i++) {
			put(s.getBytes()[i]);
		}
		put((byte) '\0');
	}

	/**
	 * Get a byte from this packet buffer
	 * 
	 * @return
	 */
	public byte get() {
		return caret[position++];
	}

	/**
	 * Fill the given byte array with bytes from this packet buffer with the
	 * specified offset and length
	 * 
	 * @param bytes
	 * @param off
	 * @param len
	 * @return
	 */
	public int get(byte[] bytes, int off, int len) {
		int read = 0;
		for (int i = off; i < off + len; i++) {
			bytes[i] = get();
			read++;
		}
		return read;
	}

	/**
	 * Fill the given byte array with bytes from this packet buffer
	 * 
	 * @param bytes
	 * @return
	 */
	public int get(byte[] bytes) {
		return get(bytes, 0, bytes.length);
	}

	/**
	 * Get a boolean from this packet buffer
	 * 
	 * @return
	 */
	public boolean getBool() {
		return get() == 1 ? true : false;
	}

	/**
	 * Get a short from this packet buffer
	 * 
	 * @return
	 */
	public short getShort() {
		return (short) ((get() << 8) + get());
	}

	/**
	 * Get an integer from this packet buffer
	 * 
	 * @return
	 */
	public int getInt() {
		return (int) ((get() << 24) + ((get() & 0xff) << 16)
				+ ((get() & 0xff) << 8) + (get() & 0xff));
	}

	/**
	 * Get a float from this packet buffer
	 * 
	 * @return
	 */
	public float getFloat() {
		return Float.intBitsToFloat(getInt());
	}

	/**
	 * Get double from this packet buffer
	 * 
	 * @return
	 */
	public double getDouble() {
		return Double.longBitsToDouble(getLong());
	}

	/**
	 * Get a short from this packet buffer
	 * 
	 * @return
	 */
	public long getLong() {
		return (long) ((long) (get() << 56) + (long) ((get() & 255) << 48)
				+ (long) ((get() & 255) << 40) + (long) ((get() & 255) << 32)
				+ (long) ((get() & 255) << 24) + (long) ((get() & 255) << 16)
				+ (long) ((get() & 255) << 8) + (long) ((get() & 255) << 0));
	}

	/**
	 * Get a string from this packet buffer
	 * 
	 * @return
	 */
	public String getString() {
		String s = "";
		char c;
		while ((c = (char) get()) != '\0') {
			s += c;
		}
		return s;
	}
}
