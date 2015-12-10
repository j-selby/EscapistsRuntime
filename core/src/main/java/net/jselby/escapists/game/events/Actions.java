package net.jselby.escapists.game.events;

import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.objects.Text;

import java.io.File;

/**
 * Events are things that the engine can do to change the execution state.
 */
public class Actions extends Conditions {
    public void Skip() {
    }

    public void NextFrame() {
        scope.getGame().loadScene(scope.getGame().getSceneIndex() + 1);
    }

    public void JumpToFrame(ParameterValue.Short scene) {
        scope.getGame().loadScene(scene.value);
    }

    public void ActivateGroup(int id) {
        System.out.printf("Starting group: %d.\n", id);
        scope.getScene().getActiveGroups().put(id, true);
    }

    public void DeactivateGroup(int id) {
        System.out.printf("Stopping group: %d.\n", id);
        scope.getScene().getActiveGroups().put(id, false);
    }

    public void StartLoop(String name, int times) {
        scope.getScene().getActiveLoops().put(name, times);
    }

    public void EndApplication() {
        // RIP
        scope.getGame().exit();
    }

    public void FlagOn(int value) {
        String key = "_env_flag_" + value;
        scope.getScene().getVariables().put(key, true);
    }

    public void FlagOff(int value) {
        String key = "_env_flag_" + value;
        scope.getScene().getVariables().put(key, false);
    }

    public void SetX(int newX) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setX(newX);
        }
    }

    public void SetY(int newY) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setY(newY);
        }
    }

    public void BringToFront() {
        for (ObjectInstance object : scope.getObjects()) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            Layer[] layers = scope.getScene().getLayers();
            layers[layers.length - 1].objects.add(object);
        }
    }

    public void Disappear() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setVisible(false);
        }
    }

    public void Reappear() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setVisible(true);
        }
    }

    public void SetDirection(int newDir) {
        // TODO: Better Animation implementation that supports this
    }

    public void SetAnimation(int newAnimation) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setAnimation(newAnimation);
        }
    }

    public void SetString(String value) {
        for (ObjectInstance object : scope.getObjects()) {
            if (object instanceof Text) {
                ((Text) object).setString(value);
            }
        }
    }

    public void ClearObjectVarArray() {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().clear();
        }
    }

    public void SetIntegerVar(String name, int val) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, val);
        }
    }

    public void SetStringVar(String name, String val) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, val);
        }
    }

    public void SetGlobalValueInt(int id, int value) {
        scope.getGame().globalInts.put(id, value);
    }

    public void SetGlobalString(int id, String value) {
        scope.getGame().globalStrings.put(id, value);
    }

    public void SetGlobalValue(int id, Number value) {
        scope.getGame().globalInts.put(id, value);
    }

    public void PlayLoopingChannelFileSample(String location, int channel, int times) {
        // TODO: Audio
    }

    public void PlayChannelSample(int unknown1, int unknown2, String name, int channel) {
        // TODO: Audio
    }

    public void SetChannelVolume(int channel, int volume) {
        // TODO: Audio
    }

    public void StopChannel(int channel) {
        // TODO: Audio
    }

    public void LoadPropertiesFile(String filename, int id) {

    }

    public boolean CreateDirectory(String name) {
        return new File(name).mkdir();
    }

    public void HideCursor() {
        scope.getGame().getPlatformUtils().hideMouse();
    }

    public void EmbedFont(String path) {
        // Fonts are loaded by the runtime at launch, so we are gonna ignore this.
        System.out.println("Application requested font load: " + path);
    }

    public void ChangeInputKey(int key, int keycode) {
        // TODO: Key binds
    }
}
