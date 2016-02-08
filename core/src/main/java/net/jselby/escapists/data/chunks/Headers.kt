package net.jselby.escapists.data.chunks

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import net.jselby.escapists.data.Chunk
import net.jselby.escapists.data.StringChunk
import net.jselby.escapists.util.ByteReader
import java.nio.LongBuffer
import java.util.*

/**
 * This file contains header "App"-style chunks and other purely String based headers.
 */

class ReflectionsHandle private constructor()

class AboutText : StringChunk()

class AppAuthor : StringChunk()

class AppName : StringChunk()

class Copyright : StringChunk()

class EditorFilename : StringChunk()

class TargetFilename : StringChunk()

class ExeOnly : Chunk() {
    var exeOnly : Byte = 0

    override fun init(buffer: ByteReader, length: Int) {
        exeOnly = buffer.byte;
    }
}

class ExtData : Chunk() {
    override fun init(buffer: ByteReader, length: Int) {
        val check = buffer.int
        if (check != 0) {
            System.err.printf("Invalid check value in %s: %d.\n", javaClass.name, check)
        }
    }
}

class ExtendedHeader : Chunk() {
    // Boolean matrix
    var flags: Int = 0

    // Build fields
    var buildType: Long = 0
    var buildFlags: Long = 0

    // Screen properties
    var screenRatioTolerance: Short = 0
    var screenAngle: Short = 0

    override fun init(buffer: ByteReader, length: Int) {
        flags = buffer.int

        buildType = (buffer.int.toLong() and 0xffffffffL)
        buildFlags = (buffer.int.toLong() and 0xffffffffL)

        screenRatioTolerance = buffer.short
        screenAngle = buffer.short
    }
}

class ExtensionList : Chunk() {
    private var extensions: Array<Extension?>? = null

    override fun init(buffer: ByteReader, length: Int) {
        val num = buffer.unsignedShort
        val preloadExtensions = buffer.unsignedShort

        extensions = arrayOfNulls<Extension?>(num)
        for (i in 0..num - 1) {
            (extensions as Array<Extension?>)[i] = Extension(buffer)
        }
    }

    /**
     * A extension is a executable library used by the engine.
     */
    private inner class Extension(buffer: ByteReader) {
        val handle: Short
        val magicNumber: Int
        val versionLS: Int
        val versionMS: Int
        val name: String
        val subType: String

        init {
            val currentPosition = buffer.position
            var size = buffer.short.toInt()
            if (size < 0) {
                size = -size
            }

            handle = buffer.short
            magicNumber = buffer.int
            versionLS = buffer.int
            versionMS = buffer.int
            name = buffer.string
            subType = buffer.string
            buffer.position = currentPosition + size
        }
    }
}

class OtherExtensions : Chunk() {
    private var extensions: Array<String?>? = null

    override fun init(buffer: ByteReader, length: Int) {
        val size = buffer.unsignedShort
        extensions = arrayOfNulls<String>(size)
        for (i in 0..size - 1) {
            extensions!![i] = buffer.string
        }
    }
}

class AppHeader : Chunk() {
    // The flags are a bitset of various attributes.
    var flags1: Int = 0
    var flags2: Int = 0
    var flags3: Int = 0

    // The mode is the graphics mode of the game. See included ENUM below.
    var mode: Int = 0

    // The windowWidth/windowHeight fields correspond to the initial dimensions of the game.
    var windowWidth: Int = 0
    var windowHeight: Int = 0

    // Various variables used by the game engine
    var initialScore: Long = 0
    var initialLives: Long = 0
    var numberOfFrames: Long = 0
    var frameRate: Long = 0

    // The controls used by this Application
    var controls: Array<Controls?> = arrayOfNulls(0)

    var borderColor: Color? = null

    /**
     * Initialises this chunk with a ByteBuffer.
     * @param buffer A NIO ByteBuffer in Little-Endian mode
     * *
     * @param length Length of the buffer
     */
    override fun init(buffer: ByteReader, length: Int) {
        val size = buffer.int

        flags1 = buffer.unsignedShort
        flags2 = buffer.unsignedShort
        mode = buffer.short.toInt()
        flags3 = buffer.unsignedShort

        windowWidth = buffer.short.toInt() and 65535
        windowHeight = buffer.short.toInt() and 65535

        initialScore = buffer.unsignedInt.inv()
        initialLives = buffer.unsignedInt.inv()

        controls = arrayOfNulls<Controls>(4)
        for (i in controls.indices) {
            controls[i] = Controls(buffer)
        }
        for (i in controls.indices) {
            if (controls[i] != null) {
                controls[i]!!.readKeys(buffer)
            }
        }

        borderColor = buffer.color

        numberOfFrames = buffer.unsignedInt
        frameRate = buffer.unsignedInt
    }

