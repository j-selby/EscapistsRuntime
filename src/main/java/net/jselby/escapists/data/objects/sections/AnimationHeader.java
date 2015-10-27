package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * The header of an animation.
 */
public class AnimationHeader {
    public AnimationHeader(ByteReader buffer) {
        int position = buffer.position();

        short size = buffer.getShort();
        short count = buffer.getShort();

        short[] offsets = new short[count];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = buffer.getShort();
        }

        // TODO: Finish animations
        /*
        self.loadedAnimations = animationDict = {}
        for index, offset in enumerate(offsets):
            if offset != 0:
                reader.seek(currentPosition + offset)
                animationDict[index] = self.new(Animation, reader,
                    index = index)

        for index in xrange(count):
            self.items.append(getClosestAnimation(index, animationDict, count))
         */
    }
}