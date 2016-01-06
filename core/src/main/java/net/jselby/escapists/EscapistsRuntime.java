package net.jselby.escapists;

import com.badlogic.gdx.Gdx;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ChunkDecoder;
import net.jselby.escapists.data.pe.PEFile;
import net.jselby.escapists.data.pe.PESection;
import net.jselby.escapists.game.Application;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.events.FunctionRegister;
import net.jselby.escapists.util.ByteReader;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The primary Escapists runtime.
 *
 * @author j_selby
 */
public class EscapistsRuntime {
    public static final String VERSION = "0.1";
    public static boolean DEBUG = false;
    private static final boolean UNPACK_FILES = false;

    private static final byte[] UNICODE_GAME_HEADER = "PAMU".getBytes();
    private static final byte[] PACK_HEADER = new byte[]{119, 119, 119, 119, 73, -121, 71, 18};
    private static final String[] UNCOMPRESSED_PACKED_FILES = {"mmfs2.dll"};
    private static EscapistsRuntime runtime;

    private Application application;
    private FunctionRegister register = new FunctionRegister();
    private File escapistsDirectory;

    public static EscapistsRuntime getRuntime() {
        return runtime;
    }

    public boolean start(EscapistsGame game) throws IOException {
        System.out.printf("Escapists Runtime, v%s by jselby.\n", VERSION);
        EscapistsRuntime.runtime = this;

        // Alright, lets get cracking!
        // Read into a ByteBuffer
        // TODO: Detect Steam installations, and use their Escapists build.
        game.setLoadingMessage("Loading game executable...");

        // Check for internal storage/SD card
        if (!Gdx.files.isExternalStorageAvailable()) {
            game.fatalPrompt("There is no SD card inserted.");
            return false;
        }

        // Locate the game directory, in Steam/storage root.
        escapistsDirectory = new File(Gdx.files.getExternalStoragePath(), "The Escapists");
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Desktop) {
            escapistsDirectory = game.getPlatformUtils().findGameFolder();
            if (escapistsDirectory == null) {
                return false;
            }
        }

        System.out.println("Escapists directory: " + escapistsDirectory);

        if (!escapistsDirectory.exists()) {
            game.fatalPrompt("Game does not exist in " +
                    (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android
                            ? "SD card/internal storage." : "same directory as application."));
            return false;
        }

        if (!new File(escapistsDirectory, "TheEscapists_eur.exe").exists()) {
            game.fatalPrompt("Failed to find game's executable within game folder.");
            return false;
        }

        // Check for debugging
        if (new File(escapistsDirectory, "debug.flag").exists()) {
            System.out.println("Debugging ENABLED.");
            DEBUG = true;
        }

        File escapistsFile = new File(escapistsDirectory, "TheEscapists_eur.exe");

