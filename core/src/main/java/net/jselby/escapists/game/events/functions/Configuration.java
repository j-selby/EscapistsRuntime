package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ini.PropertiesFile;
import net.jselby.escapists.data.ini.PropertiesSection;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration functions store section-key-value pairs in a INI format on disk.
 */
public class Configuration extends FunctionCollection {
    @Action(subId = 63, id = 86)
    public void LoadIniFile(String path) {
        ObjectInstance[] objects = scope.getObjects();

        PropertiesFile propertiesFile;
        if (!EscapistsRuntime.getRuntime().getApplication().properties.containsKey(path)) {
            File file = new File(translateFilePath(path));
            String contents;
            if (!file.exists()) {
                System.err.println("Failed to open file \"" + file + "\", as it doesn't exist.");
                EscapistsRuntime.getRuntime().getApplication().properties.put(path, new PropertiesFile());
                for (ObjectInstance instance : objects) {
                    instance.setLoadedFile(path);
                }
                return;
            }
            try {
                contents = IOUtils.toString(file.toURI());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            System.out.println("Caching file " + path + " in memory.");
            propertiesFile = new PropertiesFile(contents);
            EscapistsRuntime.getRuntime().getApplication().properties.put(path, propertiesFile);
        } else {
            propertiesFile = EscapistsRuntime.getRuntime().getApplication().properties.get(path);
        }

        for (ObjectInstance instance : objects) {
            instance.setLoadedFile(path);
        }

        for (ObjectInstance object : objects) {
            for (Map.Entry<String, PropertiesSection> section : propertiesFile.entrySet()) {
                for (Map.Entry<String, Object> item : section.getValue().entrySet()) {
                    object.getVariables().put(section.getKey() + ":" + item.getKey(), item.getValue());
                }
            }
        }
    }

    @Action(subId = 47, id = 158)
    public void loadIniFileAndClear(String path, int unknown) {
        for (ObjectInstance instance : scope.peekAtObjects()) {
            for (Object var : instance.getVariables().entrySet().toArray()) {
                Map.Entry<String, Object> pair = (Map.Entry<String, Object>) var;
                if (pair.getKey().contains(":")) {
                    instance.getVariables().remove(pair.getKey());
                }
            }
        }

        LoadIniFile(path);
    }

    /**
     * Translates file paths for this platform.
     * @param path The path to translate
     * @return A translated path
     */
    private String translateFilePath(String path) {
        return path.replace("/", "\\").replace("\\", File.separator);
    }

    @Action(subId = 63, id = 90)
    public void SetItemValue(String section, String key, String value) {
        ObjectInstance[] instances = scope.getObjects();
        if (instances.length == 0) {
            return;
        }
        for (ObjectInstance instance : instances) {
            instance.getVariables().put(section + ":" + key, value);
        }

        ObjectInstance instance = instances[0];
        System.out.println("SetItemValue: " + section + ":" + key + " = " + value + " @ " + instance.getLoadedFile());

        HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
        for (Map.Entry<String, Object> var : instance.getVariables().entrySet()) {
            if (var.getKey().contains(":")) {
                String varSection = var.getKey().split(":")[0];
                String varKey = var.getKey().split(":")[1];
                Object varValue = var.getValue();

                HashMap<String, Object> sectionVars;

                if (map.containsKey(varSection)) {
                    sectionVars = map.get(varSection);
                } else {
                    sectionVars = new HashMap<String, Object>();
                    map.put(varSection, sectionVars);
                }

                sectionVars.put(varKey, varValue);
            }
        }

        // Convert to INI format
        String contents = "";
        for (Map.Entry<String, HashMap<String, Object>> sectionPair : map.entrySet()) {
            contents += "[" + sectionPair.getKey() + "]\r\n";
            for (Map.Entry<String, Object> content : sectionPair.getValue().entrySet()) {
                contents += content.getKey() + "=" + content.getValue().toString()
                        .replace("\r", "\\r").replace("\n", "\\n") + "\r\n";
            }
        }


        // Finally, write it out
        try {
            FileOutputStream out = new FileOutputStream(instance.getLoadedFile());
            out.write(contents.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Condition(subId = 47, id = -85)
    public boolean HasItemValue(String key, String value) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            return instance.getVariables().containsKey(key + ":" + value);
        }

        return false;
    }
}
