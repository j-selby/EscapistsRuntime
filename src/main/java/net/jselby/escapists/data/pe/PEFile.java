package net.jselby.escapists.data.pe;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The PE file reads Windows executables, and reads important data from them, such as sections, etc.
 *
 * @author j_selby
 */
public class PEFile {
    private static byte[] EXE_HEADER = new byte[]{'M', 'Z'};

    private byte[] peSignature = new byte[4];
    private int machineType;
    private int peCharacteristics;
    private int bitCount;
    private PESection[] sections;

    public PEFile(ByteBuffer buf) throws InvalidFileException {
        // Firstly, check that this file matches the default Windows executable magic, 'MZ'
        int startPos = buf.position();

        byte[] exeHeader = new byte[2];
        buf.get(exeHeader);
        if (!Arrays.equals(exeHeader, EXE_HEADER)) {
            // Failed this check
            throw new InvalidFileException("EXE header not found.");
        }

        // Secondly, read the header offset
        buf.position(0x30 + 12);
        int peHeaderOffset = buf.getInt();

        // Grab data from the PE header
        buf.position(peHeaderOffset);
        buf.get(peSignature);
        machineType = buf.getShort();
        int sectionCount = buf.getShort();

        buf.position(buf.position() + 14);
        peCharacteristics = buf.getShort();

        // Grab data from the optional header
        bitCount = buf.getShort();
        buf.position(buf.position() + 134);

        // Read sections
        sections = new PESection[sectionCount];
        for (int i = 0; i < sectionCount; i++) {
            byte[] nameBytes = new byte[8];
            String sectionName = new String(nameBytes).trim();
            buf.get(nameBytes);

            // Read virtual sizes
            int virtualSizeRVA = buf.getInt();
            int virtualAddressRVA = buf.getInt();

            // Read physical sizes
            buf.position(buf.position() + 8);

            int sectionSize = buf.getInt();
            int sectionPointer = buf.getInt();

            sections[i] = new PESection(sectionName, virtualSizeRVA,
                    virtualAddressRVA, sectionSize, sectionPointer);
        }

        buf.position(startPos);
    }

    /**
     * Get definitions for which
     * @return
     */
    public PESection[] getSections() {
        return sections;
    }
}
