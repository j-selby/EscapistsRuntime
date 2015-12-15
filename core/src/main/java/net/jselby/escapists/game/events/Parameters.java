package net.jselby.escapists.game.events;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.objects.Text;

import java.io.File;

/**
 * Implementations of parameter functions that can be utilised by the scripting engine at runtime.
 */
public class Parameters {
    protected Scope scope;

    public String CurrentText(int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id && instance instanceof Text) {
                String str = (String) ((Text) instance).getString();
                // Filter for Chowdren flags
                if (str.startsWith("Chowdren:")) {
                    String flagName = str.substring(str.indexOf(":") + 1).trim();
                    if (flagName.equalsIgnoreCase("Platform")) {
                        str = "jselby's runtime on " + System.getProperty("os.name");
                    } else {
                        System.out.printf("Unknown Chowdren flag: %s.\n", flagName);
                    }
                }
                return str;
            }
        }
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

    public float GetY(int extra, int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == extra) {
                return instance.getY();
            }
        }
        return 0;
    }

    public String UpperString(int id, String str) {
        return str.toUpperCase();
    }

    public String LowerString(int id, String str) {
        return str.toLowerCase();
    }

    public Number GlobalValue(int key) {
        return scope.getGame().globalInts.get(key);
    }

    public String GlobalString(int key) {
        String val = scope.getGame().globalStrings.get(key);
        if (key == 1) { // Version String
            val += " (UER)";
        } else if (key == 6) { // Inject Language
            return "eng";
        }
        return val;
    }

    public Object GetValue(int objectId, String varName) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == objectId) {
                if (instance.getVariables().containsKey(varName)) {
                    //System.out.printf("GetValue:%d=%s.\n", objectId, instance.getVariables().get(varName));
                    return instance.getVariables().get(varName);
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    public String GetItemString(int id, String a, String b, String c) {
        // TODO: Item properties
        return "";
    }

    public String GroupItemString(int id, String category, String key) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                if (instance.getVariables().containsKey(category + ":" + key)) {
                    System.out.printf("GroupItemString;%d;%s=%s\n", id, category + ":" + key, instance.getVariables().get(category + ":" + key));
                    return (String) instance.getVariables().get(category + ":" + key);
                }
            }
        }
        return "";
    }

    public String Element(int id, int element) {
        // TODO: Item lists
        return "";
    }

    public int ListLength(int id) {
        // TODO: Item lists
        return 0;
    }

    public String Select(int id) {
        // TODO: Item lists
        return "";
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
