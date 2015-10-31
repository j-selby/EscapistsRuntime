package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ChunkDecoder;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.util.ByteReader;

import java.util.List;

/**
 * A list of items for frames.
 */
public class FrameItems extends Chunk {
    public ObjectDefinition[] info;

    @Override
    public void init(ByteReader buffer, int length) {
        int coupleLength = (int) buffer.getUnsignedInt();

        // Sort by handle
        ObjectDefinition[] unsortedInfo = new ObjectDefinition[coupleLength];
        int maxHandle = 0;
        for (int i = 0; i < coupleLength; i++) {
            unsortedInfo[i] = new ObjectInfo(buffer).def;
            if (unsortedInfo[i].handle > maxHandle) {
                maxHandle = unsortedInfo[i].handle;
            }
        }

        // Do the sort main, just in case chunks are out of order
        info = new ObjectDefinition[maxHandle + 1];
        for (ObjectDefinition definition : unsortedInfo) {
            info[definition.handle] = definition;
        }
    }

    public class ObjectInfo {
        public ObjectDefinition def = new ObjectDefinition();

        public ObjectInfo(ByteReader buffer) {
            // Read info chunks
            List<Chunk> chunks = ChunkDecoder.decodeChunk(buffer, null);

            for (Chunk chunk : chunks) {
                if (chunk instanceof ObjectHeader) {

                    ObjectHeader header = (ObjectHeader) chunk;

                    def.handle = header.handle;
                    def.objectType = header.objectType;
                    def.headerFlags = header.flags;
                    def.headerReserved = header.reserved;
                    def.inkEffect = header.inkEffect;
                    def.inkEffectParameter = header.inkEffectParameter;
                    def.transparent = (def.inkEffect & ((short) Math.pow(2, /* index */ 28))) != 0;
                    def.inkEffect = def.inkEffect & 0xFFFF;
                    /*
                     self.transparent = byteflag.getFlag(inkEffect, 28)
                     self.antialias = byteflag.getFlag(inkEffect, 29)
                     self.inkEffect = inkEffect & 0xFFFF
                     */
                } else if (chunk instanceof ObjectName) {
                    def.name = ((ObjectName) chunk).getContent();
                } else if (chunk instanceof ObjectProperties) {
                    ObjectProperties prop = (ObjectProperties) chunk;
                    prop.load(def.objectType);
                    def.properties = prop;
                } else if (chunk instanceof ObjectEffects) {
                    ObjectEffects effects = (ObjectEffects) chunk;
                    def.shaderId = effects.id;
                    def.shaderItems = effects.data;
                } else {
                    System.err.printf("Bad chunk in FrameItems: %s.\n", chunk.getClass().getSimpleName());
                }
            }
            /**

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
