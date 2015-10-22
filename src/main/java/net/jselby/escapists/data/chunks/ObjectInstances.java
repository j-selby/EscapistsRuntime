package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The ObjectInstances chunk is a chunk of instances belonging to a frame.
 */
public class ObjectInstances extends Chunk {
    private ObjectInstance[] instances;

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
    private class ObjectInstance {
        private final int handle;
        private final int objectInfo;

        private final int x;
        private final int y;

        private final short parentType;
        private final short parentHandle;
        private final short layer;

        public ObjectInstance(ByteReader buffer) {
            handle = buffer.getUnsignedShort();
            objectInfo = buffer.getUnsignedShort();
            x = buffer.getInt();
            y = buffer.getInt();
            parentType = buffer.getShort();
            parentHandle = buffer.getShort(); // Object info
            layer = buffer.getShort();
        }
    }
}
