package net.jselby.escapists.game.events;

import net.jselby.escapists.EscapistsRuntime;

import java.io.File;

/**
 * Implementations of parameter functions that can be utilised by the scripting engine at runtime.
 */
public class Parameters {
    protected Scope scope;

    public String CurrentText() { // TODO: Find out what this means. Environmental variable?
        return "";
    }

    public int XMouse() {
        return scope.getGame().getMouseX();
    }

    public int YMouse() {
        return scope.getGame().getMouseY();
    }

    public String SteamAccountUserName() {
        return "Escapists Runtime User";
    }

    public int SteamAccountUserId() {
        return 0;
    }

    public String GetDataDirectory() {
        return scope.getGame().getPlatformUtils().getSaveLocation().toString();
    }

    public String ApplicationDrive() {
        String absPath = EscapistsRuntime.getRuntime().getGamePath().getAbsolutePath();
        for (File file : File.listRoots()) {
            if (absPath.startsWith(file.getAbsolutePath())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    public String ApplicationDirectory() {
        return EscapistsRuntime.getRuntime().getGamePath()
                .getAbsolutePath().substring(ApplicationDrive().length()) + File.separator;
    }
}
