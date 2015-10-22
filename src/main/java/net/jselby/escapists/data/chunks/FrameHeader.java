package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

import java.awt.*;

/**
 * The FrameHeader is a set of basic properties relating to a games frame.
 */
public class FrameHeader extends Chunk {
    private int width;
    private int height;
    private Color background;
    private long flags;

    @Override
    public void init(ByteReader buffer, int length) {
        width = buffer.getInt();
        height = buffer.getInt();
        background = buffer.getColor();
        flags = buffer.getUnsignedInt();
    }
}
