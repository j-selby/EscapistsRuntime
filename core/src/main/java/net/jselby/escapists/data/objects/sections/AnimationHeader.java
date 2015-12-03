package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * The header of an animation.
 */
public class AnimationHeader {
    public final Animation[] loadedAnimations;

    public AnimationHeader(ByteReader buffer) {
        int position = buffer.getPosition();

        short size = buffer.getShort();
        short count = buffer.getShort();

        short[] offsets = new short[count];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = buffer.getShort();
        }

        loadedAnimations = new Animation[count];
        for (int i = 0; i < count; i++) {
            if (offsets[i] != 0) {
                buffer.setPosition(position + offsets[i]);
                loadedAnimations[i] = new Animation(buffer, i);
            }
        }

        // TODO: GetClosestAnimation
        /*
        for index in xrange(count):
            self.items.append(getClosestAnimation(index, animationDict, count))
         */
    }

    public class Animation {
        public final AnimationDirection[] localDirections;

        private int index;

        public Animation(ByteReader buffer, int index) {
            this.index = index;

            int currentPosition = buffer.getPosition();

            short[] offsets = new short[32];
            for (int i = 0; i < offsets.length; i++) {
                offsets[i] = buffer.getShort();
            }

            localDirections = new AnimationDirection[32];
            for (int i = 0; i < offsets.length; i++) {
                int offset = offsets[i];
                if (offset != 0) {
                    buffer.setPosition(currentPosition + offset);
                    localDirections[i] = new AnimationDirection(buffer, index);
                }
            }

            /*
            // TODO: Sort by direction
        for index in xrange(32):
            self.directions.append(getClosestDirection(index,
                directionDict))
             */
        }
    }

    /**
     * A set of animations for a particular direction of a animation.
     */
    public class AnimationDirection {
        public short minSpeed;
        public short maxSpeed;
        public boolean hasSingle = false;

        public short repeat;
        public short backTo;

        public short[] frames;

        public AnimationDirection(ByteReader buffer, int index) {
            minSpeed = buffer.getUnsignedByte();
            maxSpeed = buffer.getUnsignedByte();

            if (index == 0 || index == 3
                    || index == 4 || index == 6) { // Has a single speed
                minSpeed = maxSpeed;
                hasSingle = true;
            }

            repeat = buffer.getShort();
            backTo = buffer.getShort();

            int framesLength = buffer.getShort();
            frames = new short[framesLength];
            for (int i = 0; i < frames.length; i++) {
                frames[i] = buffer.getShort();
            }
        }
    }
}