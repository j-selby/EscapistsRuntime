package net.jselby.escapists.data.objects;

import net.jselby.escapists.data.Shape;
import net.jselby.escapists.util.ByteReader;

/**
 * A quick backdrop is a backdrop done quick... lol
 */
public class QuickBackdrop extends ObjectDefinitionProperties {
    public long size;

    public short obstacleType;
    public short collisionMode;

    public int width;
    public int height;

    public Shape shape;

    @Override
    public void read(ByteReader buffer, int length) {
        size = buffer.getUnsignedInt();
        obstacleType = buffer.getShort();
        collisionMode = buffer.getShort();
        width = buffer.getInt();
        height = buffer.getInt();
        shape = new Shape(buffer);
    }
}
