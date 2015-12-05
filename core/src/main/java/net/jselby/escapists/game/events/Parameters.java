package net.jselby.escapists.game.events;

import java.io.File;

/**
 * Implementations of parameter functions that can be utilised by the scripting engine at runtime.
 */
public class Parameters {
    protected Scope scope;

    public String CurrentText() { // TODO: Find out what this means. Environmental variable?
        return "";
    }

    public String GetDataDirectory() {
        return scope.getGame().getPlatformUtils().getSaveLocation().toString();
    }

    public String ApplicationDrive() {
        String absPath = scope.getGame().getPlatformUtils().getSaveLocation().getAbsolutePath();
        for (File file : File.listRoots()) {
            if (absPath.startsWith(file.getAbsolutePath())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    public String ApplicationDirectory() {
        return scope.getGame().getPlatformUtils()
                .getSaveLocation().getAbsolutePath().substring(ApplicationDrive().length());
    }
}
