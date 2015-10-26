package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

import javax.imageio.ImageIO;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The AppIcon is a 16*16 icon for the application.
 */
public class AppIcon extends Chunk {
    public BufferedImage image;

    @Override
    public void init(ByteReader buffer, int length) {
        buffer.position(buffer.getInt() + buffer.position() - 4);
        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        Color[] colorIndexes = new Color[16 * 16];

        for (int i = 0; i < 16 * 16; i++) {
            // Read RGB set (in reverse)
            int b = buffer.getUnsignedByte();
            int g = buffer.getUnsignedByte();
            int r = buffer.getUnsignedByte();
            buffer.skipBytes(1);
            colorIndexes[i] = new Color(r, g, b);
        }

        Color[] points = new Color[16 * 16];
        int i = 0;
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                points[16 * 16 - i - 1] = colorIndexes[buffer.getUnsignedByte()];
                i++;
            }
        }

        int index = 0;
        for (i = 0; i < 16 * 16 / 8; i++) {
            BitSet set = BitSet.valueOf(LongBuffer.allocate(buffer.getUnsignedByte()));
            for (int ii = set.length() - 1; ii >= 0; ii--) {
                Color oldColor = points[index];
                points[index] = new Color(oldColor.getRed(), oldColor.getGreen(),
                        oldColor.getBlue(), set.get(ii) ? 255 : 0);
                index++;
            }
        }

        // Finally, paint
        i = 0;
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                image.setRGB(x, y, points[i].getRGB());
                i++;
            }
        }
    }
}
