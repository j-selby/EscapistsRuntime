package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * A VirtualSize is a Rectangle within a frame.
 */
public class VirtualSize extends Chunk {
    public int left;
    public int top;
    public int right;
    public int bottom;

    @Override
    public void init(ByteReader buffer, int length) {
        left = buffer.getInt();
        top = buffer.getInt();
        right = buffer.getInt();
        bottom = buffer.getInt();
    }
}
