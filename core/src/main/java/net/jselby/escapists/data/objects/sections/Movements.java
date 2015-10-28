package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * A set of movements for an object.
 */
public class Movements {
    private final Movement[] items;

    public Movements(ByteReader buffer) {
        int rootPosition = buffer.position();

        long count = buffer.getUnsignedInt();
        items = new Movement[(int) count];

        int currentPosition = buffer.position();

        for (int i = 0; i < count; i++) {
            items[i] = new Movement(buffer, rootPosition);
            buffer.position(currentPosition + 16);
            currentPosition = buffer.position();
        }
    }

    private class Movement {
        private final short player;

        private final short type;
        private final MovementTypes typeEnum;

        private final byte movingAtStart;
        private final int directionAtStart;

        public Movement(ByteReader buffer, int rootPosition) {
            int nameOffset = buffer.getInt();
            int movementId = buffer.getInt();
            int newOffset = buffer.getInt();
            int dataSize = buffer.getInt();

            buffer.position(rootPosition + newOffset);

            player = buffer.getShort();
            type = buffer.getShort();
            typeEnum = MovementTypes.getTypeById(type);
            movingAtStart = buffer.getByte();

            buffer.skipBytes(3);

            directionAtStart = buffer.getInt();

            if (typeEnum == MovementTypes.Extension) {
                buffer.skipBytes(14);
                dataSize -= 14;
            }

            // TODO: Implement movement types

            // TODO: Finish extensions
        }
    }

    public enum MovementTypes {
        Static(0),
        Mouse(1),
        Race(2),
        EightDirections(3),
        Ball(4),
        Path(5),
        Platform(9),
        Extension(14);

        private int id;

        MovementTypes(int id) {
            this.id = id;
        }

        public static MovementTypes getTypeById(int id) {
            for (MovementTypes type : values()) {
                if (type.id == id) {
                    return type;
                }
            }

            return null;
        }
    }
}