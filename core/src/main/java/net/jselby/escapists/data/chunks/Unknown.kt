package net.jselby.escapists.data.chunks

import net.jselby.escapists.data.Chunk
import net.jselby.escapists.util.ByteReader

/**
 * Unknown chunks, which have unknown payloads.
 */
class UnknownChunk1 : Chunk() {
    override fun init(buffer: ByteReader?, length: Int) {
    }
}

class UnknownChunk2 : Chunk() {
    override fun init(buffer: ByteReader?, length: Int) {
    }
}

