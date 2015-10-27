package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * A set of values associated with an object.
 */
public class AlterableValues {
    private final int[] values;

    public AlterableValues(ByteReader buffer) {
        int count = buffer.getUnsignedShort();

        values = new int[count];
        for (int i = 0; i < count; i++) {
            values[i] = buffer.getInt();
        }
    }
}
