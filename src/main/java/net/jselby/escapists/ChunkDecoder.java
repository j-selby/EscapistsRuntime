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

            int compressedSize;
            int decompressedSize = -1;
            if ((flags & 1) != 0) {
                decompressedSize = buf.getInt();
                compressedSize = buf.getInt();
            }

            byte[] data = new byte[size - (decompressedSize == -1 ? 0 : 8)];
            buf.get(data);


            if (type == ChunkType.Last) {
                break;
            }

            if ((flags & 1) != 0) {
                if ((flags & 2) != 0) {
                    System.out.printf("Unable to transform chunk %d (%s) (size: %d bytes).\n", id, type.name(), data.length);
                    continue;
                } else {
                    try {
                        byte[] decompData = new byte[decompressedSize];
                        inflater.reset();
                        inflater.setInput(data);
                        inflater.inflate(decompData);
                        data = decompData;
                    } catch (DataFormatException e) {
                        System.out.printf("Failed to inflate chunk %d: %s.\n", id, e.getMessage());
                    }
                }
            }

            //System.out.println(type.name() + " (" + id + "): " + data.length + "/" + size + " bytes, " + flags + " flags.");

            if (type == ChunkType.Frame) {
                decodeChunk(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN));
            }

            if (type == ChunkType.FrameName) {
                System.out.println(new String(data));
            }

            if (type == ChunkType.ImageBank) {
                //System.out.println(new String(data));
            }

            /*if (type == ChunkType.AppHeader) {
                ByteBuffer buf1 = ByteBuffer.wrap(data)
            }*/
        }
    }
}
