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

    protected PESection(String sectionName, int virtualSizeRVA, int virtualAddressRVA, int sectionSize,
                     int sectionPointer) {
        this.sectionName = sectionName;
        this.virtualAddressRVA = virtualAddressRVA;
        this.virtualSizeRVA = virtualSizeRVA;
        this.sectionSize = sectionSize;
        this.sectionPointer = sectionPointer;
    }

    /**
     * Returns the name of this section (without additional whitespace).
     * @return The section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Returns the virtual size for this section (RVA).
     * @return A integer
     */
    public int getVirtualSizeRVA() {
        return virtualSizeRVA;
    }

    /**
     * Returns the virtual address for this section (RVA).
     * @return A integer
     */
    public int getVirtualAddressRVA() {
        return virtualAddressRVA;
    }

    /**
     * Returns the section size for this section.
     * @return A integer
     */
    public int getSectionSize() {
        return sectionSize;
    }

    /**
     * Returns the section pointer for this section.
     * @return A integer
     */
    public int getSectionPointer() {
        return sectionPointer;
    }

    @Override
    public String toString() {
        return "PESection={name=\"" + getSectionName() + "\", virtualSize=\"" + virtualSizeRVA
                + "\", virtualAddress=\"" + virtualAddressRVA + "\", sectionSize=\"" + sectionSize
                + "\", sectionPointer=\"" + sectionPointer + "\"}";
    }
}
