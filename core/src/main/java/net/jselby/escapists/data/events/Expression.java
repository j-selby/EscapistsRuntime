package net.jselby.escapists.data.events;

import net.jselby.escapists.util.ByteReader;

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
            if (ExpressionNames.getByID(objectType, num) != null) {
                value = ExpressionValue.getExpression(ExpressionNames.getByID(objectType, num), buffer);
            } else if (objectType >= 2 || objectType == -7) {
                objectInfo = buffer.getUnsignedShort();
                objectInfoList = buffer.getShort();
                if (ExpressionNames.getByExtensionID(num) != null) {
                    value = ExpressionValue.getExpression(ExpressionNames.getByExtensionID(num), buffer);
                } else {
                    //System.out.println("Unknown value: " + (size - 8));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buffer.setPosition(currentPosition + size);
    }

    @Override
    public String toString() {
        return "HA" + (value == null ? "null1" : value.toString());
    }
}
