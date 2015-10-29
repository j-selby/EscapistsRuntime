package net.jselby.escapists.data.chunks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;
import net.jselby.escapists.util.CompressionUtils;
import org.mini2Dx.core.graphics.Sprite;

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

        private final com.badlogic.gdx.graphics.Color transparent;
        public final org.mini2Dx.core.graphics.Sprite image;

        public int handle;

        public ImageItem(ByteReader buffer) {
            handle = buffer.getInt();

            // Decompress
            buffer = CompressionUtils.decompress(buffer);

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
            int transparentVal = Color.rgba8888(transparent);
            //System.out.println(Long.toBinaryString(flags) + ":" + flags);
            boolean alpha = ((flags >> 4) & 1) != 0;

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
            Pixmap image = new Pixmap(width, height, Pixmap.Format.RGBA8888);

            int originalPos = buffer.position();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    buffer.position(originalPos + n);
                    int b = buffer.getUnsignedByte();
                    int g = buffer.getUnsignedByte();
                    int r = buffer.getUnsignedByte();
                    int val =  Color.rgba8888(r / 256f, g / 256f, b / 256f, 1);
                    if (val != transparentVal/* && !alpha*/) {
                        image.drawPixel(x, y, val);
                    }// else {
                        //buf[i] = new Color(((float) r) / 256f, ((float) g) / 256f,
                        //        ((float) b) / 256f, 1f);
                    //}

                    i++;
                    n += 3;
                }
                n += 3 * pad;
            }

            buffer.position(originalPos + (height * width * 3));

            /*if (alpha) { // Alpha
                i = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // TODO: Implement alpha, if needed
                        int a = buffer.getUnsignedByte();
                        if (!buf[i].equals(transparent)) {
                            image.setRGB(x, y, val);
                        }
                    }
                }
            }*.

            /*try {
                ImageIO.write(image, "png", new File("images/" + handle + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            this.image = new Sprite(new Texture(image));
        }
    }
}
