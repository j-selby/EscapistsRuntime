package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The header properties of an object.
 */
public class ObjectHeader extends Chunk {
    public short handle;
    public short objectType;

    public int flags;
    public short reserved;

    public long inkEffect;
    public long inkEffectParameter;

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
