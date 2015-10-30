package net.jselby.escapists.data.events;

import net.jselby.escapists.util.ByteReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Parameter loaders + holders.
 */
public abstract class ParameterValue {
    /**
     * Reads a parameter using the specified buffer.
     * @param buffer The buffer to read from.
     */
    public abstract void read(ByteReader buffer);

    public static class Object extends ParameterValue {
        private short objectInfoList;
        private int objectInfo;
        private short objectType;

        @Override
        public void read(ByteReader buffer) {
            objectInfoList = buffer.getShort();
            objectInfo = buffer.getUnsignedShort();
            objectType = buffer.getShort();
        }
    }

    public static class Time extends ParameterValue {
        private int timer;
        private int loops;

        @Override
        public void read(ByteReader buffer) {
            timer = buffer.getInt();
            loops = buffer.getInt();
        }
    }

    public static class Short extends ParameterValue {
        private short value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getShort();
        }
    }

    // TODO: Remark

    public static class Int extends ParameterValue {
        private int value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getInt();
        }
    }

    public static class Sample extends ParameterValue {
        private int handle;
        private int flags;
        private java.lang.String name;

        @Override
        public void read(ByteReader buffer) {
            handle = buffer.getShort();
            flags = buffer.getUnsignedShort();
            name = buffer.getString();
        }
    }

    public static class Create extends ParameterValue {
        private Position position;

        private int objectInstance;
        private int objectInfo;

        @Override
        public void read(ByteReader buffer) {
            position = new Position();
            position.read(buffer);
            objectInstance = buffer.getUnsignedShort();
            objectInfo = buffer.getUnsignedShort();
            buffer.skipBytes(4);
        }
    }

    public static class Every extends ParameterValue {
        private int delay;
        private int compteur;

        @Override
        public void read(ByteReader buffer) {
            delay = buffer.getInt();
            compteur = buffer.getInt();
        }
    }

    public static class KeyParameter extends ParameterValue {
        private short key;

        @Override
        public void read(ByteReader buffer) {
            key = buffer.getShort();
        }
    }

    public static class ExpressionParameter extends ParameterValue {
        private short comparison;
        private Expression[] expressions;

        @Override
        public void read(ByteReader buffer) {
            comparison = buffer.getShort();

            List<Expression> expressions = new ArrayList<Expression>();
            while(true) {
                Expression expression = new Expression(buffer);
                if (expression.objectType == 0 && expression.num == 0) {
                    break;
                }
                expressions.add(expression);
            }

            this.expressions = expressions.toArray(new Expression[expressions.size()]);
        }
    }

    public static class Position extends ParameterValue {
        private int objectInfoParent;
        private int flags;

        private short x;
        private short y;

        private short slope;
        private short angle;

        private int direction;
        private short typeParent;

        private short objectInfoList;
        private short layer;

        @Override
        public void read(ByteReader buffer) {
            objectInfoParent = buffer.getUnsignedShort();
            flags = buffer.getUnsignedShort();

            x = buffer.getShort();
            y = buffer.getShort();

            slope = buffer.getShort();
            angle = buffer.getShort();

            direction = buffer.getInt();
            typeParent = buffer.getShort();

            objectInfoList = buffer.getShort();
            layer = buffer.getShort();
        }
    }

    public static class Shoot extends ParameterValue {
        private Position position;

        private int objectInstance;
        private int objectInfo;

        private short shootSpeed;

        @Override
        public void read(ByteReader buffer) {
            position = new Position();
            position.read(buffer);

            objectInstance = buffer.getUnsignedShort();
            objectInfo = buffer.getUnsignedShort();

            buffer.skipBytes(4);

            shootSpeed = buffer.getShort();
        }
    }

    public static class Zone extends ParameterValue {
        private short x1;
        private short y1;
        private short x2;
        private short y2;

        @Override
        public void read(ByteReader buffer) {
            x1 = buffer.getShort();
            y1 = buffer.getShort();
            x2 = buffer.getShort();
            y2 = buffer.getShort();
        }
    }

    public static class Colour extends ParameterValue {
        private com.badlogic.gdx.graphics.Color value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getColor();
        }
    }

    public static class Program extends ParameterValue {
        private int flags;

        private java.lang.String fileName;
        private java.lang.String command;

        @Override
        public void read(ByteReader buffer) {
            flags = buffer.getUnsignedShort();

            int currentPosition = buffer.position();
            fileName = buffer.getString();
            buffer.position(currentPosition + 260);

            command = buffer.getString();
        }
    }

    public static class Group extends ParameterValue {
        private int offset;

        private int flags;
        private int id;

        private java.lang.String name;
        private int password;

        @Override
        public void read(ByteReader buffer) {
            offset = buffer.position() - 24;
            flags = buffer.getUnsignedShort();
            id = buffer.getUnsignedShort();
            name = buffer.getString(96);
            password = buffer.getInt();
        }
    }

    public static class GroupPointer extends ParameterValue {
        private int savedPointer;
        private int pointer;
        private short id;

        @Override
        public void read(ByteReader buffer) {
            pointer = savedPointer = buffer.getInt();
            id = buffer.getShort();

            if (pointer != 0) {
                pointer += buffer.position();
            }
        }
    }

    public static class String extends ParameterValue {
        private java.lang.String value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getString();
        }
    }

    public static class Filename extends ParameterValue {
        private java.lang.String value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getString();
        }
    }

    public static class CompareTime extends ParameterValue {
        private int timer;
        private int loops;
        private short comparison;

        @Override
        public void read(ByteReader buffer) {
            timer = buffer.getInt();
            loops = buffer.getInt();
            comparison = buffer.getShort();
        }
    }

    public static class TwoShorts extends ParameterValue {
        private short value1;
        private short value2;

        @Override
        public void read(ByteReader buffer) {
            value1 = buffer.getShort();
            value2 = buffer.getShort();
        }
    }

    public static class Extension extends ParameterValue {
        private short type;
        private short code;
        private byte[] data;

        @Override
        public void read(ByteReader buffer) {
            int size = buffer.getShort();
            type = buffer.getShort();
            code = buffer.getShort();

            data = buffer.getBytes(size - 6);
        }
    }

    public static class Click extends ParameterValue {
        private byte click;
        private boolean doubleVal;

        @Override
        public void read(ByteReader buffer) {
            click = buffer.getByte();
            doubleVal = buffer.getByte() > 0;
        }
    }

    public static class CharacterEncoding extends ParameterValue {
        @Override
        public void read(ByteReader buffer) {
        }
    }

    public static class Bug extends ParameterValue {
        @Override
        public void read(ByteReader buffer) {
        }
    }

    public static ParameterValue getParameter(java.lang.String name, ByteReader buffer) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ParameterValue> parameterClass
                    = (Class<? extends ParameterValue>) Class.forName(ParameterValue.class.getName() + "$" + name);
            ParameterValue result = parameterClass.newInstance();
            result.read(buffer);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
