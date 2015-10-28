package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * A Protection chunk is a chunk checksum.
 */
public class Protection extends Chunk {
    private long checksum;

    @Override
    public void init(ByteReader buffer, int length) {
        checksum = buffer.getUnsignedInt();
    }
}
