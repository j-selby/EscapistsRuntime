package net.jselby.escapists.data;

import net.jselby.escapists.util.ByteReader;

/**
 * A StringChunk is a Chunk consisting entirely of a String.
 */
public abstract class StringChunk extends Chunk {
    protected String content;

    /**
     * Initialises this chunk with a ByteBuffer.
     *  @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    @Override
    public void init(ByteReader buffer, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length / 2; i++) {
            builder.append(buffer.getChar());
        }
        content =builder.toString();
    }

    /**
     * Returns the String contents for this Chunk.
     * @return A String
     */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return  "{" + getClass().getSimpleName() + "=\"" + content + "\"}";
    }
}
