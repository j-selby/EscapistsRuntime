package net.jselby.escapists.data.chunks;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.events.ActionNames;
import net.jselby.escapists.data.events.ConditionNames;
import net.jselby.escapists.data.events.ParameterNames;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.util.ByteReader;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
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
    private static String[] EXPRESSION_INSTANCE_REQ = {"Once"};

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
     * Converts this event set to a Javascript script, ready to be executed.
     * @return A String
     */
    public String toJS() {
        // Iterate over the events
        StringBuilder builder = new StringBuilder();
        int indent = 0;

        for (EventGroup group : groups) {
            // Check for groups
            if (group.conditions.length != 0 && group.conditions[0].name != null && group.conditions[0].name.equalsIgnoreCase("NewGroup")) {
                builder.append(StringUtils.repeat(' ', indent)).append("if (env.GroupActivated(")
                        .append(((ParameterValue.Group) group.conditions[0].items[0].value).id)
                        .append(")) { // Flags: ")
                        .append(Integer.toBinaryString(((ParameterValue.Group) group.conditions[0].items[0].value).flags))
                        .append(", name: ")
                        .append(((ParameterValue.Group) group.conditions[0].items[0].value).name)
                        .append("\n");
                indent += 4;
                continue;
            } else if (group.conditions.length != 0 && group.conditions[0].name != null && group.conditions[0].name.equalsIgnoreCase("GroupEnd")) {
                indent -= 4;
                builder.append(StringUtils.repeat(' ', indent)).append("}\n");
                continue;
            }

            // Check for ORs within the group
            boolean hasOR = false;
            if (group.conditions.length > 3) {
                for (Condition condition : group.conditions) {
                    if (condition.name != null && condition.name.trim().equalsIgnoreCase("OrFiltered")) {
                        hasOR = true;
                        break;
                    }
                }
            }

            String conditions = "";
            int count = 0;

            if (hasOR) {
                conditions += "(";
            }

            // Build conditions for group
            boolean lastWasOr = false;
            for (Condition condition : group.conditions) {

                ObjectDefinition object = null;
                if (EscapistsRuntime.getRuntime().getApplication().objectDefs.length > condition.objectInfo) {
                    object = EscapistsRuntime.getRuntime().getApplication().objectDefs[condition.objectInfo];
                }

                int id = (object == null ? -1 : object.handle);
                String objName = (id + " /*" + (object == null ? "null" + condition.objectInfo : object.name)
                        + "*/").trim();
                String objDeclaration = "env." + (id == 0 ? "" : ("withObjects(" + objName + ")."));
                String objMethod = (condition.name == null ? (condition.objectType + ":" + condition.num) : condition.name).trim();

                boolean requiresContext = false;
                for (String type : EXPRESSION_INSTANCE_REQ) {
                    if (type.equalsIgnoreCase(objMethod)) {
                        requiresContext = true;
                        break;
                    }
                }

                if (objMethod.equalsIgnoreCase("OrFiltered")) {
                    conditions += (hasOR ? ")" : "") + " || " + (hasOR ? "(" : "");
                    lastWasOr = true;
                    continue;
                } else if (!lastWasOr && count != 0) {
                    conditions += " && ";
                } else {
                    lastWasOr = false;
                }

                if (condition.inverted()) {
                    conditions += "!";
                }

                conditions += objDeclaration + objMethod + "(";

                int paramCount = 0;
                if (requiresContext) {
                    conditions += condition.identifier;
                    paramCount++;
                }
                for (Parameter param : condition.items) {
                    conditions += (paramCount != 0 ? ", " : "") + param.value;// + ":" + param.code;//param.loader.name() + " " + param.name.toLowerCase();
                    paramCount++;
                }
                conditions += ")";
                count++;
            }

            if (hasOR) {
                conditions += ")";
            }

            // Build actions for group
            String actions = "";
            indent += 4;
            for (Action action : group.actions) {
                if (action.name != null && action.name.equalsIgnoreCase("Skip")) {
                    continue;
                }

                actions += StringUtils.repeat(' ', indent);

                if (action.name == null) {
                    actions += "/* Unknown: ";
                }

                ObjectDefinition object = null;
                if (EscapistsRuntime.getRuntime().getApplication().objectDefs.length > action.objectInfo) {
                    object = EscapistsRuntime.getRuntime().getApplication().objectDefs[action.objectInfo];
                }


                int id = (object == null ? -1 : object.handle);
                String objName = (id + " /*" + (object == null ? "null" + action.objectInfo : object.name)
                        + (action.name == null ? "* /" : "*/")).trim();
                String objDeclaration = "env." + (id == 0 ? "" : ("withObjects(" + objName + ")."));
                String objMethod = (action.name == null ? (action.objectType + ":" + action.num) : action.name).trim();

                actions += objDeclaration + objMethod + "(";

                int paramCount = 0;
                for (Parameter param : action.items) {
                    actions += (paramCount != 0 ? ", " : "") + param.value;// + ":" + param.code;//param.loader.name() + " " + param.name.toLowerCase();
                    paramCount++;
                }

                actions += ");";

                if (action.name == null) {
                    actions += " */";
                }

                actions += "\n";
            }

            indent -= 4;

            if (actions.length() == 0) {
                continue;
            }

            builder.append(StringUtils.repeat(' ', indent));
            if (conditions.length() != 0) {
                builder.append("if (").append(conditions).append(") ");
            }

            builder.append("{\n");
            builder.append(actions);
            builder.append(StringUtils.repeat(' ', indent)).append("}\n");
        }

        return builder.toString();
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
        private final short objectType;
        private final short num;

        private final int objectInfo;
        private final short objectInfoList;

        private final short flags;
        private final short flags2;

        private final byte defType;
        public final short identifier;

        public final String name;
        public Method method;
        public final Parameter[] items;

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

            name = ConditionNames.getByID(objectType, num);

            items = new Parameter[paramCount];
            for (int i = 0; i < paramCount; i++) {
                items[i] = new Parameter(buffer);
            }

            // Grab the appropriate method to invoke this Condition
            /*if (name != null) {
                method = Conditions.getMethodForCondition(name);
            }*/

            buffer.setPosition(currentPosition + size);
        }

        public boolean inverted() {
            return (flags2 & 1) != 0;
        }

        @Override
        public String toString() {
            // DefType = 0? always? Identifier = uniqueId for object
            return "Condition:" + identifier + ":" + (name == null ? (num + ":" + objectType) : name) + ":c=" + Arrays.toString(items);
        }
    }

    /**
     * An Action is something executed if (x) conditions are evaluated as all true. This primarily interact
     * with the game rule.
     */
    public class Action {
        private final short objectType;
        private final short num;

        public final int objectInfo;
        private final short objectInfoList;

        private final short flags;
        private final short flags2;

        private final byte defType;

        public final String name;
        public Method method;
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

            name = ActionNames.getByID(objectType, num);

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
            return "Action:" + name + ":" + Arrays.toString(items);
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
