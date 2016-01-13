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
        public short value2;
        public short value3;

        @Override
        public void read(ByteReader buffer) {
            //buffer.skipBytes(4);
            value2 = buffer.getShort();
            value3 = buffer.getShort();
            value = buffer.getShort();
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
            return "Expressions." + getClass().getSimpleName() + "()";
        }
    }

    public static class ExtensionFunctionOneParam extends ExtensionCommon {
        @Override
        public java.lang.String toString() {
            return "Expressions." + getClass().getSimpleName() + "(" + value + ")";
        }
    }

    public static class ExtensionFunctionIgnoreData extends ExtensionCommon {
        @Override
        public java.lang.String toString() {
            return "Expressions." + getClass().getSimpleName() + "(" + value + ", ";
        }
    }

    public static class ExtensionFunction extends ExtensionCommon {
        @Override
        public java.lang.String toString() {
            return "Expressions." + getClass().getSimpleName() + "(" + value + ", ";
        }
    }

    public static class GlobalString extends GlobalCommon {
        @Override
        public java.lang.String toString() {
            return "Expressions.GlobalString(" + value + ")";
        }
    }

    public static class GlobalValue extends GlobalCommon {
        @Override
        public java.lang.String toString() {
            return "Expressions.GlobalValue(" + value + ")";
        }
    }

    // Runtime access
    public static class LoopIndex extends ExtensionFunction {
    }

    public static class Find extends ExpressionValue {
    }

    public static class GetY extends GlobalCommon {

        @Override
        public java.lang.String toString() {
            return "Expressions.GetY(" + value2 + ", " + value + ")";
        }
    }

    public static class XMouse extends ExpressionValue {
    }

    public static class YMouse extends ExpressionValue {
    }

    public static class FrameWidth extends ExpressionValue {
    }

    public static class FrameHeight extends ExpressionValue {
    }

    public static class UpperString extends ExtensionFunctionIgnoreData {
    }

    public static class LowerString extends ExtensionFunctionIgnoreData {
    }

    public static class StringLength extends ExtensionFunctionIgnoreData {
    }

    public static class ReplaceSubstring extends ExtensionFunctionIgnoreData {
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

    public static class CurrentText extends ExtensionFunctionOneParam {
    }

    public static class XLeftFrame extends ExpressionValue {
    }

    public static class XRightFrame extends ExpressionValue {
    }

    public static class YTopFrame extends ExpressionValue {
    }

    public static class YBottomFrame extends ExpressionValue {
    }

    public static class LeftString extends ExtensionFunction {
    }

    public static class RightString extends ExtensionFunction {
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


    public static class GetItemValue extends ExtensionFunction {
    }

    public static class GetItemString extends ExtensionFunction {
    }

    public static class GroupItemString extends ExtensionFunction {
    }

    public static class GetValue extends ExtensionFunction {
    }

    public static class GetString extends ExtensionFunction {
    }

    // List API
    public static class Element extends ExtensionFunction {
    }

    public static class Select extends ExtensionFunctionOneParam {
    }

    public static class SelectedLine extends ExtensionFunctionOneParam {
    }

    public static class ListLength extends ExtensionFunctionOneParam {
    }

    // Unknowns
    public static class UnknownX extends ExpressionValue {
    }

    public static class UnknownY extends ExpressionValue {
    }

    public static class GetObjectXLeft extends ExtensionFunctionOneParam {
    }


    public static class GetValueX extends ExpressionValue {
    }

    public static class GetValueY extends ExpressionValue {
    }

    public static class UnknownList extends ExtensionFunction {
    }

    // HTTP
    public static class HTTPContent extends ExtensionFunctionOneParam {
    }

    // Steam API
    public static class SteamAccountUserName extends ExpressionValue {
    }

    public static class SteamAccountUserId extends ExpressionValue {
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

    public static class ToNumber extends ExtensionFunction {
    }

    public static class ToInt extends ExtensionFunction {
    }

    public static class FloatToString extends ExtensionFunction {
    }

    public static class ToString extends ExtensionFunction {
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
