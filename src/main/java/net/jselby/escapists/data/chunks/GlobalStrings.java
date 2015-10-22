package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;

import java.nio.ByteBuffer;

/**
 * The GlobalStrings chunk contains strings used during execution.
 */
public class GlobalStrings extends Chunk {
    private String[] data;

    @Override
    public void init(ByteBuffer buffer, int length) {
        long l = ((long) buffer.getInt() & 0xffffffffL);
        data = new String[(int) l];

        for (int i = 0; i < l; i++) {
            StringBuilder builder = new StringBuilder();
            while(true) {
                char character = buffer.getChar();
                if (character == '\0') {
                    break;
                }
                builder.append(character);
            }
            data[i] = builder.toString();
        }
    }
}
