package net.jselby.escapists.data;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * A StringChunk is a Chunk consisting entirely of a String.
 */
public abstract class StringChunk extends Chunk {
    protected String content;

    /**
     * Initialises this chunk with a ByteBuffer.
     *
     * @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    @Override
    public void init(ByteBuffer buffer, int length) {
        try {
            content = new String(buffer.array(), getEncodingType());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the String contents for this Chunk.
     * @return A String
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the encoding type of this Chunk.
     * @return The encoding type.
     */
    protected abstract String getEncodingType();

    @Override
    public String toString() {
        return content;
    }
}
