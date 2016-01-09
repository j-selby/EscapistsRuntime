package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Actions;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

import java.io.File;

/**
 * FileIO functions handle IO with the disk.
 */
public class FileIO extends FunctionCollection {
    @Condition(subId = 42, id = -86)
    public boolean DirectoryExists(String name) {
        return new File(name).exists();
    }

    @Condition(subId = 42, id = -83)
    public boolean isFileReadable(String name) {
        return new File(name).exists();
    }

    @Actions({
            @Action(subId = 42, id = 82),
            @Action(subId = 66, id = 95),
            @Action(subId = 13, id = 82)
    })
    public boolean CreateDirectory(String name) {
        return new File(name).mkdir();
    }

    @Action(subId = 42, id = 85)
    public boolean DeleteFile(String name) {
        return new File(name).delete();
    }

    @Action(subId = 65, id = 80)
    public void EmbedFont(String path) {
        // Fonts are loaded by the runtime at launch, so we are gonna ignore this.
    }
}
