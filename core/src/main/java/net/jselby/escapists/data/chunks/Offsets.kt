package net.jselby.escapists.data.chunks

import net.jselby.escapists.data.Chunk
import net.jselby.escapists.util.ByteReader

/**
 * Offsets store positions of audiovisual assets.
 */
class ImageOffsets : Chunk() {
    override fun init(buffer: ByteReader, length: Int) {
    }
}

class FontOffsets : Chunk() {
    override fun init(buffer: ByteReader, length: Int) {
    }
}

class SoundOffsets : Chunk() {
    override fun init(buffer: ByteReader, length: Int) {
    }
}
