package net.jselby.escapists;

import com.google.common.io.Files;
import net.jselby.escapists.data.pe.PEFile;
import net.jselby.escapists.data.pe.PESection;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * The primary Escapists runtime.
 *
 * @author j_selby
 */
public class EscapistsRuntime {
    private static byte[] PACK_HEADER = new byte[]{119, 119, 119, 119, 73, -121, 71, 18};

    public static void main(String[] args) throws IOException {
        ByteBuffer buf = Files.map(new File("TheEscapists_eur.exe")).order(ByteOrder.LITTLE_ENDIAN);
        PEFile file = new PEFile(buf);

        // Find the last section, and the content after it
        PESection[] peSections = file.getSections();
        System.out.printf("Found %d sections in PE file.\n", peSections.length);
        PESection lastPeSection = peSections[peSections.length - 1];
        int afterSectionPointer = lastPeSection.getSectionPointer() + lastPeSection.getSectionSize();
        buf.position(afterSectionPointer);

        // -- PACK READING
        // Verify header
        byte[] packHeaderMagic = new byte[8];
        buf.get(packHeaderMagic);
        if (!Arrays.equals(packHeaderMagic, PACK_HEADER)) {
            System.out.println("Invalid pack header.");
            return;
        }

        // Read pack
        int packStart = buf.position() - 8;
        if (buf.getInt() != 32) { // Pack header size
            System.out.println("Bad pack header size.");
            return;
        }

        buf.position(packStart + 16);
        // Pack metadata
        int formatVersion = buf.getInt();
        if (buf.getInt() != 0 || buf.getInt() != 0) {
            System.out.println("Bad pack header Padding.");
            return;
        }
        int packCount = buf.getInt();

        System.out.printf("Pack format version: %d.\n", formatVersion);

    }

    public static void dumpBytes(ByteBuffer buf, int length) {
        for (int i = 1; i <= length; i++) {
            int num = buf.get();
            System.out.printf("%s(%d) ", Integer.toHexString(num), num);
            if (i % 8 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
