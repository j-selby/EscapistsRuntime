package net.jselby.escapists.data.events;

import net.jselby.escapists.util.ByteReader;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Values of expressions.
 * Only $5.99 for the first DLC pack!
 *
 * @author j_selby
 */
public abstract class ExpressionValue {
    @Override
    public java.lang.String toString() {
        return "Expressions." + getClass().getSimpleName() + "()";
    }

    /**
     * Reads a parameter using the specified buffer.
     * @param buffer The buffer to read from.
     */
    public void read(ByteReader buffer) {}

    public static class String extends ExpressionValue {
        public java.lang.String value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getString();
        }

        @Override
        public java.lang.String toString() {
            return "\"" + StringEscapeUtils.escapeEcmaScript(value) + "\"";
        }
    }

    public static class Long extends ExpressionValue {
        public int value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getInt();
        }

        @Override
        public java.lang.String toString() {
            return "" + value;
        }
    }

    public static class Double extends ExpressionValue {
        public double value;
        private float floatValue;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getDouble();
            floatValue = buffer.getFloat();
        }

        @Override
        public java.lang.String toString() {
            // TODO: Investigate floatValue
            return "" + value;
        }
    }

    // Operators
    public static class Virgule extends ExpressionValue {
        @Override
        public java.lang.String toString() {
            return ", ";
        }
    }

    public static class Parenthesis extends ExpressionValue {
        @Override
        public java.lang.String toString() {
            return "(";
        }
    }

    public static class EndParenthesis extends ExpressionValue {
        @Override
        public java.lang.String toString() {
            return ")";
        }
    }

    // Converters
    public static abstract class Operation extends ExpressionValue {
    }

    public static class Plus extends Operation {
        @Override
        public java.lang.String toString() {
            return "+";
        }
    }

    public static class Minus extends Operation {
        @Override
        public java.lang.String toString() {
            return "-";
        }
    }

    public static class Multiply extends Operation {
        @Override
        public java.lang.String toString() {
            return "*";
        }
    }

    public static class Divide extends Operation {
        @Override
        public java.lang.String toString() {
            return "/";
        }
    }

    public static class Modulus extends Operation {
    }

    public static class Power extends Operation {
    }

    public static class AND extends Operation {
    }

    public static class OR extends Operation {
    }

    public static class XOR extends Operation {
    }

    public static ExpressionValue getExpression(java.lang.String name, ByteReader buffer) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ExpressionValue> parameterClass
                    = (Class<? extends ExpressionValue>) Class.forName(ExpressionValue.class.getName() + "$" + name);
            ExpressionValue result = parameterClass.newInstance();
            result.read(buffer);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
