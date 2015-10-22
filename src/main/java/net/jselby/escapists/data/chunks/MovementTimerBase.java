package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The MovementTimerBase chunk is a integer value (timer?)
 */
public class MovementTimerBase extends Chunk {
    private int value;

    @Override
    public void init(ByteReader buffer, int length) {
        value = buffer.getInt();
    }
}
