package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.StringChunk;

/**
 * The AppAuthor is the creator of the application.
 */
public class AppAuthor extends StringChunk {
    @Override
    protected String getEncodingType() {
        return "UTF-8";
    }
}
