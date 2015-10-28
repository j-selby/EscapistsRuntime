package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Events are chunks used to invoke various things within the game world.
 */
public class Events extends Chunk {
    private static byte[] HEADER = "ER>>".getBytes();
    private static byte[] EVENT_COUNT = "ERes".getBytes();
    private static byte[] EVENTGROUP_DATA = "ERev".getBytes();
    private static byte[] END = "<<ER".getBytes();

    private int maxObjects;
    private short maxObjectInfo;

    private short numberOfPlayers;
    private short[] numberOfConditions;

    private Qualifier[] qualifiers;
    private EventGroup[] groups;

    @Override
    public void init(ByteReader buffer, int length) {
        while(true) {
            byte[] identifier = buffer.getBytes(4);
            if (Arrays.equals(identifier, HEADER)) {
                maxObjects = Math.max(300, buffer.getShort());
                maxObjectInfo = buffer.getShort();

                numberOfPlayers = buffer.getShort();

                numberOfConditions = new short[17];
                for (int i = 0; i < numberOfConditions.length; i++) {
                    numberOfConditions[i] = buffer.getShort();
                }

                qualifiers = new Qualifier[buffer.getShort()];
                for (int i = 0; i < qualifiers.length; i++) {
                    qualifiers[i] = new Qualifier(buffer);
                }
            } else if (Arrays.equals(identifier, EVENT_COUNT)) {
                buffer.getInt(); // size
            } else if (Arrays.equals(identifier, EVENTGROUP_DATA)) {
                int size = buffer.getInt();
                int endPosition = buffer.position() + size;

                List<EventGroup> groups = new ArrayList<EventGroup>();

                while(true) {
                    groups.add(new EventGroup(buffer));
                    if (buffer.position() >= endPosition) {
                        break;
                    }
                }

                this.groups = groups.toArray(new EventGroup[groups.size()]);

            } else if (Arrays.equals(identifier, END)) {
                break;
            } else {
                System.err.printf("Identifier %s not implemented.\n", new String(identifier));
                break;
            }
        }
    }

    /**
     * A Qualifier is a object qualifier.
     */
    private class Qualifier {
        private final int objectInfo;
        private final short type;
        private final int qualifier;

        public Qualifier(ByteReader buffer) {
            objectInfo = buffer.getUnsignedShort();
            type = buffer.getShort();
            qualifier = objectInfo; // TODO: Binary & 0x11111111111
        }
    }

    private class EventGroup {
        private final int flags;

        private final int is_restricted;
        private final int restrictCpt;

        private final Condition[] conditions;
        private final Action[] actions;

        public EventGroup(ByteReader buffer) {
            int initialPosition = buffer.position();
            int size = buffer.getShort() * -1;

            int numberOfConditions = buffer.getUnsignedByte();
            int numberOfActions = buffer.getUnsignedByte();
            flags = buffer.getUnsignedShort();

            buffer.skipBytes(2);

            is_restricted = buffer.getInt();
            restrictCpt = buffer.getInt();

            conditions = new Condition[numberOfConditions];
            for (int i = 0; i < numberOfConditions; i++) {
                conditions[i] = new Condition(buffer);
            }

            actions = new Action[numberOfActions];
            for (int i = 0; i < numberOfActions; i++) {
                actions[i] = new Action(buffer);
            }

            buffer.position(initialPosition + size);
        }

    }

    /**
     * A condition is a check checked when evaluating events.
     */
    private class Condition {
        private final short objectType;
        private final short num;

        private final int objectInfo;
        private final short objectInfoList;

        private final short flags;
        private final short flags2;

        private final byte defType;
        private final short identifier;

        private final Parameter[] items;

        public Condition(ByteReader buffer) {
            int currentPosition = buffer.position();
            int size = buffer.getUnsignedShort();

            objectType = buffer.getShort();
            num = buffer.getShort();
            objectInfo = buffer.getUnsignedShort();
            objectInfoList = buffer.getShort();

            flags = buffer.getUnsignedByte();
            flags2 = buffer.getUnsignedByte();

            int paramCount = buffer.getByte();
            defType = buffer.getByte();
            identifier = buffer.getShort(); // Event identifier

            items = new Parameter[paramCount];
            for (int i = 0; i < paramCount; i++) {
                items[i] = new Parameter(buffer);
            }

            buffer.position(currentPosition + size);
        }
    }

    /**
     * An Action is something executed if (x) conditions are evaluated as all true. This primarily interact
     * with the game rule.
     */
    private class Action {
        private final short objectType;
        private final short num;

        private final int objectInfo;
        private final short objectInfoList;

        private final short flags;
        private final short flags2;

        private final byte defType;

        private final Parameter[] items;

        public Action(ByteReader buffer) {
            int currentPosition = buffer.position();
            int size = buffer.getUnsignedShort();

            objectType = buffer.getShort();
            num = buffer.getShort();

            objectInfo = buffer.getUnsignedShort();
            objectInfoList = buffer.getShort();

            flags = buffer.getUnsignedByte();
            flags2 = buffer.getUnsignedByte();

            int paramCount = buffer.getByte();
            defType = buffer.getByte();

            items = new Parameter[paramCount];
            for (int i = 0; i < paramCount; i++) {
                items[i] = new Parameter(buffer);
            }

            buffer.position(currentPosition + size);
        }
    }

    /**
     * A Parameter is a param to a action/condition.
     */
    private class Parameter {
        private final short code;
        private final ParameterLoader loader;

        public Parameter(ByteReader buffer) {
            int currentPosition = buffer.position();
            int size = buffer.getShort();
            code = buffer.getShort();
            loader = ParameterLoader.getParamByID(code);
            buffer.position(currentPosition + size);
        }
    }

    /**
     * A ParameterLoader is a loader argument within a Parameter.
     */
    public enum ParameterLoader {
        None(0, 8, 20, 30),
        Object(1),
        Time(2),
        Short(3, 4, 10, 11, 12, 17, 26, 31, 43, 49, 50, 57, 58, 60, 61),
        Int(5, 25, 29, 34, 48, 56),
        Sample(6, 7, 35, 36),
        Create(9, 21),
        Every(13),
        KeyParameter(14, 44),
        ExpressionParameter(15, 22, 23, 27, 28, 45, 46, 52, 53, 54, 59, 62),
        Position(16),
        Shoot(18),
        Zone(19),
        Colour(24),
        Click(32),
        Program(33),
        Remark(37),
        Group(38),
        GroupPointer(39),
        Filename(40, 63),
        String(41, 64),
        CompareTime(42),
        TwoShorts(47, 51),
        Extension(55),
        CharacterEncoding(65, 66)
        ;

        private int[] ids;

        ParameterLoader(int... ids) {
            this.ids = ids;
        }

        public static ParameterLoader getParamByID(int id) {
            for (ParameterLoader param : values()) {
                for (int testId : param.ids) {
                    if (id == testId) {
                        return param;
                    }
                }
            }
            return null;
        }
    }
}
