package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The effects of an object
 */
public class ObjectEffects extends Chunk {
    private long id;
    private byte[][] data;

    @Override
    public void init(ByteReader buffer, int length) {
        id = buffer.getUnsignedInt();
        int dataLength = (int) buffer.getUnsignedInt();
        data = new byte[dataLength][];

        for (int i = 0; i < dataLength; i++) {
            data[i] = buffer.getBytes(4);
        }
    }
}
