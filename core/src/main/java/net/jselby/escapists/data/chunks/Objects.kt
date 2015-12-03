package net.jselby.escapists.data.chunks

import net.jselby.escapists.data.Chunk
import net.jselby.escapists.data.StringChunk
import net.jselby.escapists.data.objects.Backdrop
import net.jselby.escapists.data.objects.ObjectCommon
import net.jselby.escapists.data.objects.ObjectDefinitionProperties
import net.jselby.escapists.data.objects.QuickBackdrop
import net.jselby.escapists.util.ByteReader

/**
 * An object is a definition of a object within the game world.
 */

class ObjectHeader : Chunk() {
    var handle: Short = 0
    var objectType: Short = 0

    var flags: Int = 0
    var reserved: Short = 0

    var inkEffect: Long = 0
    var inkEffectParameter: Long = 0

    override fun init(buffer: ByteReader, length: Int) {
        handle = buffer.short
        objectType = buffer.short
        flags = buffer.unsignedShort
        reserved = buffer.short // Not used
        inkEffect = buffer.unsignedInt
        inkEffectParameter = buffer.unsignedInt
    }
}

class ObjectName : StringChunk()

class ObjectInstances : Chunk() {
    lateinit var instances: Array<ObjectInstance?>

    override fun init(buffer: ByteReader, length: Int) {
        val count = buffer.unsignedInt.toInt()
        instances = arrayOfNulls<ObjectInstance>(count)

        for (i in 0..count - 1) {
            instances[i] = ObjectInstance(buffer)
        }
    }

    /**
     * A ObjectInstance is a instance of a object within the game world.
     */
    inner class ObjectInstance(buffer: ByteReader) {
        val handle: Int
        val objectInfo: Int

        val x: Int
        val y: Int

        val parentType: Short
        val parentHandle: Short
        val layer: Short

        init {
            handle = buffer.unsignedShort
            objectInfo = buffer.unsignedShort
            x = buffer.int
            y = buffer.int
            parentType = buffer.short
            parentHandle = buffer.short // Object info
            layer = buffer.short
            buffer.skipBytes(2)
        }

        override fun toString(): String {
            return "ObjectInstance{x=$x,y=$y,handle=$handle,objectInfo=$objectInfo,parentType=$parentType,parentHandle=$parentHandle,layer=$layer}"
        }
    }
}

class ObjectProperties : Chunk() {
    private var buffer: ByteReader? = null
    private var bufferLength: Int = 0

    var isCommon = false
    var objectType: ObjectTypes? = null

    var properties: ObjectDefinitionProperties? = null

    override fun init(buffer: ByteReader, length: Int) {
        this.buffer = buffer
        this.bufferLength = length
    }

    fun load(objectTypeRaw: Short) {
        this.objectType = getById(objectTypeRaw.toInt())

        if (objectType == ObjectTypes.QuickBackdrop) {
            properties = QuickBackdrop()
        } else if (objectType == ObjectTypes.Backdrop) {
            properties = Backdrop()
        } else {
            isCommon = true
            properties = ObjectCommon(objectTypeRaw.toInt())
        }

        properties!!.read(buffer, bufferLength)

        buffer = null // Remove ref
    }

    enum class ObjectTypes private constructor(val id: Int) {
        Player(-7),
        Keyboard(-6),
        Create(-5),
        Timer(-4),
        Game(-3),
        Speaker(-2),
        System(-1),
        QuickBackdrop(0),
        Backdrop(1),
        Active(2),
        Text(3),
        Question(4),
        Score(5),
        Lives(6),
        Counter(7),
        RTF(8),
        SubApplication(9);
    }
}

fun getById(id: Int): ObjectProperties.ObjectTypes? {
    for (type in ObjectProperties.ObjectTypes.values) {
        if (type.id == id) {
            return type
        }
    }
    return null
}

class ObjectEffects : Chunk() {
    var id: Long = 0
    lateinit var data: Array<ByteArray?>

    override fun init(buffer: ByteReader, length: Int) {
        id = buffer.unsignedInt
        val dataLength = buffer.unsignedInt.toInt()
        data = arrayOfNulls<ByteArray>(dataLength)

        for (i in 0..dataLength - 1) {
            data[i] = buffer.getBytes(4)
        }
    }
}
