package net.jselby.escapists.util;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Utilities for decompressing chunks.
 *
 * @author j_selby
 */
public class CompressionUtils {
    public static ByteReader decompress(ByteReader buffer) {
        // Decompress
        Inflater inflater = new Inflater();

        int decompressedSize = (int) buffer.getUnsignedInt();
        int compressedData = (int) buffer.getUnsignedInt();
        byte[] data = new byte[compressedData];
        buffer.getBytes(data);

        try {
            byte[] decompData = new byte[decompressedSize];
            inflater.reset();
            inflater.setInput(data);
            inflater.inflate(decompData);
            buffer = new ByteReader(decompData);
        } catch (DataFormatException e) {
            System.out.printf("Failed to inflate ImageItem: %s.\n", e.getMessage());
            throw new IllegalStateException(e);
        }

        return buffer;
    }
}
