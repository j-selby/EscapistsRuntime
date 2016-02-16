package net.jselby.escapists.data;

import net.jselby.escapists.data.chunks.ReflectionsHandle;
import net.jselby.escapists.util.ByteReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
        ExecutorService threadManager = Executors.newCachedThreadPool();

        Inflater inflater = new Inflater();
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();

        while(true) {
            int id = buf.getShort();
            ChunkType type = ChunkType.getTypeForID(id);
            int flags = buf.getShort();
            int size = buf.getInt();

            if (type == ChunkType.Last) {
                if (size != 0) {
                    buf.skipBytes(size);
                }
                break;
            }

            byte[] data = buf.getBytes(size);

            if ((flags & 1) != 0) { // Compression
                if ((flags & 2) != 0) { // Encryption
                    byte[] dataCopy = new byte[size - 4];
                    System.arraycopy(data, 4, dataCopy, 0, size - 4);
                    dataCopy = ChunkTransforms.transform(dataCopy);
                    System.arraycopy(dataCopy, 0, data, 4, size - 4);
                }

                ByteReader decompressionSizing = new ByteReader(data);
                int decompressedSize = decompressionSizing.getInt();
                decompressionSizing.getInt(); // Compressed size
                data = decompressionSizing.getBytes(size - decompressionSizing.getPosition());

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

            // TODO: Clean up this mess, create our own runnable tracker
            // Attempt to find a copy
            try {
                Class<?> potentialChunkDef = Class.forName(ReflectionsHandle.class.getPackage().getName() + "." + type.name());

                final Chunk chunk = potentialChunkDef.asSubclass(Chunk.class).newInstance();
                final byte[] finalData = data;

                threadManager.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chunk.init(new ByteReader(ByteBuffer.wrap(finalData).order(ByteOrder.LITTLE_ENDIAN)), finalData.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                chunks.add(chunk);
            } catch (ClassNotFoundException e) {
                if (type == ChunkType.Unknown) {
                    System.err.printf("Failed to find a chunk representation for ID: %d.\n", id);
                } else {
                    System.err.printf("Failed to find a chunk representation for \"%s\" (ID: %d).\n", type.name(), id);
                }
            } catch (InstantiationException e) {
                System.err.printf("Failed to create chunk representation for \"%s\" (ID: %d): ", type.name(), id);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.err.printf("Failed to create chunk representation for \"%s\" (ID: %d): ", type.name(), id);
                e.printStackTrace();
            }
        }

        try {
            threadManager.shutdown();
            threadManager.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return chunks;
    }
}
