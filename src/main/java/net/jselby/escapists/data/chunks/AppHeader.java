package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;

import java.nio.ByteBuffer;

/**
 * Created by James on 21/10/2015.
 */
public class AppHeader extends Chunk {
    /**
     * Initialises this chunk with a ByteBuffer.
     *
     * @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    @Override
    public void init(ByteBuffer buffer, int length) {
        int size = buffer.getInt();

        int flags1 = buffer.getShort() & 0xFFFF;
        int flags2 = buffer.getShort() & 0xFFFF;
        int mode = buffer.getShort();
        int flags3 = buffer.getShort() & 0xFFFF;

        int windowHeight = buffer.getShort() & 0xFFFF;
        int windowWidth = buffer.getShort() & 0xFFFF;
        System.out.println(windowWidth + ":" + windowHeight);

        int initialScore = buffer.getInt();
        int initialLives = buffer.getInt();
    }
}
