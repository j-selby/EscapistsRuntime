package net.jselby.escapists.data.events;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.util.ByteReader;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;

import java.util.ArrayList;
import java.util.Arrays;
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
        public short objectInfoList;
        public int objectInfo;
        public short objectType;

        @Override
        public void read(ByteReader buffer) {
            objectInfoList = buffer.getShort();
            objectInfo = buffer.getUnsignedShort();
            objectType = buffer.getShort();
        }
    }

    public static class Time extends ParameterValue {
        public int timer;
        public int loops;

        @Override
        public void read(ByteReader buffer) {
            timer = buffer.getInt();
            loops = buffer.getInt();
        }
    }

    public static class Short extends ParameterValue {
        public short value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getShort();
        }

        @Override
        public java.lang.String toString() {
            return "Short=" + value;
        }
    }

    // TODO: Remark

    public static class Int extends ParameterValue {
        public int value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getInt();
        }

        @Override
        public java.lang.String toString() {
            return "Int=" + value;
        }
    }

    public static class Sample extends ParameterValue {
        public int handle;
        public int flags;
        public java.lang.String name;

        @Override
        public void read(ByteReader buffer) {
            handle = buffer.getShort();
            flags = buffer.getUnsignedShort();
            name = buffer.getString();
        }
    }

    public static class Create extends ParameterValue {
        public Position position;

        public int objectInstance;
        public int objectInfo;

        @Override
        public void read(ByteReader buffer) {
            position = new Position();
            position.read(buffer);
            objectInstance = buffer.getUnsignedShort();
            objectInfo = buffer.getUnsignedShort();
            buffer.skipBytes(4);
        }

        @Override
        public java.lang.String toString() {
            return "Create=" + objectInfo;
        }
    }

    public static class Every extends ParameterValue {
        public int delay;
        public int compteur;

        @Override
        public void read(ByteReader buffer) {
            delay = buffer.getInt();
            compteur = buffer.getInt();
        }

        @Override
        public java.lang.String toString() {
            return "Every=" + delay;
        }
    }

    public static class KeyParameter extends ParameterValue {
        public short key;

        @Override
        public void read(ByteReader buffer) {
            key = buffer.getShort();
        }
    }

    public static class ExpressionParameter extends ParameterValue {
        public short comparison;
        public Expression[] expressions;
        private Script value;

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

        @Override
        public java.lang.String toString() {
            // Build string
            java.lang.String str = "";
            for (Expression expression : expressions) {
                if (expression.value != null) {
                    str += expression.value.toString();
                } else {
                    str += "/*$null(" + expression.objectType + ":" + expression.num + ")$*/";
                }
            }

            return str;
        }

        public void compile(Context context) {
            //System.out.println("Compiling expression: " + toString());
            //System.out.println("Compiling expression: " + toString());
            try {
                //value = context.compileString(toString(), "expression@" + hashCode(), 1, null);
            } catch (Exception ignored) {
            }
        }
    }

    public static class Position extends ParameterValue {
        public int objectInfoParent;
        public int flags;

        public short x;
        public short y;

        public short slope;
        public short angle;

        public int direction;
        public short typeParent;

        public short objectInfoList;
        public short layer;

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
        public Position position;

        public int objectInstance;
        public int objectInfo;

        public short shootSpeed;

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
        public short x1;
        public short y1;
        public short x2;
        public short y2;

        @Override
        public void read(ByteReader buffer) {
            x1 = buffer.getShort();
            y1 = buffer.getShort();
            x2 = buffer.getShort();
            y2 = buffer.getShort();
        }
    }

    public static class Colour extends ParameterValue {
        public com.badlogic.gdx.graphics.Color value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getColor();
        }
    }

    public static class Program extends ParameterValue {
        public int flags;

        public java.lang.String fileName;
        public java.lang.String command;

        @Override
        public void read(ByteReader buffer) {
            flags = buffer.getUnsignedShort();

            int currentPosition = buffer.getPosition();
            fileName = buffer.getString();
            buffer.setPosition(currentPosition + 260);

            command = buffer.getString();
        }
    }

    public static class Group extends ParameterValue {
        public int offset;

        public int flags;
        public int id;

        public java.lang.String name;
        public int password;

        @Override
        public void read(ByteReader buffer) {
            offset = buffer.getPosition() - 24;
            flags = buffer.getUnsignedShort();
            id = buffer.getUnsignedShort();
            name = buffer.getString(96);
            password = buffer.getInt();
        }
    }

    public static class GroupPointer extends ParameterValue {
        public int savedPointer;
        public int pointer;
        public short id;

        @Override
        public void read(ByteReader buffer) {
            pointer = savedPointer = buffer.getInt();
            id = buffer.getShort();

            if (pointer != 0) {
                pointer += buffer.getPosition();
            }
        }
    }

    public static class String extends ParameterValue {
        public java.lang.String value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getString();
        }
    }

    public static class Filename extends ParameterValue {
        public java.lang.String value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getString();
        }
    }

    public static class CompareTime extends ParameterValue {
        public int timer;
        public int loops;
        public short comparison;

        @Override
        public void read(ByteReader buffer) {
            timer = buffer.getInt();
            loops = buffer.getInt();
            comparison = buffer.getShort();
        }
    }

    public static class TwoShorts extends ParameterValue {
        public short value1;
        public short value2;

        @Override
        public void read(ByteReader buffer) {
            value1 = buffer.getShort();
            value2 = buffer.getShort();
        }
    }

    public static class Extension extends ParameterValue {
        public short type;
        public short code;
        public byte[] data;

        @Override
        public void read(ByteReader buffer) {
            int size = buffer.getShort();
            type = buffer.getShort();
            code = buffer.getShort();

            data = buffer.getBytes(size - 6);
        }
    }

    public static class Click extends ParameterValue {
        public byte click;
        public boolean doubleVal;

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
            throw new IllegalStateException(e);
        }
    }
}
