package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * States whether this executable is independent of other files.
 */
public class ExeOnly extends Chunk {
    private byte exeOnly;

    @Override
    public void init(ByteReader buffer, int length) {
        exeOnly = buffer.getByte();
    }
}
