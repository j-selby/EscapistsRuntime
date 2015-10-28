package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * A FrameEffect is a effect applied to a frame.
 * (Ignored)
 */
public class FrameEffects extends Chunk {
    @Override
    public void init(ByteReader buffer, int length) {
        buffer.skipBytes(2);
    }
}
