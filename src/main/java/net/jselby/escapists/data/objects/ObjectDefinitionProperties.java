package net.jselby.escapists.data.objects;

import net.jselby.escapists.util.ByteReader;

import java.nio.ByteBuffer;

/**
 * The properties of an object, implemented by the appropiate ObjectType.
 *
 * @author j_selby
 */
public abstract class ObjectDefinitionProperties {
    /**
     * Initialises this object definition's properties with a ByteBuffer.
     * @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    public abstract void read(ByteReader buffer, int length);
}
