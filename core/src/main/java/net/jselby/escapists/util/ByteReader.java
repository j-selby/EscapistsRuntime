package net.jselby.escapists.util;

import com.badlogic.gdx.graphics.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The ByteReader reads various pieces of data from a ByteBuffer stream in various formats.
 */
public class ByteReader {
    private final ByteBuffer buf;

    /**
     * Creates a ByteReader from a already-existing buffer.
     * @param buf The buffer
     */
    public ByteReader(ByteBuffer buf) {
        this.buf = buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Creates a ByteReader from a byte array.
     * @param data The data
     */
    public ByteReader(byte[] data) {
        this.buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Reads an integer from the ByteBuffer.
     * @return An integer
     */
    public int getInt() {
        return buf.getInt();
    }

    /**
     * Reads an unsigned integer from the ByteBuffer.
     * @return An unsigned integer
     */
    public long getUnsignedInt() {
        return ((long) buf.getInt() & 0xffffffffL);
    }

    /**
     * Reads a float from the ByteBuffer.
     * @return A float
     */
    public float getFloat() {
        return buf.getFloat();
    }

    /**
     * Reads a character from the ByteBuffer.
     * @return A character
     */
    public char getChar() {
        return buf.getChar();
    }

    /**
     * Reads a byte from the ByteBuffer.
     * @return A byte
     */
    public byte getByte() {
        return buf.get();
    }

    /**
     * Reads an unsigned byte from the ByteBuffer.
     * @return An unsigned byte
     */
    public short getUnsignedByte() {
        return ((short) (buf.get() & 0xff));
    }

    /**
     * Reads a set of bytes from the ByteBuffer.
     * @return A set of bytes
     */
    public byte[] getBytes(int length) {
        byte[] data = new byte[length];
        buf.get(data);
        return data;
    }

    /**
     * Reads a set of bytes from the ByteBuffer using a preallocated byte array.
     * @return A set of bytes
     */
    public byte[] getBytes(byte[] bytes) {
        buf.get(bytes);
        return bytes;
    }

    /**
     * Reads a short from the ByteBuffer.
     * @return a short
     */
    public short getShort() {
        return buf.getShort();
    }

    /**
     * Reads an unsigned short from the ByteBuffer.
     * @return an unsigned short
     */
    public int getUnsignedShort() {
        return (buf.getShort() & 0xffff);
    }

    /**
     * Reads a color from the ByteBuffer.
     * @return a color
     */
    public Color getColor() {
        short r = getUnsignedByte();
        short g = getUnsignedByte();
        short b = getUnsignedByte();
        skipBytes(1);
        return new Color(Color.rgba8888(r / 256f, g / 256f, b / 256f, 1));
    }

    /**
     * Sets the position of the byte buffer.
     * @param pos A byte position
     */
    public void setPosition(int pos) {
        buf.position(pos);
    }

    /**
     * Returns the current position of the byte buffer.
     * @return A position
     */
    public int getPosition() {
        return buf.position();
    }

    /**
     * Skips a particular amount of bytes.
     * @param count Byte count
     */
    public void skipBytes(int count) {
        buf.position(buf.position() + count);
    }

    public String getString() {
        StringBuilder builder = new StringBuilder();
        while(true) {
            char character = buf.getChar();
            if (character == '\0') {
                break;
            }
            builder.append(character);
        }
        return builder.toString();
    }

    public String getString(int length) {
        return new ByteReader(getBytes(length)).getString();
    }

    public double getDouble() {
        return buf.getDouble();
    }
}
