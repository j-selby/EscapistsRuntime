package net.jselby.escapists.game;

import com.badlogic.gdx.graphics.Pixmap;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.*;
import net.jselby.escapists.data.ini.PropertiesFile;
import net.jselby.escapists.util.ChunkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            FontBank.class,

            ImageOffsets.class,
            FontOffsets.class,
            SoundOffsets.class,

            UnknownChunk1.class,
            UnknownChunk2.class
    };

    private final EscapistsRuntime runtime;
    private List<Chunk> chunks;

    public String name;
    public String author;
    public Pixmap icon;
    public String copyright;

    public List<Scene> frames;
    public Map<String, PropertiesFile> properties = new HashMap<String, PropertiesFile>();
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

    public void init(EscapistsGame game) {
        // Get instances of all important chunks
        AppName nameChunk = (AppName) ChunkUtils.popChunk(chunks, AppName.class);
        AppAuthor authorChunk = (AppAuthor) ChunkUtils.popChunk(chunks, AppAuthor.class);
        AppIcon appIconChunk = (AppIcon) ChunkUtils.popChunk(chunks, AppIcon.class);
        Copyright copyrightChunk = (Copyright) ChunkUtils.popChunk(chunks, Copyright.class);
        AppHeader appHeader = (AppHeader) ChunkUtils.popChunk(chunks, AppHeader.class);
        GlobalValues globalValuesChunk = (GlobalValues) ChunkUtils.popChunk(chunks, GlobalValues.class);
        GlobalStrings globalStringsChunk = (GlobalStrings) ChunkUtils.popChunk(chunks, GlobalStrings.class);
        FrameItems frameItemsChunk = (FrameItems) ChunkUtils.popChunk(chunks, FrameItems.class);
        final FontBank fontBankChunk = (FontBank) ChunkUtils.popChunk(chunks, FontBank.class);

        ChunkUtils.popChunk(chunks, EditorFilename.class);
        ChunkUtils.popChunk(chunks, TargetFilename.class);
        ChunkUtils.popChunk(chunks, ExtendedHeader.class);
        ChunkUtils.popChunk(chunks, AboutText.class);
        ChunkUtils.popChunk(chunks, SoundBank.class);

        // Get remaining chunks
        final ImageBank imageBankChunk = (ImageBank) ChunkUtils.popChunk(chunks, ImageBank.class);

        assert nameChunk != null; // Impossible without
        assert authorChunk != null;
        assert appIconChunk != null;
        assert copyrightChunk != null;
        assert imageBankChunk != null;
        assert frameItemsChunk != null;
        assert appHeader != null;
        assert fontBankChunk != null;
        assert globalValuesChunk != null;
        assert globalStringsChunk != null;

        name = nameChunk.getContent();
        author = authorChunk.getContent();
        icon = appIconChunk.getImage();
        copyright = copyrightChunk.getContent();

        width = appHeader.getWindowWidth();
        height = appHeader.getWindowHeight();
        objectDefs = frameItemsChunk.getInfo();
        targetFPS = (int) appHeader.getFrameRate();
        fonts = fontBankChunk.fonts;

        // Discover highest handle
        int highestHandle = 0;
        for (ImageBank.ImageItem imageItem : imageBankChunk.images) {
            if (imageItem.getHandle() > highestHandle) {
                highestHandle = imageItem.getHandle();
            }
        }

        // Update images
        images = new ImageBank.ImageItem[highestHandle + 1];
        for (ImageBank.ImageItem imageItem : imageBankChunk.images) {
            images[imageItem.getHandle()] = imageItem;
            imageItem.load();
        }

        // Prepare fonts
        for (FontBank.FontItem font : fontBankChunk.fonts) {
            if (font != null) {
                font.getValue().load();
            }
        }

        // Get frames
        frames = new ArrayList<Scene>();
        Frame frame;
        while((frame = (Frame) ChunkUtils.popChunk(chunks, Frame.class)) != null) {
            frames.add(new Scene(runtime, frame, game));
        }

        System.out.println(frames);

        // Prepare global values
        Number[] values = globalValuesChunk.getValues();
        for (int i = 0; i < values.length; i++) {
            game.globalInts.put(i, values[i]);
        }

        String[] stringValues = globalStringsChunk.getData();
        for (int i = 0; i < stringValues.length; i++) {
            game.globalStrings.put(i, stringValues[i]);
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
}
