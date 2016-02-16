package net.jselby.escapists.data.objects.sections;

import net.jselby.escapists.util.ByteReader;

/**
 * A set of movements for an object.
 */
public class Movements {
    public final Movement[] items;

    public Movements(ByteReader buffer) {
        int rootPosition = buffer.getPosition();

        long count = buffer.getUnsignedInt();
        items = new Movement[(int) count];

        int currentPosition = buffer.getPosition();

        for (int i = 0; i < count; i++) {
            items[i] = new Movement(buffer, rootPosition);
            buffer.setPosition(currentPosition + 16);
            currentPosition = buffer.getPosition();
        }
    }

    private class Movement {
        public final short player;

        public final short type;
        public final MovementTypes typeEnum;

        public final byte movingAtStart;
        public final int directionAtStart;

        public Movement(ByteReader buffer, int rootPosition) {
            buffer.getInt(); // nameOffset
            buffer.getInt(); // movementId
            int newOffset = buffer.getInt();
            buffer.getInt(); // dataSize

            buffer.setPosition(rootPosition + newOffset);

            player = buffer.getShort();
            type = buffer.getShort();
            typeEnum = MovementTypes.getTypeById(type);
            movingAtStart = buffer.getByte();

            buffer.skipBytes(3);

            directionAtStart = buffer.getInt();

            if (typeEnum == MovementTypes.Extension) {
                buffer.skipBytes(14);
                //dataSize -= 14;
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