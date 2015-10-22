package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.StringChunk;

/**
 * The EditorFilename is the filename of the original project file of the game.
 */
public class EditorFilename extends StringChunk {
    @Override
    protected String getEncodingType() {
        return "UTF-8";
    }
}
