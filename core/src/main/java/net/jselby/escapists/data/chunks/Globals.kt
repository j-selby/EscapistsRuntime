package net.jselby.escapists.data.chunks

import net.jselby.escapists.data.Chunk
import net.jselby.escapists.util.ByteReader

class GlobalStrings : Chunk() {
    var data: Array<String?>? = null

    override fun init(buffer: ByteReader, length: Int) {
        val l = (buffer.int.toLong() and 0xffffffffL)
        data = arrayOfNulls<String>(l.toInt())

        for (i in 0..l - 1) {
            (data as Array<String?>)[i.toInt()] = buffer.string
        }
    }
}

class GlobalValues : Chunk() {
    var values: FloatArray? = null

    override fun init(buffer: ByteReader, length: Int) {
        val numberOfItems = buffer.short.toInt() and 65535

        values = FloatArray(numberOfItems)
        for (i in 0..numberOfItems - 1) {
            val type = buffer.byte.toInt() and 255
            var value = 0f
            if (type == 2) {
                value = buffer.float
            } else if (type == 0) {
                value = buffer.int.toFloat()
            } else {
                System.out.printf("Invalid GlobalValue type %d.\n", type)
            }
            (values as FloatArray)[i] = value
        }
    }
}
