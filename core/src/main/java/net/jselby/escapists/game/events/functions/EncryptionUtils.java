package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Encryption utilities handle blowfish functionality.
 */
public class EncryptionUtils extends FunctionCollection {
    @Action(subId = 46, id = 80)
    public void initBlowfishEncryption(String key) {
        // TODO: Runtime decryption
    }

}
