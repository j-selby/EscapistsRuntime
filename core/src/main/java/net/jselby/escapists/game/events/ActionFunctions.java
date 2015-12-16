package net.jselby.escapists.game.events;

import com.badlogic.gdx.Gdx;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ini.PropertiesFile;
import net.jselby.escapists.data.ini.PropertiesSection;
import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.objects.Text;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Events are things that the engine can do to change the execution state.
 */
public class ActionFunctions extends ConditionFunctions {
    @Action(subId = -1, id = 0)
    public void Skip() {
    }

    @Action(subId = -3, id = 0)
    public void NextFrame() {
        scope.getGame().loadScene(scope.getGame().getSceneIndex() + 1);
    }

    @Action(subId = -3, id = 2)
    public void JumpToFrame(int scene) {
        scope.getGame().loadScene(scene);
    }

    @Action(subId = -1, id = 6)
    public void ActivateGroup(int id) {
        scope.getScene().getActiveGroups().put(id, true);
    }

    @Action(subId = -1, id = 7)
    public void DeactivateGroup(int id) {
        scope.getScene().getActiveGroups().put(id, false);
    }

    @Action(subId = -1, id = 14)
    public void StartLoop(String name, int times) {
        scope.getScene().getActiveLoops().put(name, times);
    }

    @Action(subId = -3, id = 4)
    public void EndApplication() {
        // RIP
        scope.getGame().exit();
    }

    @Action(subId = 2, id = 35)
    public void FlagOn(int value) {
        String key = "_env_flag_" + value;
        scope.getScene().getVariables().put(key, true);
    }

    @Action(subId = 2, id = 36)
    public void FlagOff(int value) {
        String key = "_env_flag_" + value;
        scope.getScene().getVariables().put(key, false);
    }

    @Action(subId = 2, id = 2)
    public void SetX(int newX) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setX(newX);
        }
    }

    @Action(subId = 2, id = 3)
    public void SetY(int newY) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setY(newY);
        }
    }

    @Action(subId = 2, id = 57)
    public void BringToFront() {
        for (ObjectInstance object : scope.getObjects()) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            Layer[] layers = scope.getScene().getLayers();
            layers[layers.length - 1].objects.add(object);
        }
    }

    @Action(subId = 3, id = 26)
    public void Disappear() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setVisible(false);
        }
    }

    @Action(subId = 3, id = 27)
    public void Reappear() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setVisible(true);
        }
    }

    @Action(subId = 2, id = 23)
    public void SetDirection(int newDir) {
        // TODO: Better Animation implementation that supports this
    }

    @Action(subId = 2, id = 17)
    public void SetAnimation(int newAnimation) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setAnimation(newAnimation);
        }
    }

    @Action(subId = 2, id = 15)
    public void StopAnimation() {
        // TODO: Better Animation implementation
    }

    @Action(subId = 3, id = 83)
    public void SetColor(int value) {
        // TODO: Set color
    }

    @Action(subId = 2, id = 65)
    public void SetAlphaCoefficient(int value) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setImageAlpha(256 - value);
        }
    }

    @Action(subId = 3, id = 88)
    public void SetString(String value) {
        for (ObjectInstance object : scope.getObjects()) {
            if (object instanceof Text) {
                ((Text) object).setString(value);
            }
        }
    }

    @Action(subId = 36, id = 91)
    public void ClearObjectVarArray() {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().clear();
        }
    }

    @Action(subId = 36, id = 80)
    public void SetIntegerVar(String name, int val) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, val);
        }
    }

    @Action(subId = 36, id = 81)
    public void AddIntegerVar(String name, int by) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, (Integer) object.getVariables().get(name) + by);
        }
    }

    @Action(subId = 36, id = 82)
    public void SubtractIntegerVar(String name, int by) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, (Integer) object.getVariables().get(name) - by);
        }
    }

    @Action(subId = 36, id = 88)
    public void SetStringVar(String name, String val) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, val);
        }
    }

    @Action(subId = 63, id = 86)
    public void LoadIniFile(String path) {
        File file = new File(translateFilePath(path));
        String contents;
        try {
            contents = IOUtils.toString(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PropertiesFile propertiesFile = new PropertiesFile(contents);

        for (ObjectInstance object : scope.getObjects()) {
            for (Map.Entry<String, PropertiesSection> section : propertiesFile.entrySet()) {
                for (Map.Entry<String, Object> item : section.getValue().entrySet()) {
                    object.getVariables().put(section.getKey() + ":" + item.getKey(), item.getValue());
                }
            }
        }
    }

    @Action(subId = 47, id = 158)
    public void loadIniFileAndClear(String path, int unknown) {
        File file = new File(translateFilePath(path));
        String contents;
        try {
            contents = IOUtils.toString(file.toURI());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PropertiesFile propertiesFile = new PropertiesFile(contents);

        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().clear();
            for (Map.Entry<String, PropertiesSection> section : propertiesFile.entrySet()) {
                for (Map.Entry<String, Object> item : section.getValue().entrySet()) {
                    object.getVariables().put(section.getKey() + ":" + item.getKey(), item.getValue());
                }
            }
        }
    }

    /**
     * Translates file paths for this platform.
     * @param path The path to translate
     * @return A translated path
     */
    private String translateFilePath(String path) {
        return path.replace("/", "\\").replace("\\", File.separator);
    }

    @Action(subId = -1, id = 27)
    public void SetGlobalValueInt(int id, int value) {
        scope.getGame().globalInts.put(id, value);
    }

    @Action(subId = -1, id = 19)
    public void SetGlobalString(int id, String value) {
        scope.getGame().globalStrings.put(id, value);
    }

    @Action(subId = -1, id = 3)
    public void SetGlobalValue(int id, Number value) {
        scope.getGame().globalInts.put(id, value);
    }

    @Action(subId = -2, id = 29)
    public void PlayLoopingChannelFileSample(String location, int channel, int times) {

        scope.getGame().getAudio().playFile(location, channel, times);
    }

    @Action(subId = -2, id = 11)
    public void PlayChannelSample(int unknown1, int unknown2, String name, int channel) {
        scope.getGame().getAudio().playFile(EscapistsRuntime.getRuntime().getGamePath().getAbsolutePath()
                + File.separator + "audio" + File.separator + name + ".wav", channel, 1);
    }

    public void SetChannelVolume(int channel, int volume) {
        // TODO: Audio
    }

    @Action(subId = -2, id = 15)
    public void StopChannel(int channel) {
        scope.getGame().getAudio().stopChannel(channel);
    }

    @Actions({
            @Action(subId = 42, id = 82),
            @Action(subId = 66, id = 95),
            @Action(subId = 13, id = 82)
    })
    public boolean CreateDirectory(String name) {
        return new File(name).mkdir();
    }

    @Action(subId = -6, id = 0)
    public void HideCursor() {
        scope.getGame().getPlatformUtils().hideMouse();
    }

    @Action(subId = 56, id = 80)
    public void OpenURL(String url) {
        Gdx.net.openURI(url);
    }

    @Action(subId = 65, id = 80)
    public void EmbedFont(String path) {
        // Fonts are loaded by the runtime at launch, so we are gonna ignore this.
    }

    @Action(subId = -7, id = 9)
    public void ChangeInputKey(int key, int keycode) {
        // TODO: Key binds
    }

    @Action(subId = 46, id = 80)
    public void initBlowfishEncryption(String key) {
        // TODO: Runtime decryption
    }
}
