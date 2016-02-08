package net.jselby.escapists.data.events.expression;

import kotlin.Pair;
import net.jselby.escapists.data.events.ExpressionValue;
import net.jselby.escapists.game.events.Expression;
import net.jselby.escapists.util.ByteReader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * A ExpressionFunction is a function call inside a expression.
 */
public class ExpressionFunction extends ExpressionValue {
    public final Expression annotation;
    public final Method method;

    private int value1;
    private int value2;
    private int value3;

    public List<Object> openParams;

    public ExpressionFunction(Pair<Method, Annotation> type) {
        method = type.getFirst();
        annotation = (Expression) type.getSecond();
    }

    @Override
    public void read(ByteReader buffer) {
        value1 = buffer.getShort(); // TODO: This used to be first with GlobalCommon, fix implementing functions
        value2 = buffer.getShort();
        value3 = buffer.getShort();
    }

    public Object[] getParameters() {
        int index = 0;
        Object[] array = new Object[
                (annotation.requiresArg1() ? 1 : 0) +
                (annotation.requiresArg2() ? 1 : 0) +
                (annotation.requiresArg3() ? 1 : 0) +
                        (openParams != null ? openParams.size() : 0)];
        if (annotation.requiresArg1()) {
            array[index++] = value1;
        }
        if (annotation.requiresArg2()) {
            array[index++] = value2;
        }
        if (annotation.requiresArg3()) {
            array[index++] = value3;
        }
        if (openParams != null) {
            for (Object openParam : openParams) {
                array[index++] = openParam;
            }
        }

        return array;
    }

    @Override
    public java.lang.String toString() {
        java.lang.String testArgs = Arrays.toString(getParameters());
        testArgs = testArgs.substring(1, testArgs.length() - 1);

        return method.getDeclaringClass().getSimpleName() + "." +
                method.getName() + "("
                + testArgs
                + (annotation.openEnded() ? ", " : ")");
    }
}
