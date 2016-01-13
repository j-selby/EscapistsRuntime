package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Expression;
import net.jselby.escapists.game.events.FunctionCollection;
import net.jselby.escapists.game.objects.Text;

import java.io.File;

/**
 * Implementations of parameter functions that can be utilised by the scripting engine at runtime.
 */
public class Expressions extends FunctionCollection {
    @Expression(subId = 3, id = 81)
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

    @Expression(subId = -6, id = 0)
    public int XMouse() {
        return scope.getGame().getMouseX();
    }

    @Expression(subId = -6, id = 1)
    public int YMouse() {
        return scope.getGame().getMouseY();
    }

    @Expression(subId = 39, id = 82)
    public String SteamAccountUserName() {
        return "Escapists Runtime User";
    }

    @Expression(subId = 39, id = 81)
    public int SteamAccountUserId() {
        return 0;
    }

    @Expression(subId = 2, id = 1)
    public float GetY(int extra, int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == extra) {
                return instance.getY();
            }
        }
        return 0;
    }

    @Expression(subId = -1, id = 52)
    public String UpperString(int id, String str) {
        return str.toUpperCase();
    }

    @Expression(subId = -1, id = 51)
    public String LowerString(int id, String str) {
        return str.toLowerCase();
    }

    @Expression(subId = -1, id = 5)
    public Number ToNumber(int id, String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return Double.parseDouble(str);
    }

    @Expression(subId = -1, id = 24)
    public Number GlobalValue(int key) {
        return scope.getGame().globalInts.get(key);
    }

    @Expression(subId = -1, id = 50)
    public String GlobalString(int key) {
        return scope.getGame().globalStrings.get(key);
    }

    @Expression(subId = 36, id = 80)
    public Object GetValue(int objectId, String varName) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == objectId) {
                if (instance.getVariables().containsKey(varName)) {
                    return instance.getVariables().get(varName);
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    @Expression(subId = 36, id = 81)
    public String GetString(int objectId, String varName) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == objectId) {
                if (instance.getVariables().containsKey(varName)) {
                    return (String) instance.getVariables().get(varName);
                } else {
                    return "";
                }
            }
        }
        return "";
    }

    @Expression(subId = 47, id = 89)
    public String GetItemString(int id, String section, String key, String defaultVal) {
        String varName = section + ":" + key;
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                if (instance.getVariables().containsKey(varName)) {
                    return (String) instance.getVariables().get(varName);
                } else {
                    return defaultVal;
                }
            }
        }

        return defaultVal;
    }

    @Expression(subId = 63, id = 85)
    public String GroupItemString(int id, String category, String key) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                if (instance.getVariables().containsKey(category + ":" + key)) {
                    return (String) instance.getVariables().get(category + ":" + key);
                }
            }
        }
        return "";
    }

    @Expression(subId = 34, id = 81)
    public String Element(int id, int element) {
        if (element < 0) {
            throw new IllegalArgumentException("Array cannot be negatively indexed!");
        }
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                if (instance.getListElements().size() > element) {
                    return instance.getListElements().get(element);
                } else {
                    return "";
                }
            }
        }
        return "";
    }

    @Expression(subId = 32, id = 87)
    public int ListLength(int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                return instance.getListElements().size();
            }
        }
        return 0;
    }

    @Expression(subId = 32, id = 81)
    public String Select(int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                if (instance.getListElements().size() < instance.getSelectedLine()) {
                    return "";
                }
                return instance.getListElements().get(instance.getSelectedLine() - 1);
            }
        }

        return "";
    }

    @Expression(subId = 32, id = 80)
    public int SelectedLine(int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                return instance.getSelectedLine();
            }
        }

        return 0;
    }

    @Expression(subId = 42, id = 107)
    public String GetDataDirectory() {
        return scope.getGame().getPlatformUtils().getSaveLocation().toString();
    }

    @Expression(subId = -1, id = 6)
    public String ApplicationDrive() {
        String absPath = EscapistsRuntime.getRuntime().getGamePath().getAbsolutePath();
        for (File file : File.listRoots()) {
            if (absPath.startsWith(file.getAbsolutePath())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    @Expression(subId = -1, id = 7)
    public String ApplicationDirectory() {
        return EscapistsRuntime.getRuntime().getGamePath()
                .getAbsolutePath().substring(ApplicationDrive().length()) + File.separator;
    }

    @Expression(subId = -1, id = 4)
    public String ToString(int id, String str) {
        return str; // Automatically done
    }


    @Expression(subId = -1, id = 46)
    public int LoopIndex(int int1, String loopName) {
        return scope.getScene().getActiveLoops().get(loopName);
    }

    @Expression(subId = 38, id = 84)
    public int UnknownX() {
        return 0;
    }

    @Expression(subId = 38, id = 85)
    public int UnknownY() {
        return 0;
    }

    @Expression(subId = 50, id = 80)
    public String GetValueX() {
        return "";
    }

    @Expression(subId = 58, id = 80)
    public int GetValueY() {
        return 0;
    }

    @Expression(subId = 2, id = 7)
    public int GetObjectXLeft(int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                return (int) instance.getX();
            }
        }
        return 0;
    }

    @Expression(subId = 52, id = 80)
    public String HTTPContent(int id) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == id) {
                return (String) instance.getVariables().get("_env_gethttp");
            }
        }

        return "";
    }

    @Expression(subId = -1, id = 22)
    public int StringLength(int id, String content) {
        return content.length();
    }
}
