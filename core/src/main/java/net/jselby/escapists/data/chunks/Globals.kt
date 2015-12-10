package net.jselby.escapists.data.chunks

import net.jselby.escapists.data.Chunk
import net.jselby.escapists.util.ByteReader

class GlobalStrings : Chunk() {
    lateinit var data: Array<String?>

    override fun init(buffer: ByteReader, length: Int) {
        val l = buffer.unsignedInt.toInt()
        data = arrayOfNulls<String>(l)

        for (i in 0..l - 1) {
            data[i] = buffer.string
            System.out.println(data[i])
        }
    }
}

class GlobalValues : Chunk() {
    lateinit var values: Array<Number?>

    override fun init(buffer: ByteReader, length: Int) {
        val numberOfItems = buffer.unsignedShort

        val tempList = arrayOfNulls<ByteReader?>(numberOfItems);
        for (i in 0..numberOfItems - 1) {
            tempList[i] = ByteReader(buffer.getBytes(4));
        }

        values = arrayOfNulls<Number?>(numberOfItems)
        for (i in 0..numberOfItems - 1) {
            val reader = tempList[i]!!;
            val value : Number;
            val type = buffer.unsignedByte.toInt()
            if (type == 2) { // Float/Double
                value = reader.float
            } else if (type == 0) { // Int
                value = reader.int
            } else {
                throw IllegalArgumentException("Invalid GlobalValue type $type.");
            }
            values[i] = value
        }
    }
}
