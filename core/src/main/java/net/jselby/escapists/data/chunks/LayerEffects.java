package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The LayerEffects chunk is a set of LayerEffects applied on a particular layer.
 */
public class LayerEffects extends Chunk {
    @Override
    public void init(ByteReader buffer, int length) {
        LayerEffect[] effects = new LayerEffect[length / 20];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = new LayerEffect(buffer);
        }
    }

    /**
     * A LayerEffect is a special effect applied upon a particular layer.
     */
    private class LayerEffect {
        public final long inkEffect;
        public final long inkEffectValue;

        public LayerEffect(ByteReader buffer) {
            long pInkEffect = buffer.getUnsignedInt();
            inkEffect = pInkEffect & 0xFFFF;
            inkEffectValue = buffer.getUnsignedInt();
        }
    }
}
