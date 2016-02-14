package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.FunctionCollection;
import net.jselby.escapists.util.BlowfishCompatEncryption;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Encryption utilities handle blowfish functionality.
 */
public class EncryptionUtils extends FunctionCollection {
    @Action(subId = 46, id = 80)
    public void initBlowfishEncryption(String key) {
        // TODO: Runtime decryption
    }

    @Action(subId = 46, id = 88)
    public void decryptFile(String key, String fileName) {
        System.out.println("In-place decrypting file " + fileName + " using key \"" + key + "\".");
        try {
            byte[] data = BlowfishCompatEncryption.decryptBytes(
                    IOUtils.toByteArray(new File(fileName).toURI()), key.getBytes());
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action(subId = 46, id = 87)
    public void encryptFile(String key, String fileName) {
        System.out.println("In-place encrypting file " + fileName + " using key \"" + key + "\".");
        try {
            byte[] data = BlowfishCompatEncryption.encryptBytes(
                    IOUtils.toByteArray(new File(fileName).toURI()), key.getBytes());
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
