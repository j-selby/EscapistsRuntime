package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * A counter is a set of numbers used to display some sort of value to the player.
 */
public class Counter {
    public final int initial;
    public final int minimum;
    public final int maximum;

    public Counter(ByteReader buffer) {
        buffer.getShort(); // Size

        initial = buffer.getInt();
        minimum = buffer.getInt();
        maximum = buffer.getInt();

        /*
        width = buffer.getUnsignedInt();
        height = buffer.getUnsignedInt();

        player = buffer.getUnsignedShort();
        displayType = DisplayTypes.values()[buffer.getShort()];

        flags = buffer.getShort();


    private final long width;
    private final long height;

    private final int player;
    private final DisplayTypes displayType;

    private final short flags;

    private final int font;

    private short[] frames;
    private Shape shape;

        self.integerDigits = self.flags & INT_DIGITS_MASK
        self.formatFloat = self.flags & FORMAT_FLOAT != 0
        self.floatDigits = ((self.flags & FLOAT_DIGITS_MASK
            ) >> FLOAT_DIGITS_SHIFT) + 1
        self.useDecimals = self.flags & USE_DECIMALS != 0
        self.decimals = ((self.flags & FLOAT_DECIMALS_MASK
            ) >> FLOAT_DECIMALS_SHIFT)
        self.addNulls = self.flags & FLOAT_PAD != 0
        self.inverse = byteflag.getFlag(self.flags, 8)

        font = buffer.getUnsignedShort();

        if (displayType == DisplayTypes.Numbers || displayType == DisplayTypes.Animation) {
            int framesLength = buffer.getUnsignedShort();
            frames = new short[framesLength];
            for (int i = 0; i < framesLength; i++) {
                frames[i] = buffer.getShort();
            }
        } else if (displayType == DisplayTypes.VerticalBar || displayType == DisplayTypes.HorizontalBar
                || displayType == DisplayTypes.Text) {
            shape = new Shape(buffer);
        }


    public enum DisplayTypes {
        Hidden,
        Numbers,
        VerticalBar,
        HorizontalBar,
        Animation,
        Text
    }

        */
    }
}