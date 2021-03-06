package net.jselby.escapists.data.chunks;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * A Transition is a effect used when transitioning between frames.
 */
public abstract class Transition extends Chunk {
    protected byte[] module;

    protected byte[] name;
    protected int duration;
    protected long flags;

    protected Color color;

    protected String moduleFile;

    protected byte[] parameterData;

    @Override
    public void init(ByteReader buffer, int length) {
        int currentPosition = buffer.getPosition();

        this.module = buffer.getBytes(4);
        this.name = buffer.getBytes(4);
        this.duration = buffer.getInt();

        this.flags = buffer.getUnsignedInt();

        this.color = buffer.getColor();

        int nameOffset = buffer.getInt();

        int parameterOffset = buffer.getInt();
        int parameterSize = buffer.getInt();

        buffer.setPosition(currentPosition + nameOffset);
        this.moduleFile = buffer.getString();

        buffer.setPosition(currentPosition + parameterOffset);
        this.parameterData = buffer.getBytes(parameterSize);
    }

    public abstract boolean getFadeIn();
}
