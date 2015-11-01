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
        int currentPosition = buffer.position();
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

        buffer.position(currentPosition + size);
    }

    @Override
    public String toString() {
        return value == null ? "null1" : value.toString();
    }

    /*
    cdef int currentPosition = reader.tell()
        self.objectType = reader.readShort()
        self.num = reader.readShort()
        if self.objectType == 0 and self.num == 0:
            return
        cdef int size = reader.readShort(True)
        if self.objectType in systemLoaders and self.num in systemLoaders[self.objectType]:
            self.loader = self.new(
                systemLoaders[self.objectType][self.num], reader)
        elif self.objectType >= 2 or self.objectType == -7:
            self.objectInfo = reader.readShort(True)
            self.objectInfoList = reader.readShort()
            if self.num in extensionLoaders:
                self.loader = self.new(extensionLoaders[self.num], reader)
        reader.seek(currentPosition + size)
     */
}
