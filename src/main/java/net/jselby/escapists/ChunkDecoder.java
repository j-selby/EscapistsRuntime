package net.jselby.escapists;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ChunkType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Decodes chunks into a List.
 *
 * @author j_selby
 */
public class ChunkDecoder {
    public static List<Chunk> decodeChunk(ByteBuffer buf) {
        Inflater inflater = new Inflater();
        ArrayList<Chunk> chunks = new ArrayList<>();

        while(true) {
            int id = buf.getShort();
            ChunkType type = ChunkType.getTypeForID(id);
            int flags = buf.getShort();
            int size = buf.getInt();

            byte[] data = new byte[size];
            buf.get(data);

            if (type == ChunkType.Last) {
                break;
            }

            if ((flags & 1) != 0) { // Compression
                if ((flags & 2) != 0) { // Encryption
                    byte[] dataCopy = new byte[data.length - 4];
                    System.arraycopy(data, 4, dataCopy, 0, data.length - 4);
                    dataCopy = ChunkTransforms.transform(dataCopy);
                    System.arraycopy(dataCopy, 0, data, 4, data.length - 4);
                }

                ByteBuffer decompressionSizing = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                int decompressedSize = decompressionSizing.getInt();
                decompressionSizing.getInt(); // Compressed size
                data = new byte[size - 8];
                decompressionSizing.get(data);

                try {
                    byte[] decompData = new byte[decompressedSize];
                    inflater.reset();
                    inflater.setInput(data);
                    inflater.inflate(decompData);
                    data = decompData;
                } catch (DataFormatException e) {
                    System.out.printf("Failed to inflate chunk %d (%s): %s.\n", id, type.name(), e.getMessage());
                }
            } else if ((flags & 2) != 0) {  // Encryption
                data = ChunkTransforms.transform(data);
            }

            //System.out.println(type.name() + " (" + id + "): " + data.length + "/" + size + " bytes, " + flags + " flags.");

            // Attempt to find a copy
            try {
                Class<?> potentialChunkDef = Class.forName("net.jselby.escapists.data.chunks." + type.name());
                if (potentialChunkDef.getGenericSuperclass() == Chunk.class) {
                    // Create a instance of this, it worked out!
                    Chunk chunk = potentialChunkDef.asSubclass(Chunk.class).newInstance();
                    chunk.init(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN), data.length);
                    chunks.add(chunk);
                } else {
                    throw new ClassNotFoundException();
                }
            } catch (ClassNotFoundException e) {
                System.err.printf("Failed to find a chunk representation for \"%s\".\n", type.name());
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.printf("Failed to create chunk representation for \"%s\": ", type.name());
                e.printStackTrace();
            }
        }

        return chunks;
    }
}
