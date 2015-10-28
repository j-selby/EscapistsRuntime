package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The Extension list is a list of extensions
 */
public class ExtensionList extends Chunk {
    private Extension[] extensions;

    @Override
    public void init(ByteReader buffer, int length) {
        int num = buffer.getUnsignedShort();
        int preloadExtensions = buffer.getUnsignedShort();

        extensions = new Extension[num];
        for (int i = 0; i < num; i++) {
            extensions[i] = new Extension(buffer);
        }
    }

    /**
     * A extension is a executable library used by the engine.
     */
    private class Extension {
        public final short handle;
        public final int magicNumber;
        public final int versionLS;
        public final int versionMS;
        public final String name;
        public final String subType;

        public Extension(ByteReader buffer) {
            int currentPosition = buffer.position();
            int size = buffer.getShort();
            if (size < 0){
                size = -size;
            }

            handle = buffer.getShort();
            magicNumber = buffer.getInt();
            versionLS = buffer.getInt();
            versionMS = buffer.getInt();
            name = buffer.getString();
            subType = buffer.getString();
            buffer.position(currentPosition + size);
        }
    }
}
