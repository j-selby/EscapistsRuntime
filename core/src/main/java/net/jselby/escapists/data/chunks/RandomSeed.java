package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The RandomSeed chunk is a seed used to generate random numbers.
 */
public class RandomSeed extends Chunk {
    private short value;

    @Override
    public void init(ByteReader buffer, int length) {
        value = buffer.getShort();
    }
}
