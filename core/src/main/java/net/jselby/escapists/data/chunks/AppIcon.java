package net.jselby.escapists.data.chunks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

import java.nio.LongBuffer;
import java.util.BitSet;

/**
 * The AppIcon is a 16*16 icon for the application.
 */
public class AppIcon extends Chunk {
    public Pixmap image;

    @Override
    public void init(ByteReader buffer, int length) {
        buffer.position(buffer.getInt() + buffer.position() - 4);
        image = new Pixmap(16, 16, Pixmap.Format.RGBA8888);

        Color[] colorIndexes = new Color[16 * 16];

        for (int i = 0; i < 16 * 16; i++) {
            // Read RGB set (in reverse)
            int b = buffer.getUnsignedByte();
            int g = buffer.getUnsignedByte();
            int r = buffer.getUnsignedByte();
            buffer.skipBytes(1);
            colorIndexes[i] = new Color(((float) r) / 256f, ((float) g) / 256f,
                    ((float) b) / 256f, 1f);
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
                points[index] = new Color(oldColor.r, oldColor.g,
                        oldColor.b, set.get(ii) ? 1f : 0f);
                index++;
            }
        }

        // Finally, paint
        i = 0;
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                image.drawPixel(x, y, points[i].toIntBits());
                i++;
            }
        }
    }
}
