package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * A set of handles pointing to frames.
 */
public class FrameHandles extends Chunk {
    private short[] handles;

    @Override
    public void init(ByteReader buffer, int length) {
        int count = length / 2;
        handles = new short[count];
        for (int i = 0; i < count; i++) {
            handles[i] = buffer.getShort();
        }

    }
}