    enum class GraphicsMode private constructor(private val id: Int) {
        colors_16_million(4),
        colors_65536(7),
        colors_32768(6),
        colors_256(3);

        companion object {
            /**
             * Returns a corresponding GraphicsMode for a particular ID.
             * @param id The ID to search for
             * *
             * @return The GraphicsMode for the ID, or null if not found.
             */
            fun getForID(id: Int): GraphicsMode? {
                for (mode in values()) {
                    if (mode.id == id) {
                        return mode
                    }
                }
                return null
            }
        }
    }

    inner class Controls
    /**
     * Reads a Control section from a AppHeader
     * @param buf The buffer to read from.
     */
    constructor(buf: ByteReader) {
        private val controlType: Short

        var up: Short = 0
        var down: Short = 0
        var left: Short = 0
        var right: Short = 0
        var button1: Short = 0
        var button2: Short = 0
        var button3: Short = 0
        var button4: Short = 0

        init {
            controlType = buf.short

        }

        fun readKeys(buf: ByteReader) {
            up = buf.short
            down = buf.short
            left = buf.short
            right = buf.short

            button1 = buf.short
            button2 = buf.short
            button3 = buf.short
            button4 = buf.short
        }
    }
}

class AppIcon : Chunk() {
    var image: Pixmap? = null

    override fun init(buffer: ByteReader, length: Int) {
        buffer.position = buffer.int + buffer.position - 4
        image = Pixmap(16, 16, Pixmap.Format.RGBA8888)

        val colorIndexes = arrayOfNulls<Color>(16 * 16)

        for (i in 0..16 * 16 - 1) {
            // Read RGB set (in reverse)
            val b = buffer.unsignedByte.toInt()
            val g = buffer.unsignedByte.toInt()
            val r = buffer.unsignedByte.toInt()
            buffer.skipBytes(1)
            colorIndexes[i] = Color((r.toFloat()) / 256f, (g.toFloat()) / 256f,
                    (b.toFloat()) / 256f, 1f)
        }

        val points = arrayOfNulls<Color>(16 * 16)
        var i = 0
        for (y in 0..15) {
            for (x in 0..15) {
                points[16 * 16 - i - 1] = colorIndexes[buffer.unsignedByte.toInt()]
                i++
            }
        }

        var index = 0
        i = 0
        while (i < 16 * 16 / 8) {
            val set = BitSet.valueOf(LongBuffer.allocate(buffer.unsignedByte.toInt()))
            for (ii in set.length() - 1 downTo 0) {
                val oldColor = points[index]
                points[index] = Color(oldColor!!.r, oldColor.g,
                        oldColor.b, if (set.get(ii)) 1f else 0f)
                index++
            }
            i++
        }

        // Finally, paint
        i = 0
        for (y in 0..15) {
            for (x in 0..15) {
                (image as Pixmap).drawPixel(x, y, points[i]!!.toIntBits())
                i++
            }
        }
    }
}

class AppMenu : Chunk() {
    override fun init(buffer: ByteReader, length: Int) {
        val currentPosition = buffer.position
        // TODO: Implement
        /*int headerSize (int) ((long) bb.getInt() & 0xffffffffL);
        currentPosition = reader.tell()
        headerSize = reader.readInt(True)
        menuOffset = reader.readInt()
        menuSize = reader.readInt()
        if menuSize == 0:
        return
                accelOffset = reader.readInt()
        accelSize = reader.readInt()
        reader.seek(currentPosition + menuOffset)
        reader.skipBytes(4)
        self.loadItems(reader)
        reader.seek(currentPosition + accelOffset)
        for i in range(accelSize / 8):
        self.accelShift.append(Key(reader.readByte()))
        reader.skipBytes(1)
        self.accelKey.append(Key(reader.readShort()))
        self.accelId.append(reader.readShort())
        reader.skipBytes(2)*/
    }
}

class Protection : Chunk() {
    private var checksum: Long = 0

    override fun init(buffer: ByteReader, length: Int) {
        checksum = buffer.unsignedInt
    }
}