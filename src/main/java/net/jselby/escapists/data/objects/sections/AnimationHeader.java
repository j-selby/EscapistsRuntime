package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * The header of an animation.
 */
public class AnimationHeader {
    public final Animation[] loadedAnimations;

    public AnimationHeader(ByteReader buffer) {
        int position = buffer.position();

        short size = buffer.getShort();
        short count = buffer.getShort();

        short[] offsets = new short[count];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = buffer.getShort();
        }

        loadedAnimations = new Animation[count];
        for (int i = 0; i < count; i++) {
            if (offsets[i] != 0) {
                buffer.position(position + offsets[i]);
                loadedAnimations[i] = new Animation(buffer);
            }
        }

        // TODO: GetClosestAnimation
        /*
        for index in xrange(count):
            self.items.append(getClosestAnimation(index, animationDict, count))
         */
    }

    private class Animation {
        public Animation(ByteReader buffer) {
            // TODO: Implement animation class
        }
    }
}