package net.jselby.escapists.data.events;

import kotlin.Pair;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.events.expression.ExpressionFunction;
import net.jselby.escapists.util.ByteReader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A expression is a operation between two or more values.
 */
public class Expression {
    public final short objectType;
    public final short num;

    public ExpressionValue value;

    private int objectInfo;
    private short objectInfoList;

    public Expression(ByteReader buffer) {
        int currentPosition = buffer.getPosition();
        objectType = buffer.getShort();
        num = buffer.getShort();
        if (objectType == 0 && num == 0) {
            return;
        }

        int size = buffer.getUnsignedShort();

        try {
            Pair<Method, Annotation> type = EscapistsRuntime.getRuntime()
                    .getRegister().getExpressionFunction(objectType, num);
            if (ExpressionNames.getByID(objectType, num) != null) {
                if (type != null) {
                    value = new ExpressionFunction(type);
                    value.read(buffer);
                } else {
                    value = ExpressionValue.getExpression(ExpressionNames.getByID(objectType, num), buffer);
                }
            } else if (objectType >= 2 || objectType == -7) {
                objectInfo = buffer.getUnsignedShort();
                objectInfoList = buffer.getShort();
                if (ExpressionNames.getByExtensionID(num) != null) {
                    value = ExpressionValue.getExpression(ExpressionNames.getByExtensionID(num), buffer);
                }// else {
                //System.out.println("Unknown value: " + (size - 8));
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buffer.setPosition(currentPosition + size);
    }

    @Override
    public String toString() {
        return "Expression!?";
        //return "HA" + (value == null ? "null1" : value.toString());
    }
}
