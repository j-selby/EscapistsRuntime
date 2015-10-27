package net.jselby.escapists.data;

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

    public long shaderId;
    public byte[][] shaderItems;

    @Override
    public String toString() {
        return name;
    }
}
