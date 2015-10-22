package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * Other extensions are a string list of other extensions.
 */
public class OtherExtensions extends Chunk {
    private String[] extensions;

    @Override
    public void init(ByteReader buffer, int length) {
        int size = buffer.getUnsignedShort();
        extensions = new String[size];
        for (int i = 0; i < size; i++) {
            extensions[i] = buffer.getString();
        }
    }
}
