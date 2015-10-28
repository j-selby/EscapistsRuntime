package net.jselby.escapists.data.chunks;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The FramePalette is a set of colors used as a palette for the frame.
 */
public class FramePalette extends Chunk {
    private Color[] color;

    @Override
    public void init(ByteReader buffer, int length) {
        buffer.skipBytes(4);

        color = new Color[256];

        for (int i = 0; i < color.length; i++) {
            color[i] = buffer.getColor();
        }
    }
}
