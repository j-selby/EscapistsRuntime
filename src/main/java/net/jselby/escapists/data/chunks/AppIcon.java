package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;

import javax.imageio.ImageIO;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * The AppIcon is a 16*16 icon for the application.
 */
public class AppIcon extends Chunk {
    @Override
    public void init(ByteBuffer buffer, int length) {
        buffer.position(buffer.getInt() + buffer.position() - 4);
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        List<Color> color = new ArrayList<>();

        for (int i = 0; i < 16 * 16; i++) {
            // Read RGB set (in reverse)
            int b = ((int) buffer.get()) & 0xFF;
            int g = ((int) buffer.get()) & 0xFF;
            int r = ((int) buffer.get()) & 0xFF;
            buffer.position(buffer.position() + 1);
            color.add(new Color(r, g, b));
        }

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                // Set RGB from position set
                image.setRGB(x, 15 - y, color.get(((int) buffer.get()) & 0xFF).getRGB());
            }
        }

        try {
            ImageIO.write(image, "png", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
