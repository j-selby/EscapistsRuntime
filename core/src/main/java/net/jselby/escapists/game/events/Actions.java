package net.jselby.escapists.game.events;

import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.objects.Text;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Events are things that the engine can do to change the execution state.
 */
public class Actions extends Conditions {
    public void Skip() {
    }

    public void NextFrame() {
        //scope.getGame().loadScene(scope.getGame().getSceneIndex() + 1);
    }

    public void JumpToFrame(ParameterValue.Short scene) {
        //scope.getGame().loadScene(scene.value);
    }

    public void ActivateGroup(int id) {
        scope.getScene().getActiveGroups().put(id, true);
    }

    public void StartLoop(String name, int times) {
        // TODO: Loops
    }

    public void EndApplication() {
        // RIP
        scope.getGame().exit();
    }

    public void SetX(int newX) {
        for (ObjectInstance object : scope.objects) {
            object.setX(newX);
        }
    }

    public void SetY(int newY) {
        for (ObjectInstance object : scope.objects) {
            object.setY(newY);
        }
    }

    public void BringToFront() {
        for (ObjectInstance object : scope.objects) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            Layer[] layers = scope.getScene().getLayers();
            layers[layers.length - 1].objects.add(object);
        }
        //System.out.println(scope.objects);
    }

    public void Disappear() {
        for (ObjectInstance object : scope.objects) {
            object.setIsVisible(false);
        }
    }

    public void Reappear() {
        for (ObjectInstance object : scope.objects) {
            object.setIsVisible(true);
        }
    }

    public void SetString(String value) {
        for (ObjectInstance object : scope.objects) {
            if (object instanceof Text) {
                ((Text) object).setString(value);
            }
        }
    }

    public void SetGlobalValueInt(int id, int value) {
        scope.getGame().globalInts.put(id, value);
    }

    public void SetGlobalString(int id, String value) {
        // TODO: Global Strings
        //scope.getGame().globalInts.put(id, value);
    }

    public void SetGlobalValue(int id, int value) {
        // TODO: Global Strings
        //scope.getGame().globalInts.put(id, value);
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

    public void extension_SetDirection(ParameterValue.Int newDirection) {
        //System.out.println(scope.objects);
    }

    // Utility methods

    /**
     * Returns a method for a particular condition, or null if one cannot be found.
     * @param name The name of the condition to return
     * @return A method, or null if one cannot be found
     */
    public Method getMethodForAction(String name) {
        for (Method method : Actions.class.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
