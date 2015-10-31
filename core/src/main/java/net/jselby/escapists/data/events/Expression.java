package net.jselby.escapists.data.events;

import net.jselby.escapists.util.ByteReader;

/**
 * A expression is a operation between two or more values.
 */
public class Expression {
    public final short objectType;
    public final short num;

    public ParameterValue value;

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

        /*if (ExpressionNames.getByID(objectType, num) != null) {
            value = ParameterValue.getParameter(ExpressionNames.getByID(objectType, num), buffer);
            value.read(buffer);
        } else if (objectType >= 2 || objectType == -7) {
            objectInfo = buffer.getUnsignedShort();
            objectInfoList = buffer.getShort();

            // TODO: Extension expression
        }*/

        buffer.position(currentPosition + size);
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