        FileInputStream fileIn = new FileInputStream(escapistsFile);
        MappedByteBuffer bufIn = (MappedByteBuffer) fileIn.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,
                escapistsFile.length()).order(ByteOrder.LITTLE_ENDIAN);
        System.out.println("JVM decided to cache in memory: " + bufIn.isLoaded());

        //ByteBuffer bufIn = ByteBuffer.wrap(IOUtils.toByteArray(fileIn)).order(ByteOrder.LITTLE_ENDIAN);
        //fileIn.close();
        //bufIn.rewind();
        ByteReader buf = new ByteReader(bufIn);

        game.setLoadingMessage("Parsing file structure...");
        // Parse the Windows Portable Executable file
        PEFile file = new PEFile(buf);

        // Find the last section, and the content after it
        PESection[] peSections = file.getSections();
        if (peSections.length != 5) {
            System.out.println("Invalid PE file section count. Is this the correct Escapists file (TheEscapists_eur.exe)?");
            game.fatalPrompt("Game failed validation check (PE header count). Is the correct Escapists file (TheEscapists_eur.exe)?");
        }
        PESection lastPeSection = peSections[peSections.length - 1];
        int afterSectionPointer = lastPeSection.getSectionPointer() + lastPeSection.getSectionSize();
        buf.setPosition(afterSectionPointer);

        // -- PACK READING
        // Verify header
        byte[] packHeaderMagic = buf.getBytes(8);
        if (!Arrays.equals(packHeaderMagic, PACK_HEADER)) {
            System.out.println("Invalid pack header. Is this the correct Escapists file (TheEscapists_eur.exe)?");
            game.fatalPrompt("Game failed validation check (PACK_HEADER). Is the correct Escapists file (TheEscapists_eur.exe)?");
            return false;
        }

        // Read pack
        if (buf.getInt() != 32) { // Pack header size
            System.out.println("Bad pack header size. Is this the correct Escapists file (TheEscapists_eur.exe)?");
            game.fatalPrompt("Game failed validation check (PACK_HEADER_SIZE). Is the correct Escapists file (TheEscapists_eur.exe)?");
            return false;
        }

        System.out.println("Game header and size validated.");

        buf.skipBytes(4);
        // Pack metadata
        int formatVersion = buf.getInt();
        if (buf.getInt() != 0 || buf.getInt() != 0) {
            System.out.println("Bad pack header padding.");
            game.fatalPrompt("Game failed validation check (PACK_HEADER_PADDING). Is the correct Escapists file (TheEscapists_eur.exe)?");
            return false;
        }
        int packCount = buf.getInt();

        if (UNPACK_FILES) {
            Inflater inflater = new Inflater();

            for (int i = 0; i < packCount; i++) {

                int packedFileNameLength = buf.getShort();
                StringBuilder builder = new StringBuilder();
                for (int ii = 0; ii < packedFileNameLength; ii++) {
                    builder.append(buf.getChar());
                }
                String fileName = builder.toString().trim();

                buf.skipBytes(4); // Magic

                int packedFileCompLength = buf.getInt();

                byte[] data = buf.getBytes(packedFileCompLength);

                boolean uncompressed = false;
                for (String uncPack : UNCOMPRESSED_PACKED_FILES) {
                    if (uncPack.equals(fileName)) {
                        uncompressed = true;
                        break;
                    }
                }

                if (!uncompressed) {
                    try {
                        byte[] decompData = new byte[packedFileCompLength * 16];
                        inflater.reset();
                        inflater.setInput(data);
                        int len = inflater.inflate(decompData);
                        byte[] arrayCopy = new byte[len];
                        System.arraycopy(decompData, 0, arrayCopy, 0, len);
                        data = arrayCopy;
                    } catch (DataFormatException e) {
                        System.out.printf("Failed to inflate packed file %s: %s.\n", fileName, e.getMessage());
                    }
                }

                //System.out.printf("Discovered packed file %s (compressed: %b).\n", fileName, !uncompressed);
            }
        } else {
            for (int i = 0; i < packCount; i++) {
                buf.skipBytes(buf.getShort() * 2 + 4);
                buf.skipBytes(buf.getInt());
            }
        }

        // -- GAME DATA READING
        byte[] gameDataHeaderMagic = buf.getBytes(4);
        if (!Arrays.equals(gameDataHeaderMagic, UNICODE_GAME_HEADER)) {
            System.out.println("Bad game header.");
            game.fatalPrompt("Game failed validation check (GAME_HEADER). Is the correct Escapists file (TheEscapists_eur.exe)?");
            return false;
        }

        int runtimeVersion = buf.getShort();
        if (runtimeVersion != 0x0302) {
            System.out.printf("Unknown runtime version \"%d\", has the game been updated?\n", runtimeVersion);
        }
        int runtimeSubversion = buf.getShort();
        int productVersion = buf.getInt();
        int productBuild = buf.getInt();

        //System.out.printf("Game version %d, build %d.\n", productVersion, productBuild);

        // Chunk reading
        game.setLoadingMessage("Parsing chunks...");
        List<Chunk> chunks = ChunkDecoder.decodeChunk(buf, game);

        game.setLoadingMessage("Preparing assets...");
        application = new Application(this, chunks);

        File saveLocation = new File(game.getPlatformUtils().getSaveLocation(),
                "The Escapists" + File.separator + "mods");
        if (!saveLocation.exists() && !saveLocation.mkdirs()) {
            game.fatalPrompt("Failed to write to storage.");
            return false;
        }
        for (File child : saveLocation.listFiles()) {
            if (child.getName().toLowerCase().endsWith(".mod")) {
                // Load mod in
                game.addMod(child.getName(), IOUtils.toString(child.toURI()));
            }
        }

        System.out.println("Chunk parse completed successfully.");

        return true;
    }

    public Application getApplication() {
        return application;
    }

    public File getGamePath() {
        return escapistsDirectory;
    }

    public FunctionRegister getRegister() {
        return register;
    }
}
