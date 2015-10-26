package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * A ImageBank is a store of images.
 */
public class ImageBank extends Chunk {
    public ImageItem[] images;

    @Override
    public void init(ByteReader buffer, int length) {
        int numberOfItems = buffer.getInt();

        images = new ImageItem[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            images[i] = new ImageItem(buffer);
        }
    }

    /**
     * A ImageItem is a Image from a ImageBank.
     */
    public class ImageItem {
        private final int checksum;
        private final int references;

        private final short width;
        private final short height;

        private final byte graphicMode;

        private final short flags;

        private final short xHotspot;
        private final short yHotspot;

        private final short actionX;
        private final short actionY;

        private final Color transparent;
        public final BufferedImage image;

        public int handle;

        public ImageItem(ByteReader buffer) {
            handle = buffer.getInt();

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

            int start = buffer.position();

            checksum = buffer.getInt();
            references = buffer.getInt();
            int size = (int) buffer.getUnsignedInt();

            width = buffer.getShort();
            height = buffer.getShort();

            graphicMode = buffer.getByte();

            flags = buffer.getUnsignedByte();

            if (flags != 16 && flags != 0) { // Only alpha or nothing
                throw new IllegalStateException("Unimplemented: Flag set (" + flags + ")");
            }

            buffer.skipBytes(2); // imgNotUsed

            xHotspot = buffer.getShort();
            yHotspot = buffer.getShort();

            actionX = buffer.getShort();
            actionY = buffer.getShort();

            transparent = buffer.getColor();
            boolean alpha = (flags & ((short) Math.pow(2, /* index */ 4))) != 0;

            if (graphicMode != 4) {
                throw new IllegalStateException("Unimplemented: Graphics mode != 4 (" + graphicMode + ")");
            }

            Color[] buf = new Color[width * height];
            int pad = 2 - ((width * 3) % 2);
            if (pad == 2) {
                pad = 0;
            }
            pad = (int) Math.ceil(pad / (float)3);
            int i = 0;
            int n = 0;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            int originalPos = buffer.position();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    buffer.position(originalPos + n);
                    int b = buffer.getUnsignedByte();
                    int g = buffer.getUnsignedByte();
                    int r = buffer.getUnsignedByte();
                    buf[i] = new Color(r, g, b);
                    if (!buf[i].equals(transparent)) {
                        image.setRGB(x, y, buf[i].getRGB());
                    }

                    i++;
                    n += 3;
                }
                n += 3 * pad;
            }

            //buffer.position(originalPos + (height * width * 3));

            /*if (alpha) { // Alpha
                i = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // TODO: Implement alpha, if needed
                        int a = buffer.getUnsignedByte();
                        buf[i] = new Color(buf[i].getRed(), buf[i].getGreen(), buf[i].getBlue(), a);
                        image.setRGB(x, y, buf[i].getRGB());
                    }
                }
            }*/

            /*try {
                ImageIO.write(image, "png", new File("images/" + handle + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            this.image = image;
        }
    }
}
