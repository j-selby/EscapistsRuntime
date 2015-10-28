package net.jselby.escapists;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ChunkDecoder;
import net.jselby.escapists.data.pe.PEFile;
import net.jselby.escapists.data.pe.PESection;
import net.jselby.escapists.game.Application;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.util.ByteReader;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    private static final String VERSION = "0.1";

    private static final byte[] UNICODE_GAME_HEADER = "PAMU".getBytes();
    private static final byte[] PACK_HEADER = new byte[]{119, 119, 119, 119, 73, -121, 71, 18};
    private static final String[] UNCOMPRESSED_PACKED_FILES = {"mmfs2.dll"};
    private static EscapistsRuntime runtime;

    private EscapistsGame game;
    private Application application;

    public static EscapistsRuntime getRuntime() {
        return runtime;
    }

    public void start() throws IOException {
        EscapistsRuntime.runtime = this;

        // Alright, lets get cracking!
        // Read into a ByteBuffer
        // TODO: Detect Steam installations, and use their Escapists build.
        InputStream fileIn = getClass().getResourceAsStream("/assets/TheEscapists_eur.exe");
        if (fileIn == null) {
            System.out.println(new File("TheEscapists_eur.exe").getAbsolutePath());
            if (!new File("TheEscapists_eur.exe").exists()) {
                System.err.println("Panicing, couldn't find Escapists in application archive.");
                throw new IllegalStateException("Null resource");
            }

            fileIn = new FileInputStream("TheEscapists_eur.exe");
        }

        ByteBuffer bufIn = ByteBuffer.wrap(IOUtils.toByteArray(fileIn)).order(ByteOrder.LITTLE_ENDIAN);
        fileIn.close();
        bufIn.rewind();
        ByteReader buf = new ByteReader(bufIn);

        // Parse the Windows Portable Executable file
        PEFile file = new PEFile(buf);

        // Find the last section, and the content after it
        PESection[] peSections = file.getSections();
        if (peSections.length != 5) {
            System.out.println("Invalid PE file section count. Is this the correct Escapists file (TheEscapists_eur.exe)?");
        }
        PESection lastPeSection = peSections[peSections.length - 1];
        int afterSectionPointer = lastPeSection.getSectionPointer() + lastPeSection.getSectionSize();
        buf.position(afterSectionPointer);

        Inflater inflater = new Inflater();
        // -- PACK READING
        // Verify header
        byte[] packHeaderMagic = buf.getBytes(8);
        if (!Arrays.equals(packHeaderMagic, PACK_HEADER)) {
            System.out.println("Invalid pack header. Is this the correct Escapists file (TheEscapists_eur.exe)?");
            return;
        }

        // Read pack
        int packStart = buf.position() - 8;
        if (buf.getInt() != 32) { // Pack header size
            System.out.println("Bad pack header size. Is this the correct Escapists file (TheEscapists_eur.exe)?");
            return;
        }

        System.out.println("Game header and size validated.");

        buf.position(packStart + 16);
        // Pack metadata
        int formatVersion = buf.getInt();
        if (buf.getInt() != 0 || buf.getInt() != 0) {
            System.out.println("Bad pack header padding.");
            return;
        }
        int packCount = buf.getInt();

        for (int i = 0; i < packCount; i++) {

            int packedFileNameLength = buf.getShort();
            StringBuilder builder = new StringBuilder();
            for (int ii = 0; ii < packedFileNameLength; ii++) {
                builder.append(buf.getChar());
            }
            String fileName = builder.toString().trim();

            buf.getInt(); // Magic

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

            //System.out.printf("Discovered packed file %s of length %d (compressed: %d).\n", fileName, len, !uncompressed);
        }

        // -- GAME DATA READING
        byte[] gameDataHeaderMagic = buf.getBytes(4);
        if (!Arrays.equals(gameDataHeaderMagic, UNICODE_GAME_HEADER)) {
            System.out.println("Bad game header.");
            return;
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
        List<Chunk> chunks = ChunkDecoder.decodeChunk(buf);
        application = new Application(this, chunks);

        System.out.println("Chunk parse done");
    }

    public Application getApplication() {
        return application;
    }
}
