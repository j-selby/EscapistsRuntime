package net.jselby.escapists.data.pe;

/**
 * A section is a representation of a PE section.
 *
 * @author j_selby
 */
public class PESection {
    private final String sectionName;
    private final int virtualSizeRVA;
    private final int virtualAddressRVA;
    private final int sectionSize;
    private final int sectionPointer;

    public PESection(String sectionName, int virtualSizeRVA, int virtualAddressRVA, int sectionSize,
                     int sectionPointer) {
        this.sectionName = sectionName;
        this.virtualAddressRVA = virtualAddressRVA;
        this.virtualSizeRVA = virtualSizeRVA;
        this.sectionSize = sectionSize;
        this.sectionPointer = sectionPointer;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getVirtualSizeRVA() {
        return virtualSizeRVA;
    }

    public int getVirtualAddressRVA() {
        return virtualAddressRVA;
    }

    public int getSectionSize() {
        return sectionSize;
    }

    public int getSectionPointer() {
        return sectionPointer;
    }
}
