package net.jselby.escapists;

import net.jselby.escapists.data.ChunkType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Created by James on 25/08/2015.
 */
public class ChunkDecoder {
    public static void decodeChunk(ByteBuffer buf) {
        Inflater inflater = new Inflater();

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

            if ((flags & 1) != 0) {
                if ((flags & 2) != 0) {
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
            } else if ((flags & 2) != 0) {
                data = ChunkTransforms.transform(data);
            }

            //System.out.println(type.name() + " (" + id + "): " + data.length + "/" + size + " bytes, " + flags + " flags.");

            if (type == ChunkType.Frame) {
                decodeChunk(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN));
            }

            if (type == ChunkType.FrameName || type == ChunkType.AppName) {
                System.out.println(new String(data));
            } else if (size < 100) {
                //System.out.println(new String(data));

            }

            if (type == ChunkType.GlobalStrings) {
                System.out.println(new String(data));
            }


            /*if (type == ChunkType.AppHeader) {
                ByteBuffer buf1 = ByteBuffer.wrap(data)
            }*/
        }
    }
}
