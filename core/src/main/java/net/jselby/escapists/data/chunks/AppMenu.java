package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * Items and other metadata for the AppMenu.
 */
public class AppMenu extends Chunk {
    @Override
    public void init(ByteReader buffer, int length) {
        int currentPosition = buffer.position();
        // TODO: Implement
        /*int headerSize (int) ((long) bb.getInt() & 0xffffffffL);
        currentPosition = reader.tell()
        headerSize = reader.readInt(True)
        menuOffset = reader.readInt()
        menuSize = reader.readInt()
        if menuSize == 0:
        return
                accelOffset = reader.readInt()
        accelSize = reader.readInt()
        reader.seek(currentPosition + menuOffset)
        reader.skipBytes(4)
        self.loadItems(reader)
        reader.seek(currentPosition + accelOffset)
        for i in range(accelSize / 8):
        self.accelShift.append(Key(reader.readByte()))
        reader.skipBytes(1)
        self.accelKey.append(Key(reader.readShort()))
        self.accelId.append(reader.readShort())
        reader.skipBytes(2)*/
    }
}
