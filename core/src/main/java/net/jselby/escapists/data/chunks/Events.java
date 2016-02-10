package net.jselby.escapists.data.chunks;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.events.ParameterNames;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.data.events.interpreter.Interpreter;
import net.jselby.escapists.game.events.FunctionRegistration;
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

    public int maxObjects;
    public short maxObjectInfo;

    public short numberOfPlayers;
    public short[] numberOfConditions;

    public Qualifier[] qualifiers;
    public EventGroup[] groups;

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
                int endPosition = buffer.getPosition() + size;

                List<EventGroup> groups = new ArrayList<EventGroup>();

                while(buffer.getPosition() < endPosition) {
                    groups.add(new EventGroup(buffer));
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

    @Override
    public String toString() {
        return "Events={"  + Arrays.toString(groups) + "}";
    }

    /**
     * A Qualifier is a object qualifier.
     */
    public class Qualifier {
        private final int objectInfo;
        private final short type;
        private final int qualifier;

        public Qualifier(ByteReader buffer) {
            objectInfo = buffer.getUnsignedShort();
            type = buffer.getShort();
            qualifier = objectInfo & 0x7ff;
        }
    }

    public class EventGroup {
        private final int flags;

        private final int is_restricted;
        private final int restrictCpt;

        public final Condition[] conditions;
        public final Action[] actions;

        public EventGroup(ByteReader buffer) {
            int initialPosition = buffer.getPosition();
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

            buffer.setPosition(initialPosition + size);
        }

        @Override
        public String toString() {
            return "EventGroup={{" + Arrays.toString(conditions) + "},{" + Arrays.toString(actions) + "}}\n";
        }
    }

    /**
     * A condition is a check checked when evaluating events.
     */
    public class Condition {
        public final short objectType;
        public final short num;

        public final int objectInfo;
        private final short objectInfoList;

        private final short flags;
        private final short flags2;

        private final byte defType;
        public final short identifier;

        public final String name;
        public final Parameter[] items;
        public final FunctionRegistration method;

        public Condition(ByteReader buffer) {
            int currentPosition = buffer.getPosition();
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

            method = EscapistsRuntime.getRuntime().getRegister().getFunction(objectType, num);
            if (method != null) {
                name = method.getMethod().getName().trim();
            } else {
               /*System.out.println("No action declared for " + objectType + ":" + num
                        + (ActionNames.getByID(objectType, num) != null ?
                        (" (Possibly " + ActionNames.getByID(objectType, num) + ")") : ""));*/
                name = null;
            }

            items = new Parameter[paramCount];
            for (int i = 0; i < paramCount; i++) {
                items[i] = new Parameter(buffer);
            }

            assert (currentPosition + size) == buffer.getPosition();

            buffer.setPosition(currentPosition + size);
        }

        public boolean inverted() {
            return (flags2 & 1) != 0;
        }

        @Override
        public String toString() {
            // DefType = 0? always? Identifier = uniqueId for object
            return "Condition:" + identifier + ":" + (name == null ? (objectType + "." + num) : name)
                    + ":inverted=" + inverted() + ":c=" + Arrays.toString(items);
        }
    }

    /**
     * An Action is something executed if (x) conditions are evaluated as all true. This primarily interact
     * with the game rule.
     */
    public class Action {
        public final short objectType;
        public final short num;

        public final int objectInfo;
        private final short objectInfoList;

        private final short flags;
        private final short flags2;

        private final byte defType;

        public final String name;
        public FunctionRegistration method;
        public final Parameter[] items;

        public Action(ByteReader buffer) {
            int currentPosition = buffer.getPosition();
            int size = buffer.getUnsignedShort();

            objectType = buffer.getShort();
            num = buffer.getShort();

            objectInfo = buffer.getUnsignedShort();
            objectInfoList = buffer.getShort();

            flags = buffer.getUnsignedByte();
            flags2 = buffer.getUnsignedByte();

            int paramCount = buffer.getByte();
            defType = buffer.getByte();

            method = EscapistsRuntime.getRuntime().getRegister().getFunction(objectType, num);
            if (method != null) {
                name = method.getMethod().getName();
            } else {
                /*System.out.println("No action declared for " + objectType + ":" + num
                        + (ActionNames.getByID(objectType, num) != null ?
                        (" (Possibly " + ActionNames.getByID(objectType, num) + ")") : ""));*/
                name = null;
            }

            items = new Parameter[paramCount];
            for (int i = 0; i < paramCount; i++) {
                items[i] = new Parameter(buffer);
            }

            // Grab the appropriate method to invoke this Action
            /*if (name != null) {
                method = Actions.getMethodForAction(name);
            }*/

            buffer.setPosition(currentPosition + size);
        }

        @Override
        public String toString() {
            return "Action:" + objectInfo + ":" +
                    (name == null ? (objectType + "." + num) : name) + ":" + Arrays.toString(items);
        }
    }

    /**
     * A Parameter is a param to a action/condition.
     */
    public class Parameter {
        private final short code;
        private final ParameterLoader loader;

        public String name;
        public ParameterValue value;

        public Parameter(ByteReader buffer) {
            int currentPosition = buffer.getPosition();
            int size = buffer.getShort();
            code = buffer.getShort();
            loader = ParameterLoader.getParamByID(code);

            name = ParameterNames.getByID(code);
            if (loader != null) {
                value = ParameterValue.getParameter(loader.name(), buffer);
            }

            buffer.setPosition(currentPosition + size);

        }

        @Override
        public String toString() {
            return name + ":" + value;
        }

        /**
         * Adds this params elements to the list below.
         * @param list the list to append to.
         */
        public void add(Interpreter interpreter, ArrayList<Object> list) {
            value.add(interpreter, list);
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
