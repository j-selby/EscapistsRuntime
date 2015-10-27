package net.jselby.escapists.data.objects;

import net.jselby.escapists.util.ByteReader;

/**
 * A backdrop is a image in the background.
 */
public class Backdrop extends ObjectDefinitionProperties {
    public int size;

    public short obstacleType;
    public short collisionMode;

    public int width;
    public int height;

    public short image;


    @Override
    public void read(ByteReader buffer, int length) {
        size = buffer.getInt();
        obstacleType = buffer.getShort();
        collisionMode = buffer.getShort();
        width = buffer.getInt();
        height = buffer.getInt();
        image = buffer.getShort();
    }
}
