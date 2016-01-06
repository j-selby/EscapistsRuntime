package net.jselby.escapists.data;

import net.jselby.escapists.util.ByteReader;

import java.util.Arrays;

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
        String str = "";
        for (int i = 0; i < length / 2; i++) {
            str += buffer.getChar();
        }
        if (str.endsWith("\00")) {
            str = str.substring(0, str.length() - 1);
        }
        content = str;
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
