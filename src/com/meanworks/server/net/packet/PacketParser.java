package com.meanworks.server.net.packet;

import java.lang.reflect.Field;
import java.util.LinkedList;

import com.meanworks.server.ServerLogger;
import com.meanworks.server.io.RawPacketBuffer;
import com.meanworks.server.net.Packet;

public class PacketParser {

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Packet parsePacket(Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		/**
		 * Read all annotations
		 */
		WritePacket writePacket = obj.getClass().getAnnotation(
				WritePacket.class);
		if (writePacket == null) {
			ServerLogger.getDefault().debug(
					"Could not find WritePacket annotation.");
		} else {
			// Find fields
			Field[] fields = obj.getClass().getDeclaredFields();
			int size = 0;
			LinkedList<Field> toBeParsed = new LinkedList<Field>();
			for (Field f : fields) {
				if (f.getType().equals(int.class)) {
					size += 4;
					toBeParsed.add(f);
				} else if (f.getType().equals(float.class)) {
					size += 4;
					toBeParsed.add(f);
				} else if (f.getType().equals(double.class)) {
					size += 8;
					toBeParsed.add(f);
				} else if (f.getType().equals(long.class)) {
					size += 8;
					toBeParsed.add(f);
				} else if (f.getType().equals(String.class)) {
					String str = (String) f.get(obj);
					size += str.length() + 1;
					toBeParsed.add(f);
				} else if (f.getType().equals(boolean.class)) {
					size += 1;
					toBeParsed.add(f);
				} else if (f.getType().equals(byte.class)) {
					size += 1;
					toBeParsed.add(f);
				}
			}
			// Parse evelytin
			RawPacketBuffer rpb = new RawPacketBuffer(size);
			for (Field f : toBeParsed) {
				if (f.getType().equals(int.class)) {
					rpb.putInt(f.getInt(obj));
				} else if (f.getType().equals(float.class)) {
					rpb.putFloat(f.getFloat(obj));
				} else if (f.getType().equals(double.class)) {
					rpb.putDouble(f.getDouble(obj));
				} else if (f.getType().equals(long.class)) {
					rpb.putLong(f.getLong(obj));
				} else if (f.getType().equals(String.class)) {
					String str = (String) f.get(obj);
					rpb.putString(str);
				} else if (f.getType().equals(boolean.class)) {
					rpb.putBool(f.getBoolean(obj));
				} else if (f.getType().equals(byte.class)) {
					rpb.put(f.getByte(obj));
				}
			}
			Packet packet = new Packet(writePacket.id(), size, rpb);
			packet.getBuffer().rewind();
			return packet;
		}
		return null;
	}

}
