package net.jselby.escapists.data.chunks;

import net.jselby.escapists.ChunkDecoder;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;
import net.jselby.escapists.util.ChunkUtils;

import java.awt.*;
import java.util.List;

/**
 * A frame is a 'level' of sorts, providing a room with code to execute.
 */
public class Frame extends Chunk {
    private List<Chunk> chunks;

    private FrameHeader frameHeader;
    public ObjectInstances objects;
    public VirtualSize virtualSize;
    public Layers layers;

    public String name;
    public int width;
    public int height;
    public Color background;
    public long flags;

    @Override
    public void init(ByteReader buffer, int length) {
        chunks = ChunkDecoder.decodeChunk(buffer);

        FrameName nameChunk = (FrameName) ChunkUtils.popChunk(chunks, FrameName.class);
        if (nameChunk != null) {
            this.name = nameChunk.getContent();
        }

        frameHeader = (FrameHeader) ChunkUtils.popChunk(chunks, FrameHeader.class);
        if (frameHeader == null) {
            throw new IllegalStateException("No FrameHeader in Frame");
        }

        width = frameHeader.width;
        height = frameHeader.height;
        background = frameHeader.background;
        flags = frameHeader.flags;

        virtualSize = (VirtualSize) ChunkUtils.popChunk(chunks, VirtualSize.class);
        objects = (ObjectInstances) ChunkUtils.popChunk(chunks, ObjectInstances.class);
        layers = (Layers) ChunkUtils.popChunk(chunks, Layers.class);

        System.out.println(chunks);
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
