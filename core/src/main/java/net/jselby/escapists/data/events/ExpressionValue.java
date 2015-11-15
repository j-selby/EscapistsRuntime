package net.jselby.escapists.data.events;

import net.jselby.escapists.util.ByteReader;

/**
 * Values of expressions.
 * Only $5.99 for the first DLC pack!
 *
 * @author j_selby
 */
public abstract class ExpressionValue {
    /**
     * Reads a parameter using the specified buffer.
     * @param buffer The buffer to read from.
     */
    public void read(ByteReader buffer) {}

    public static class String extends ExpressionValue {
        private java.lang.String value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getString();
        }

        public java.lang.String getValue() {
            return value;
        }

        @Override
        public java.lang.String toString() {
            return "\"" + value + "\"";
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
        private double value;
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

    // External values
    public abstract static class GlobalCommon extends ExpressionValue {
        public short value;

        @Override
        public void read(ByteReader buffer) {
            buffer.skipBytes(4);
            value = buffer.getShort();
        }
    }

    public static class GlobalString extends GlobalCommon {
        @Override
        public java.lang.String toString() {
            return "GlobalString(" + value + ")";
        }
    }

    public static class GlobalValue extends GlobalCommon {
        @Override
        public java.lang.String toString() {
            return "GlobalValue(" + value + ")";
        }
    }

    public abstract static class ExtensionCommon extends ExpressionValue {
        public short value;

        @Override
        public void read(ByteReader buffer) {
            value = buffer.getShort();
        }
    }

    public static class ExtensionString extends ExtensionCommon {
        @Override
        public java.lang.String toString() {
            return "ExtensionString(" + value + ")";
        }
    }

    public static class ExtensionValue extends ExtensionCommon {
        @Override
        public java.lang.String toString() {
            return "ExtensionValue(" + value + ")";
        }
    }

    // Runtime access
    public static class LoopIndex extends ExpressionValue {
    }

    public static class Find extends ExpressionValue {
    }

    public static class XMouse extends ExpressionValue {
    }

    public static class YMouse extends ExpressionValue {
    }

    public static class FrameWidth extends ExpressionValue {
    }

    public static class FrameHeight extends ExpressionValue {
    }

    public static class UpperString extends ExpressionValue {
    }

    public static class LowerString extends ExpressionValue {
    }

    public static class StringLength extends ExpressionValue {
    }

    public static class Random extends ExpressionValue {
    }

    public static class Min extends ExpressionValue {
    }

    public static class GetRGB extends ExpressionValue {
    }

    public static class Max extends ExpressionValue {
    }

    public static class ApplicationDrive extends ExpressionValue {
    }

    public static class ApplicationDirectory extends ExpressionValue {
    }

    public static class CurrentText extends ExpressionValue {
    }

    public static class XLeftFrame extends ExpressionValue {
    }

    public static class XRightFrame extends ExpressionValue {
    }

    public static class YTopFrame extends ExpressionValue {
    }

    public static class YBottomFrame extends ExpressionValue {
    }

    public static class LeftString extends ExpressionValue {
    }

    public static class RightString extends ExpressionValue {
    }

    public static class CounterValue extends ExpressionValue {
    }

    public static class GetXScale extends ExpressionValue {
    }

    public static class Floor extends ExpressionValue {
    }

    public static class Ceil extends ExpressionValue {
    }

    public static class Sin extends ExpressionValue {
    }

    public static class Cos extends ExpressionValue {
    }

    public static class Tan extends ExpressionValue {
    }

    public static class Abs extends ExpressionValue {
    }

    public static class GetChannelVolume extends ExpressionValue {
    }

    public static class DistanceBetween extends ExpressionValue {
    }

    public static class GetDataDirectory extends ExpressionValue {
    }

    // Operators
    public static class Virgule extends ExpressionValue {
        @Override
        public java.lang.String toString() {
            return ",";
        }
    }

    public static class Parenthesis extends ExpressionValue {
    }

    public static class EndParenthesis extends ExpressionValue {
    }

    // Converters
    public static abstract class Operation extends ExpressionValue {
    }

    public static class ToNumber extends Operation {
    }

    public static class ToInt extends Operation {
    }

    public static class FloatToString extends Operation {
    }

    public static class ToString extends Operation {
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
    }

    public static class Divide extends Operation {
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
