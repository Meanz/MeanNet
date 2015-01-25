package com.meanworks.server.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferPacketBuffer extends PacketBuffer {

	/**
	 * 
	 */
	private ByteBuffer buffer;

	/**
	 * 
	 */
	private boolean writeable;

	/**
	 * 
	 * @param size
	 * @param writeable
	 */
	public ByteBufferPacketBuffer(int size, boolean writeable) {
		buffer = ByteBuffer.allocate(size);
		buffer.order(ByteOrder.nativeOrder());
		this.writeable = writeable;
	}
	
	/**
	 * 
	 * @param size
	 * @param writeable
	 */
	public ByteBufferPacketBuffer(int size) {
		buffer = ByteBuffer.allocate(size);
		buffer.order(ByteOrder.nativeOrder());
		this.writeable = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#getCaret()
	 */
	public byte[] getCaret() {
		return buffer.array();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#flip()
	 */
	@Override
	public void flip() {
		buffer.flip();
	}

	/**
	 * 
	 * @return
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return buffer.array().length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#mark()
	 */
	public void mark() {
		buffer.mark();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#rewind()
	 */
	public void rewind() {
		buffer.rewind();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#position()
	 */
	public int position() {
		return buffer.position();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#position(int)
	 */
	public void position(int position) {
		buffer.position(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#put(byte)
	 */
	public void put(byte b) {
		buffer.put(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#put(byte[], int, int)
	 */
	public void put(byte[] b, int off, int len) {
		buffer.put(b, off, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#put(byte[])
	 */
	public void put(byte[] b) {
		put(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#put(int)
	 */
	public void put(int i) {
		buffer.put((byte) i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#putShort(short)
	 */
	public void putShort(short s) {
		buffer.putShort(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#putInt(int)
	 */
	public void putInt(int i) {
		buffer.putInt(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#putFloat(float)
	 */
	public void putFloat(float f) {
		buffer.putFloat(f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#putDouble(double)
	 */
	public void putDouble(double d) {
		buffer.putDouble(d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#putLong(long)
	 */
	public void putLong(long l) {
		buffer.putLong(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#get()
	 */
	public byte get() {
		return buffer.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#getShort()
	 */
	public short getShort() {
		return buffer.getShort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#getInt()
	 */
	public int getInt() {
		return buffer.getInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#getFloat()
	 */
	public float getFloat() {
		return buffer.getFloat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#getDouble()
	 */
	public double getDouble() {
		return buffer.getDouble();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meanworks.server.io.PacketBuffer#getLong()
	 */
	public long getLong() {
		return buffer.getLong();
	}

}
