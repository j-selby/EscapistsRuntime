package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * Global values contains a set of properties of integers and floats.
 */
public class GlobalValues extends Chunk {
    private float[] values;

    @Override
    public void init(ByteReader buffer, int length) {
        int numberOfItems = buffer.getShort() & 0xffff;

        values = new float[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            int type = buffer.getByte() & 0xFF;
            float value = 0;
            if (type == 2) {
                value = buffer.getFloat();
            } else if (type == 0) {
                value = buffer.getInt();
            } else {
                System.out.printf("Invalid GlobalValue type %d.\n", type);
            }
            values[i] = value;
        }
    }
}
