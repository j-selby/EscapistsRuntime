package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The Layers object represents different visual layers in a frame.
 */
public class Layers extends Chunk {
    public Layer[] layers;

    @Override
    public void init(ByteReader buffer, int length) {
        layers = new Layer[(int) buffer.getUnsignedInt()];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(buffer);
        }
    }

    /**
     * A Layer is a layer within a set of Layers.
     */
    public class Layer {
        public final long flags;

        public final float xCoefficient;
        public final float yCoefficient;

        public final int numberOfBackgrounds;
        public final int backgroundIndex;

        public final String name;

        public Layer(ByteReader buffer) {
            flags = buffer.getUnsignedInt();
            xCoefficient = buffer.getFloat();
            yCoefficient = buffer.getFloat();
            numberOfBackgrounds = buffer.getInt();
            backgroundIndex = buffer.getInt();
            name = buffer.getString();
        }
    }
}
