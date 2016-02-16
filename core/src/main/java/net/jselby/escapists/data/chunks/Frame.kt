package net.jselby.escapists.data.chunks

import com.badlogic.gdx.graphics.Color
import net.jselby.escapists.data.Chunk
import net.jselby.escapists.data.ChunkDecoder
import net.jselby.escapists.data.ObjectDefinition
import net.jselby.escapists.data.StringChunk
import net.jselby.escapists.util.ByteReader
import net.jselby.escapists.util.ChunkUtils

/**
 * Frame related Chunks (names, properties, etc).
 */
class FrameName : StringChunk()

class Frame : Chunk() {
    private var chunks: List<Chunk>? = null

    private var frameHeader: FrameHeader? = null
    var objects: ObjectInstances? = null
    var virtualSize: VirtualSize? = null
    var layers: Layers? = null
    private var fadeIn: FadeIn? = null
    private var fadeOut: FadeOut? = null
    var events: Events? = null

    var name: String? = null
    var width: Int = 0
    var height: Int = 0
    var background: Color? = null
    var flags: Long = 0


    override fun init(buffer: ByteReader, length: Int) {
        chunks = ChunkDecoder.decodeChunk(buffer)

        val nameChunk = ChunkUtils.popChunk(chunks, FrameName::class.java) as FrameName?
        if (nameChunk != null) {
            this.name = nameChunk.getContent()
        }

        frameHeader = ChunkUtils.popChunk(chunks, FrameHeader::class.java) as FrameHeader?
        if (frameHeader == null) {
            throw IllegalStateException("No FrameHeader in Frame")
        }

        width = frameHeader!!.width
        height = frameHeader!!.height
        background = frameHeader!!.background
        flags = frameHeader!!.flags

        virtualSize = ChunkUtils.popChunk(chunks, VirtualSize::class.java) as VirtualSize?
        objects = ChunkUtils.popChunk(chunks, ObjectInstances::class.java) as ObjectInstances?
        layers = ChunkUtils.popChunk(chunks, Layers::class.java) as Layers?

        fadeIn = ChunkUtils.popChunk(chunks, FadeIn::class.java) as FadeIn?
        fadeOut = ChunkUtils.popChunk(chunks, FadeOut::class.java) as FadeOut?
        events = ChunkUtils.popChunk(chunks, Events::class.java) as Events?
        /*

        newHeader = newChunks.popChunk(FrameHeader)

        self.width = newHeader.width
        self.height = newHeader.height
        self.background = newHeader.background
        self.flags = newHeader.flags

        newVirtual = newChunks.popChunk(VirtualSize)
        self.top = newVirtual.top
        self.bottom = newVirtual.bottom
        self.left = newVirtual.left
        self.right = newVirtual.right

        self.instances = newChunks.popChunk(ObjectInstances, True)

        self.layers = newChunks.popChunk(Layers)

        try:
            layerEffects = newChunks.popChunk(LayerEffects)
            for index, item in enumerate(layerEffects.effects):
                self.layers.items[index].effect = item
        except IndexError:
            pass

        self.events = newChunks.popChunk(Events)
        self.maxObjects = self.events.maxObjects

        self.palette = newChunks.popChunk(FramePalette, True)

        try:
            self.movementTimer = newChunks.popChunk(MovementTimerBase).value
        except IndexError:
            pass

        self.fadeIn = newChunks.popChunk(FadeIn, True)
        self.fadeOut = newChunks.popChunk(FadeOut, True)
         */
    }
}

class FrameEffects : Chunk() {
    override fun init(buffer: ByteReader, length: Int) {
        buffer.skipBytes(2)
    }
}

class FrameHandles : Chunk() {
    private var handles: ShortArray? = null

    override fun init(buffer: ByteReader, length: Int) {
        val count = length / 2
        handles = ShortArray(count)
        for (i in 0..count - 1) {
            (handles as ShortArray)[i] = buffer.short
        }

    }
}

class FrameHeader : Chunk() {
    var width: Int = 0
    var height: Int = 0
    var background: Color? = null
    var flags: Long = 0

    override fun init(buffer: ByteReader, length: Int) {
        width = buffer.int
        height = buffer.int
        background = buffer.color
        flags = buffer.unsignedInt
    }
}

class FrameItems : Chunk() {
    var info: Array<ObjectDefinition?>? = null

    override fun init(buffer: ByteReader, length: Int) {
        val coupleLength = buffer.unsignedInt.toInt()

        // Sort by handle
        val unsortedInfo = arrayOfNulls<ObjectDefinition>(coupleLength)
        var maxHandle = 0
        for (i in 0..coupleLength - 1) {
            unsortedInfo[i] = ObjectInfo(buffer).def
            if (unsortedInfo[i]!!.handle > maxHandle) {
                maxHandle = unsortedInfo[i]!!.handle.toInt()
            }
        }

        // Do the sort main, just in case chunks are out of order
        info = arrayOfNulls<ObjectDefinition>(maxHandle + 1)
        for (definition in unsortedInfo) {
            (info as Array<ObjectDefinition?>)[definition!!.handle.toInt()] = definition
        }
    }

    inner class ObjectInfo(buffer: ByteReader) {
        var def = ObjectDefinition()

        init {
            // Read info chunks
            val chunks = ChunkDecoder.decodeChunk(buffer)

            for (chunk in chunks) {
                if (chunk is ObjectHeader) {

                    def.handle = chunk.handle
                    def.objectType = chunk.objectType
                    def.headerFlags = chunk.flags
                    def.headerReserved = chunk.reserved
                    def.inkEffect = chunk.inkEffect
                    def.inkEffectParameter = chunk.inkEffectParameter
                    def.transparent = (def.inkEffect.toInt() and (Math.pow(2.0, /* index */ 28.0).toInt())) != 0
                    def.inkEffect = def.inkEffect and 65535
                    /*
                     self.transparent = byteflag.getFlag(inkEffect, 28)
                     self.antialias = byteflag.getFlag(inkEffect, 29)
                     self.inkEffect = inkEffect & 0xFFFF
                     */
                } else if (chunk is ObjectName) {
                    def.name = chunk.getContent()
                } else if (chunk is ObjectProperties) {
                    chunk.load(def.objectType)
                    def.properties = chunk
                } else if (chunk is ObjectEffects) {
                    def.shaderId = chunk.id
                    def.shaderItems = chunk.data
                } else {
                    System.err.printf("Bad chunk in FrameItems: %s.\n", chunk.javaClass.simpleName)
                }
            }
            /**

             * elif klass is ObjectEffects:
             * self.shaderId = loader.id
             * self.items = loader.items
             * else:
             * print chunk.getName(), chunk.id
             * properties.load(self.objectType)
             * self.properties = properties

             */
        }
    }
}

class FramePalette : Chunk() {
    private var color: Array<Color?>? = null

    override fun init(buffer: ByteReader, length: Int) {
        buffer.skipBytes(4)

        color = arrayOfNulls<Color>(256)

        for (i in color!!.indices) {
            (color as Array<Color?>)[i] = buffer.color
        }
    }
}
