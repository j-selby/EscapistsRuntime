package net.jselby.escapists;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ChunkType;
import net.jselby.escapists.util.ByteReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    /**
     * Decodes a series of Chunks.
     * @param buf The buffer to read from
     * @return A parsed list of Chunks
     */
    public static List<Chunk> decodeChunk(ByteReader buf) {
        Inflater inflater = new Inflater();
        ArrayList<Chunk> chunks = new ArrayList<>();

        while(true) {
            int id = buf.getShort();
            ChunkType type = ChunkType.getTypeForID(id);
            int flags = buf.getShort();
            int size = buf.getInt();

            byte[] data = buf.getBytes(size);

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

                Chunk chunk = potentialChunkDef.asSubclass(Chunk.class).newInstance();
                chunk.init(new ByteReader(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)), data.length);
                chunks.add(chunk);
            } catch (ClassNotFoundException e) {
                if (type == ChunkType.Unknown) {
                    System.err.printf("Failed to find a chunk representation for ID: %d.\n", id);
                } else {
                    System.err.printf("Failed to find a chunk representation for \"%s\" (ID: %d).\n", type.name(), id);
                }
                /*try {
                    FileOutputStream out = new FileOutputStream("Sections" + File.separator + type.name());
                    out.write(data);
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }*/
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.printf("Failed to create chunk representation for \"%s\" (ID: %d): ", type.name(), id);
                e.printStackTrace();
            }
        }

        return chunks;
    }
}
