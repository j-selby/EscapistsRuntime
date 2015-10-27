package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The header properties of an object.
 */
public class ObjectHeader extends Chunk {
    private short handle;
    private short objectType;

    private int flags;
    private short reserved;

    private long inkEffect;
    private long inkEffectParameter;

    @Override
    public void init(ByteReader buffer, int length) {
        handle = buffer.getShort();
        objectType = buffer.getShort();
        flags = buffer.getUnsignedShort();
        reserved = buffer.getShort(); // Not used
        inkEffect = buffer.getUnsignedInt();
        inkEffectParameter = buffer.getUnsignedInt();
    }
}
