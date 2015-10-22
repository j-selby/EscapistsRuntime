package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * The AppHeader contains attributes relating to the games expected environment (DirectX, Fullscreen, etc), and
 * other important starting variables/data for the game.
 */
public class AppHeader extends Chunk {
    // The flags are a bitset of various attributes.
    private int flags1;
    private int flags2;
    private int flags3;

    // The mode is the graphics mode of the game. See included ENUM below.
    private int mode;

    // The windowWidth/windowHeight fields correspond to the initial dimensions of the game.
    private int windowWidth;
    private int windowHeight;

    // Various variables used by the game engine
    private int initialScore;
    private int initialLives;
    private long numberOfFrames;
    private long frameRate;

    // The controls used by this Application
    private Controls[] controls;

    /**
     * Initialises this chunk with a ByteBuffer.
     *  @param buffer A NIO ByteBuffer in Little-Endian mode
     * @param length Length of the buffer
     */
    @Override
    public void init(ByteReader buffer, int length) {
        int size = buffer.getInt();

        flags1 = buffer.getShort() & 0xFFFF;
        flags2 = buffer.getShort() & 0xFFFF;
        mode = buffer.getShort();
        flags3 = buffer.getShort() & 0xFFFF;

        windowWidth = buffer.getShort() & 0xFFFF;
        windowHeight = buffer.getShort() & 0xFFFF;

        initialScore = buffer.getInt();
        initialLives = buffer.getInt();

        controls = new Controls[4];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = new Controls(buffer);
        }

        // TODO: Read Color here - Global reader implementation on ByteBuffer?
        buffer.position(buffer.position() + 4);

        numberOfFrames = (long) buffer.getInt();
        frameRate = (long) buffer.getInt();
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
        public final short up;
        public final short down;
        public final short left;
        public final short right;
        public final short button1;
        public final short button2;
        public final short button3;
        public final short button4;

        /**
         * Reads a Control section from a AppHeader
         * @param buf The buffer to read from.
         */
        private Controls(ByteReader buf) {
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
