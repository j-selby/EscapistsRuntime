package net.jselby.escapists.data;

import net.jselby.escapists.util.ByteReader;

/**
 * A Chunk is a dump of a Chunk from the game files.
 *
 * @author j_selby
 */
public abstract class Chunk {
    /**
     * Initialises this chunk with a ByteBuffer.
     *  @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    public abstract void init(ByteReader buffer, int length);
}
