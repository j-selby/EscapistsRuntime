package net.jselby.escapists.data.chunks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.BitmapFontCache
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import net.jselby.escapists.data.Chunk
import net.jselby.escapists.util.ByteReader
import net.jselby.escapists.util.CompressionUtils
import org.mini2Dx.core.graphics.Sprite

/**
 * Assets are chunks storing data such as images, sounds, and fonts.
 */
class FontBank : Chunk() {
    lateinit var fonts: Array<FontItem?>

    override fun init(buffer: ByteReader, length: Int) {
        val num = buffer.int

        val offset = -1

        fonts = arrayOfNulls<FontItem>(num)
        var highestHandle = 0
        for (i in 0..num - 1) {
            fonts[i] = FontItem(buffer)
            if (fonts[i]!!.handle > highestHandle) {
                highestHandle = fonts[i]!!.handle.toInt()
            }
        }

        // Sort by handle
        val newFonts = arrayOfNulls<FontItem>(highestHandle + 1)
        for (item in fonts) {
            newFonts[(item!!.handle.toInt())] = item
        }
        fonts = newFonts
    }

    inner class FontItem(buffer: ByteReader) {
        val handle: Long
        val checksum: Int
        val references: Int
        val value: LogFont

        init {
            var buffer = buffer

            handle = buffer.unsignedInt

            buffer = CompressionUtils.decompress(buffer)

            val position = buffer.position

            checksum = buffer.int
            references = buffer.int

            val size = buffer.int

            value = LogFont(buffer)
        }
    }

    inner class LogFont//public Font awtFont;

    (buffer: ByteReader) {
        val height: Int
        val width: Int

        val escapement: Int

        val orientation: Int
        val weight: Int

        val italic: Byte
        val underline: Byte

        val strikeOut: Byte
        val charSet: Byte

        val outPrecision: Byte
        val clipPrecision: Byte

        val quality: Byte

        val pitchAndFamily: Byte

        var faceName: String

        var font: BitmapFont? = null
        var fontCache: BitmapFontCache? = null

        init {
            height = buffer.int
            width = buffer.int

            escapement = buffer.int

            orientation = buffer.int
            weight = buffer.int

            italic = buffer.byte
            underline = buffer.byte

            strikeOut = buffer.byte
            charSet = buffer.byte

            outPrecision = buffer.byte
            clipPrecision = buffer.byte

            quality = buffer.byte

            pitchAndFamily = buffer.byte

            faceName = buffer.getString(32)

            if (faceName.equals("Small Fonts", ignoreCase = true)) {
                faceName = "Escapists"
            }
        }

        fun load() {
            val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/$faceName.ttf"))
            val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
            parameter.minFilter = Texture.TextureFilter.Nearest
            parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest
            parameter.size = Math.abs(height)
            parameter.genMipMaps = false
            parameter.flip = height < 0

            font = generator.generateFont(parameter) // font size 12 pixels
            generator.dispose()

            // Also create a cache for this font
            fontCache = BitmapFontCache(font)
        }

    }
}

class ImageBank : Chunk() {
    lateinit var images: Array<ImageItem?>

    override fun init(buffer: ByteReader, length: Int) {
        val numberOfItems = buffer.int

        images = arrayOfNulls<ImageItem>(numberOfItems)
        for (i in 0..numberOfItems - 1) {
            images[i] = ImageItem(buffer)
        }
    }

