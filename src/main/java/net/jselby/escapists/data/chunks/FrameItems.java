package net.jselby.escapists.data.chunks;

import net.jselby.escapists.ChunkDecoder;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;
import net.jselby.escapists.util.ChunkUtils;

import java.util.List;

/**
 * A list of items for frames.
 */
public class FrameItems extends Chunk {
    private ObjectInfo[] info;

    @Override
    public void init(ByteReader buffer, int length) {
        int coupleLength = (int) buffer.getUnsignedInt();
        info = new ObjectInfo[coupleLength];
        for (int i = 0; i < coupleLength; i++) {
            info[i] = new ObjectInfo(buffer);
        }
    }

    public class ObjectInfo {
        public ObjectInfo(ByteReader buffer) {
            // Read info chunks
            List<Chunk> chunks = ChunkDecoder.decodeChunk(buffer);

            /**
             infoChunks = self.new(ChunkList, reader)
             properties = None
             for chunk in infoChunks.items:
             loader = chunk.loader
             klass = loader.__class__
             if klass is ObjectName:
             self.name = loader.value
             elif klass is ObjectHeader:
             self.handle = loader.handle
             self.objectType = loader.objectType
             self.flags = loader.flags
             inkEffect = loader.inkEffect
             self.transparent = byteflag.getFlag(inkEffect, 28)
             self.antialias = byteflag.getFlag(inkEffect, 29)
             self.inkEffect = inkEffect & 0xFFFF
             self.inkEffectValue = loader.inkEffectParameter
             elif klass is ObjectProperties:
             properties = loader
             elif klass is ObjectEffects:
             self.shaderId = loader.id
             self.items = loader.items
             else:
             print chunk.getName(), chunk.id
             properties.load(self.objectType)
             self.properties = properties

             */
        }
    }
}
