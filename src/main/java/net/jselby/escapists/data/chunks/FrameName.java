package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.StringChunk;

/**
 * A FrameName is... the name of the frame.
 */
public class FrameName extends StringChunk {
    @Override
    protected String getEncodingType() {
        return "UTF-8";
    }
}
