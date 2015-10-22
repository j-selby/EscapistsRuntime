package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The ExtData is primarily a legacy chunk for older game builds.
 */
public class ExtData extends Chunk {
    @Override
    public void init(ByteReader buffer, int length) {
        int check = buffer.getInt();
        if (check != 0) {
            System.err.printf("Invalid check value in ExtData: %d.\n", check);
        }
    }
}
