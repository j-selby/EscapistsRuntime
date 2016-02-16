package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * A SecNum is a secure set of tick integers.
 */
public class SecNum extends Chunk {
    public int tickCount;
    public int serialSlice;

    @Override
    public void init(ByteReader buffer, int length) {
        int eax = buffer.getInt();
        int ecx = buffer.getInt();

        tickCount = eax ^ 0xBD75329;

        serialSlice = ecx + eax;
        serialSlice ^= 0xF75A3F;
        serialSlice ^= eax;
        serialSlice -= 10;
    }
}
