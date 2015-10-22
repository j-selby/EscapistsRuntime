package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The GlobalStrings chunk contains strings used during execution.
 */
public class GlobalStrings extends Chunk {
    private String[] data;

    @Override
    public void init(ByteReader buffer, int length) {
        long l = ((long) buffer.getInt() & 0xffffffffL);
        data = new String[(int) l];

        for (int i = 0; i < l; i++) {
            data[i] = buffer.getString();
        }
    }
}
