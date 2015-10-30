package net.jselby.escapists.game;

import com.badlogic.gdx.graphics.Pixmap;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.*;
import net.jselby.escapists.util.ChunkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The application is the primary component of the package. This stores most global metadata,
 * manages resources, object definitions, and so on.
 *
 * @author j_selby
 */
public class Application {
    private final static Class[] IGNORED_CHUNKS = new Class[]{
            AppMenu.class,
            ExtData.class,
            ExtensionList.class,
            OtherExtensions.class,
            Shaders.class,
            SecNum.class,
            FrameHandles.class,
            ExeOnly.class,
            Protection.class,
            FontBank.class
    };

    private final EscapistsRuntime runtime;
    private List<Chunk> chunks;

    private String name;
    private String author;
    private Pixmap icon;
    private String copyright;

    public List<Scene> frames;
    public ImageBank.ImageItem[] images;
    public ObjectDefinition[] objectDefs;

    private int width;
    private int height;
    private int targetFPS;
    public FontBank.FontItem[] fonts;

    public Application(EscapistsRuntime runtime, List<Chunk> chunks) {
        this.runtime = runtime;
        this.chunks = chunks;
    }

    public void init() throws IOException {
        // Get instances of all important chunks
        AppName nameChunk = (AppName) ChunkUtils.popChunk(chunks, AppName.class);
        AppAuthor authorChunk = (AppAuthor) ChunkUtils.popChunk(chunks, AppAuthor.class);
        AppIcon appIconChunk = (AppIcon) ChunkUtils.popChunk(chunks, AppIcon.class);
        Copyright copyrightChunk = (Copyright) ChunkUtils.popChunk(chunks, Copyright.class);
        AppHeader appHeader = (AppHeader) ChunkUtils.popChunk(chunks, AppHeader.class);
        EditorFilename editorFilenameChunk = (EditorFilename) ChunkUtils.popChunk(chunks, EditorFilename.class);
        TargetFilename targetFilenameChunk = (TargetFilename) ChunkUtils.popChunk(chunks, TargetFilename.class);
        ExtendedHeader extendedHeaderChunk = (ExtendedHeader) ChunkUtils.popChunk(chunks, ExtendedHeader.class);
        AboutText aboutTextChunk = (AboutText) ChunkUtils.popChunk(chunks, AboutText.class);
        GlobalValues globalValuesChunk = (GlobalValues) ChunkUtils.popChunk(chunks, GlobalValues.class);
        GlobalStrings globalStringsChunk = (GlobalStrings) ChunkUtils.popChunk(chunks, GlobalStrings.class);
        FrameItems frameItemsChunk = (FrameItems) ChunkUtils.popChunk(chunks, FrameItems.class);
        FontBank fontBankChunk = (FontBank) ChunkUtils.popChunk(chunks, FontBank.class);

        // Get remaining chunks
        ImageBank imageBankChunk = (ImageBank) ChunkUtils.popChunk(chunks, ImageBank.class);

        assert nameChunk != null; // Impossible without
        assert authorChunk != null;
        assert appIconChunk != null;
        assert copyrightChunk != null;
        assert imageBankChunk != null;
        assert frameItemsChunk != null;
        assert appHeader != null;
        assert fontBankChunk != null;

        name = nameChunk.getContent();
        author = authorChunk.getContent();
        icon = appIconChunk.image;
        copyright = copyrightChunk.getContent();

        width = appHeader.windowWidth;
        height = appHeader.windowHeight;
        objectDefs = frameItemsChunk.info;
        targetFPS = (int) appHeader.frameRate;
        fonts = fontBankChunk.fonts;

        // Discover highest handle
        int highestHandle = 0;
        for (ImageBank.ImageItem imageItem : imageBankChunk.images) {
            if (imageItem.handle > highestHandle) {
                highestHandle = imageItem.handle;
            }
        }

        // Update images
        images = new ImageBank.ImageItem[highestHandle + 1];
        for (ImageBank.ImageItem imageItem : imageBankChunk.images) {
            images[imageItem.handle] = imageItem;
        }

        // Get frames
        frames = new ArrayList<Scene>();
        Frame frame;
        while((frame = (Frame) ChunkUtils.popChunk(chunks, Frame.class)) != null) {
            frames.add(new Scene(runtime, frame));
        }

        // Remove bad/unknown chunks
        for (Chunk chunk : chunks.toArray(new Chunk[chunks.size()])) {
            for (Class classDef : IGNORED_CHUNKS) {
                if (chunk.getClass() == classDef) {
                    chunks.remove(chunk);
                    break;
                }
            }
        }

        if (chunks.size() > 0) {
            // Unknown param?
            throw new IllegalStateException("Unhandled chunks: " + chunks);
        }

        chunks = null; // Dereference
    }

    public int getWindowWidth() {
        return width;
    }

    public int getWindowHeight() {
        return height;
    }

    public int getTargetFPS() {
        return targetFPS;
    }

    public Pixmap getAppIcon() {
        return icon;
    }
}
