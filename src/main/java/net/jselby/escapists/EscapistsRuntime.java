package net.jselby.escapists;

import com.google.common.io.Files;
import net.jselby.escapists.data.pe.PEFile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The primary Escapists runtime.
 *
 * @author j_selby
 */
public class EscapistsRuntime {
    public static void main(String[] args) throws IOException {
        ByteBuffer buf = Files.map(new File("TheEscapists_eur.exe")).order(ByteOrder.LITTLE_ENDIAN);
        PEFile file = new PEFile(buf);
    }
}
