package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The extended header is an extension of the AppHeader, containing additional metadata relating to the game.
 */
public class ExtendedHeader extends Chunk {
    // Boolean matrix
    private int flags;

    // Build fields
    private long buildType;
    private long buildFlags;

    // Screen properties
    private short screenRatioTolerance;
    private short screenAngle;

    @Override
    public void init(ByteReader buffer, int length) {
        flags = buffer.getInt();

        buildType = ((long) buffer.getInt() & 0xffffffffL);
        buildFlags = ((long) buffer.getInt() & 0xffffffffL);

        screenRatioTolerance = buffer.getShort();
        screenAngle = buffer.getShort();
    }
}
