package net.jselby.escapists.data.pe;

/**
 * The machine type that a PE executable is compiled for.
 */
public enum MachineType {
    i386(0x014c),
    amd64(0x8664);

    private int id;

    MachineType(int id) {
        this.id = id;
    }

    public static MachineType getMachineTypeByID(int id) {
        for (MachineType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
