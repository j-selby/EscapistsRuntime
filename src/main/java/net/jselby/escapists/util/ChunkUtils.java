package net.jselby.escapists.util;

import net.jselby.escapists.data.Chunk;

import java.util.List;

/**
 * Various utilities for manipulating Chunks and their lists.
 *
 * @author j_selby
 */
public class ChunkUtils {
    public static Chunk popChunk(List<Chunk> chunks, Class<? extends Chunk> targetClass) {
        Chunk foundChunk = null;
        for (Chunk chunk : chunks) {
            if (chunk.getClass().equals(targetClass)) {
                foundChunk = chunk;
                break;
            }
        }

        if (foundChunk == null) {
            return null;
        }

        chunks.remove(foundChunk);
        return foundChunk;
    }

    public static Chunk getChunk(List<Chunk> chunks, Class<? extends Chunk> targetClass) {
        Chunk foundChunk = null;
        for (Chunk chunk : chunks) {
            if (chunk.getClass().equals(targetClass)) {
                foundChunk = chunk;
                break;
            }
        }

        return foundChunk;
    }
}
