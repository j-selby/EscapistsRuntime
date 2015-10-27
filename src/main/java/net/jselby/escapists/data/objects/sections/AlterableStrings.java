package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * A set of strings associated with an object.
 */
public class AlterableStrings {
    public String[] strings;

    public AlterableStrings(ByteReader buffer) {
        int count = buffer.getUnsignedShort();

        strings = new String[count];
        for (int i = 0; i < count; i++) {
            strings[i] = buffer.getString();
        }
    }
}
