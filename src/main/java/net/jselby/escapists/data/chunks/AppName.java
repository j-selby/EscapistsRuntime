package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.StringChunk;

import java.nio.ByteBuffer;

/**
 * The AppName is the name of the application.
 */
public class AppName extends StringChunk {
    @Override
    protected String getEncodingType() {
        return "UTF-8";
    }
}