    /**
     * A ImageItem is a Image from a ImageBank.
     */
    inner class ImageItem(buffer: ByteReader) {
        private val checksum: Int
        private val references: Int

        private val width: Short
        private val height: Short

        private val graphicMode: Byte

        private val flags: Short

        val xHotspot: Short
        val yHotspot: Short

        val actionX: Short
        val actionY: Short

        private val transparent: com.badlogic.gdx.graphics.Color
        var image: Sprite? = null
        private val pixmap: Pixmap

        var handle: Int = 0

        init {
            var buffer = buffer
            handle = buffer.int

            // Decompress
            buffer = CompressionUtils.decompress(buffer)

            val start = buffer.position

            checksum = buffer.int
            references = buffer.int
            val size = buffer.unsignedInt.toInt()

            width = buffer.short
            height = buffer.short

            graphicMode = buffer.byte

            flags = buffer.unsignedByte

            if (flags.toInt() != 16 && flags.toInt() != 0) {
                // Only alpha or nothing
                throw IllegalStateException("Unimplemented: Flag set ($flags)")
            }

            buffer.skipBytes(2) // imgNotUsed

            xHotspot = buffer.short
            yHotspot = buffer.short

            actionX = buffer.short
            actionY = buffer.short

            transparent = buffer.color
            val transparentVal = Color.rgba8888(transparent)
            //System.out.println(Long.toBinaryString(flags) + ":" + flags);
            val alpha = ((flags.toInt() shr 4) and 1) != 0

            if (graphicMode.toInt() != 4) {
                throw IllegalStateException("Unimplemented: Graphics mode != 4 ($graphicMode)")
            }

            val buf = arrayOfNulls<Color>(width * height)
            var pad = 2 - ((width * 3) % 2)
            if (pad == 2) {
                pad = 0
            }
            pad = Math.ceil((pad / 3.toFloat()).toDouble()).toInt()
            var i = 0
            var n = 0
            pixmap = Pixmap(width.toInt(), height.toInt(), Pixmap.Format.RGBA8888)

            val originalPos = buffer.position
            for (y in 0..height - 1) {
                for (x in 0..width - 1) {
                    buffer.position = originalPos + n
                    val b = buffer.unsignedByte.toInt()
                    val g = buffer.unsignedByte.toInt()
                    val r = buffer.unsignedByte.toInt()
                    val `val` = Color.rgba8888(r / 256f, g / 256f, b / 256f, 1f)
                    if (`val` != transparentVal/* && !alpha*/) {
                        pixmap.drawPixel(x, y, `val`)
                    }// else {
                    //buf[i] = new Color(((float) r) / 256f, ((float) g) / 256f,
                    //        ((float) b) / 256f, 1f);
                    //}

                    i++
                    n += 3
                }
                n += 3 * pad
            }

            buffer.position = originalPos + (height.toInt() * width.toInt() * 3)

            /*if (alpha) { // Alpha
                        i = 0;
                        for (int y = 0; y < height; y++) {
                            for (int x = 0; x < width; x++) {
                                // TODO: Implement alpha, if needed
                                int a = buffer.getUnsignedByte();
                                if (!buf[i].equals(transparent)) {
                                    image.setRGB(x, y, val);
                                }
                            }
                        }
                    }*.

                    /*try {
                        ImageIO.write(image, "png", new File("images/" + handle + ".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }*/

        }

        /**
         * Thread-unsafe task for uploading this image to the GPU.
         */
        fun load() {
            this.image = Sprite(Texture(pixmap))
        }
    }
}

class SoundBank : Chunk() {
    lateinit var sounds: Array<SoundItem?>

    override fun init(buffer: ByteReader, length: Int) {
        val itemCount = buffer.int;

        sounds = arrayOfNulls<SoundItem>(itemCount);

        for (i in 0 .. itemCount-1) {
            sounds[i] = SoundItem(buffer);
        }
    }

    inner class SoundItem(buffer: ByteReader) {
        init {
            var start = buffer.position;
            var handle = buffer.unsignedInt;
            var checksum = buffer.int;
            var references = buffer.int;
            var decompressedLength = buffer.int;
            var flags = buffer.unsignedInt;
            var reserved = buffer.int;
            var nameLength = buffer.int;

            var data : ByteReader;
            if (((flags.toInt() shr 5) and 1) != 0) {
                data = buffer.getReader(decompressedLength);
            } else {
                val compressedLength = buffer.unsignedInt;
                data = CompressionUtils.decompress(buffer, compressedLength.toInt(), decompressedLength);
            }

            val name = data.getString(nameLength * 2);
            decompressedLength -= nameLength * 2;

            data = data.getReader(decompressedLength);

            // Attempt to identify the file format of this sound
            var headerString = String(data.getBytes(4));

            var extension : String;
            if (headerString == "RIFF") {
                extension = "wav";
            } else if (headerString == "AIFF") {
                extension = "aiff";
            } else if (headerString == "OggS") {
                extension = "ogg";
            } else {
                extension = "mod";
            }

            data.position = 0;

            /*val out = FileOutputStream("sound/$name.$extension");
            out.write(data.getBytes(decompressedLength));
            out.close();*/
        }
    }
}