package com.meanworks.server.io;

public abstract class PacketBuffer {

	/**
	 * 
	 * @return
	 */
	public abstract byte[] getCaret();
	
	/**
	 * Flip the buffer
	 */
	public abstract void flip();

	/**
	 * Get the size of this buffer
	 * 
	 * @return
	 */
	public abstract int size();

	/**
	 * 
	 */
	public abstract void mark();

	/**
	 * 
	 */
	public abstract void rewind();

	/**
	 * 
	 * @return
	 */
	public abstract int position();

	/**
	 * 
	 * @param i
	 */
	public abstract void position(int i);

	/**
	 * 
	 * @param b
	 */
	public abstract void put(byte b);

	/**
	 * 
	 * @param b
	 */
	public abstract void put(byte[] b, int off, int len);

	/**
	 * 
	 * @param b
	 */
	public abstract void put(byte[] b);

	/**
	 * 
	 * @param i
	 */
	public abstract void put(int i);

	/**
	 * 
	 * @param s
	 */
	public abstract void putShort(short s);

	/**
	 * 
	 * @param i
	 */
	public abstract void putInt(int i);

	/**
	 * 
	 * @param f
	 */
	public abstract void putFloat(float f);

	/**
	 * 
	 * @param d
	 */
	public abstract void putDouble(double d);

	/**
	 * 
	 * @param l
	 */
	public abstract void putLong(long l);

	/**
	 * Put a string into this packet buffer
	 * 
	 * @param s
	 */
	public void putString(String s) {
		for (int i = 0; i < s.length(); i++) {
			put(s.getBytes()[i]);
		}
		put((byte) '\0');
	}

	/**
	 * 
	 * @return
	 */
	public abstract byte get();

	/**
	 * 
	 * @return
	 */
	public abstract short getShort();

	/**
	 * 
	 * @return
	 */
	public abstract int getInt();

	/**
	 * 
	 * @return
	 */
	public abstract float getFloat();

	/**
	 * 
	 * @return
	 */
	public abstract double getDouble();

	/**
	 * 
	 * @return
	 */
	public abstract long getLong();

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
