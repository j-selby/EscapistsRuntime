package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The ObjectInstances chunk is a chunk of instances belonging to a frame.
 */
public class
        ObjectInstances extends Chunk {
    public ObjectInstance[] instances;

    @Override
    public void init(ByteReader buffer, int length) {
        int count = (int) buffer.getUnsignedInt();
        instances = new ObjectInstance[count];

        for (int i = 0; i < count; i++) {
            instances[i] = new ObjectInstance(buffer);
        }
    }

    /**
     * A ObjectInstance is a instance of a object within the game world.
     */
    public class ObjectInstance {
        public final int handle;
        public final int objectInfo;

        public final int x;
        public final int y;

        public final short parentType;
        public final short parentHandle;
        public final short layer;

        public ObjectInstance(ByteReader buffer) {
            handle = buffer.getUnsignedShort();
            objectInfo = buffer.getUnsignedShort();
            x = buffer.getInt();
            y = buffer.getInt();
            parentType = buffer.getShort();
            parentHandle = buffer.getShort(); // Object info
            layer = buffer.getShort();
            buffer.skipBytes(2);
        }

        @Override
        public String toString() {
            return "ObjectInstance{x=" + x + ",y=" + y + ",handle=" + handle + ",objectInfo="
                    + objectInfo + ",parentType=" + parentType + ",parentHandle=" + parentHandle
                    + ",layer=" + layer + "}";
        }
    }
}
