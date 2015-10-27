package net.jselby.escapists.data.objects;

import net.jselby.escapists.util.ByteReader;

/**
 * A backdrop is a image in the background.
 */
public class Backdrop extends ObjectDefinitionProperties {
    private int size;

    private short obstacleType;
    private short collisionMode;

    private int width;
    private int height;

    private short image;


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
