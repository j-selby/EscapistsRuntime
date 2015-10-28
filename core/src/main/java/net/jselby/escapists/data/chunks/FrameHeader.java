package net.jselby.escapists.data.chunks;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The FrameHeader is a set of basic properties relating to a games frame.
 */
public class FrameHeader extends Chunk {
    public int width;
    public int height;
    public Color background;
    public long flags;

    @Override
    public void init(ByteReader buffer, int length) {
        width = buffer.getInt();
        height = buffer.getInt();
        background = buffer.getColor();
        flags = buffer.getUnsignedInt();
    }
}
