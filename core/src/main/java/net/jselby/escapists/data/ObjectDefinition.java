package net.jselby.escapists.data;

import net.jselby.escapists.data.chunks.ObjectProperties;

/**
 * An object definition is the raw definition of a object, including name, id, etc.
 *
 * @author j_selby
 */
public class ObjectDefinition {
    public String name;

    public short handle;
    public short objectType;

    public int headerFlags;
    public short headerReserved;

    public long inkEffect;
    public long inkEffectParameter;

    public boolean transparent;

    public long shaderId;
    public byte[][] shaderItems;
    public ObjectProperties properties;

    @Override
    public String toString() {
        return name;
    }
}
