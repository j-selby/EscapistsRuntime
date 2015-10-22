package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.StringChunk;

/**
 * The TargetFilename is the filename used when exporting the file.
 */
public class TargetFilename extends StringChunk {
    @Override
    protected String getEncodingType() {
        return "UTF-8";
    }
}
