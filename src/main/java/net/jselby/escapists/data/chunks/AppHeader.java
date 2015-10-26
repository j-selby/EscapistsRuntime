package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

import java.awt.*;

/**
 * The AppHeader contains attributes relating to the games expected environment (DirectX, Fullscreen, etc), and
 * other important starting variables/data for the game.
 */
public class AppHeader extends Chunk {
    // The flags are a bitset of various attributes.
    public int flags1;
    public int flags2;
    public int flags3;

    // The mode is the graphics mode of the game. See included ENUM below.
    public int mode;

    // The windowWidth/windowHeight fields correspond to the initial dimensions of the game.
    public int windowWidth;
    public int windowHeight;

    // Various variables used by the game engine
    public long initialScore;
    public long initialLives;
    public long numberOfFrames;
    public long frameRate;

    // The controls used by this Application
    public Controls[] controls;

    public Color borderColor;

    /**
     * Initialises this chunk with a ByteBuffer.
     *  @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    @Override
    public void init(ByteReader buffer, int length) {
        int size = buffer.getInt();

        flags1 = buffer.getUnsignedShort();
        flags2 = buffer.getUnsignedShort();
        mode = buffer.getShort();
        flags3 = buffer.getUnsignedShort();

        windowWidth = buffer.getShort() & 0xFFFF;
        windowHeight = buffer.getShort() & 0xFFFF;

        initialScore = ~buffer.getUnsignedInt();
        initialLives = ~buffer.getUnsignedInt();

        controls = new Controls[4];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = new Controls(buffer);
        }
        for (int i = 0; i < controls.length; i++) {
            controls[i].readKeys(buffer);
        }

        borderColor = buffer.getColor();

        numberOfFrames = buffer.getUnsignedInt();
        frameRate = buffer.getUnsignedInt();
    }

    public enum GraphicsMode {
        colors_16_million(4),
        colors_65536(7),
        colors_32768(6),
        colors_256(3);

        private int id;

        GraphicsMode(int id) {
            this.id = id;
        }

        /**
         * Returns a corresponding GraphicsMode for a particular ID.
         * @param id The ID to search for
         * @return The GraphicsMode for the ID, or null if not found.
         */
        public static GraphicsMode getForID(int id) {
            for (GraphicsMode mode : values()) {
                if (mode.id == id) {
                    return mode;
                }
            }
            return null;
        }
    }

    public class Controls {
        private final short controlType;

        public short up;
        public short down;
        public short left;
        public short right;
        public short button1;
        public short button2;
        public short button3;
        public short button4;

        /**
         * Reads a Control section from a AppHeader
         * @param buf The buffer to read from.
         */
        private Controls(ByteReader buf) {
            controlType = buf.getShort();

        }

        public void readKeys(ByteReader buf) {
            up = buf.getShort();
            down = buf.getShort();
            left = buf.getShort();
            right = buf.getShort();

            button1 = buf.getShort();
            button2 = buf.getShort();
            button3 = buf.getShort();
            button4 = buf.getShort();
        }
    }
}
