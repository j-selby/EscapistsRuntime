package net.jselby.escapists.data.objects.sections;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.util.ByteReader;

/**
 * A selection of text for rendering to the screen
 */
public class Text {
    public final int width;
    public final int height;
    public final Paragraph[] paragraphs;

    public Text(ByteReader buffer) {
        int currentPosition = buffer.position();
        int size = buffer.getInt();

        width = buffer.getInt();
        height = buffer.getInt();

        int itemOffsetsCount = buffer.getInt();
        int[] itemOffsets = new int[itemOffsetsCount];
        for (int i = 0; i < itemOffsetsCount; i++) {
            itemOffsets[i] = buffer.getInt();
        }

        paragraphs = new Paragraph[itemOffsetsCount];
        for (int i = 0; i < itemOffsets.length; i++) {
            buffer.position(currentPosition + itemOffsets[i]);
            paragraphs[i] = new Paragraph(buffer);
        }
    }

    public class Paragraph {
        public final int font;
        public final int flags;
        public final Color color;
        public final String value;

        public Paragraph(ByteReader buffer) {
            font = buffer.getUnsignedShort();
            flags = buffer.getUnsignedShort();
            color = buffer.getColor();
            value = buffer.getString();
            System.out.println(value + ":" + color);
        }
    }
}
