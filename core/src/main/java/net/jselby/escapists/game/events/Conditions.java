package net.jselby.escapists.game.events;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.ObjectInstance;

import java.io.File;

/**
 * Implementations of conditions within the engine.
 *
 * @author j_selby
 */
public class Conditions extends Parameters {
    public boolean Always() {
        return true;
    }

    public boolean Compare(Object str1, Object str2) {
        return str1.equals(str2);
    }

    public boolean StartOfFrame() {
        return scope.getScene().firstFrame;
    }

    public boolean OnLoop(String loopName) {
        return scope.getScene().getActiveLoops().containsKey(loopName);
    }

    public boolean OnGroupActivation() {
        // TODO: Once-only
        //return scope.getScene().getActiveGroups().containsKey(id);
        return true;
    }

    public boolean HasItemValue(String key, String value) {
        // TODO: Object specific variables
        return false;
    }

    public boolean MouseOnObject(int objectId) {
        // Find all objects which correspond to this
        int mouseX = scope.getGame().getMouseX();
        int mouseY = scope.getGame().getMouseY();

        if (mouseX == -1 && mouseY == -1) {
            return false;
        }

        boolean mouseOver = false;
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == objectId
                    && instance.getScreenX() <= mouseX
                    && instance.getScreenY() <= mouseY
                    && instance.getScreenX() + instance.getWidth() >= mouseX
                    && instance.getScreenY() + instance.getHeight() >= mouseY)  {
                mouseOver = true;
                scope.addObjectToScope(instance);
            }
        }

        return mouseOver;
    }

    public boolean MouseClicked(int mouseButton, boolean doubleClick) {
        // We only get left clicks on mobile platforms
        // TODO: Clicked vs held
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && mouseButton != 0)
                && Gdx.input.isButtonPressed(mouseButton);

    }

    public boolean WhileMousePressed(int mouseButton) {
        // We only get left clicks on mobile platforms
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && mouseButton != 0)
                && Gdx.input.isButtonPressed(mouseButton);

    }

    public boolean ObjectClicked(int mouseButton, boolean doubleClicked,
            int object) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop
                && mouseButton != 0) {
            // We only get left clicks on mobile platforms
            //System.out.println("Bad click type for platform: " + click.click);
            return false;
        }

        // TODO: Support double clicking
        // TODO: Clicked means once

        if (!Gdx.input.isButtonPressed(mouseButton)) {
            return false;
        }

        // Find all objects which correspond to this
        int mouseX = scope.getGame().getMouseX();
        int mouseY = scope.getGame().getMouseY();

        if (mouseX == -1 && mouseY == -1) {
            return false;
        }

        boolean mouseOver = false;
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == object
                    && instance.getScreenX() <= mouseX
                    && instance.getScreenY() <= mouseY
                    && instance.getScreenX() + instance.getWidth() >= mouseX
                    && instance.getScreenY() + instance.getHeight() >= mouseY) {
                mouseOver = true;
                scope.addObjectToScope(instance);
            }
        }

        if (mouseOver) {
            Gdx.input.vibrate(100);
        }

        return mouseOver;
    }

    public boolean Every(int id, int every) {
        String key = "_env_every_" + id;
        if (scope.getScene().getVariables().containsKey(key)) {
            // Check if it has updated
            long currentTime = System.currentTimeMillis();
            long lastTime = (Long) scope.getScene().getVariables().get(key);
            long diff = currentTime - lastTime;
            if (diff > every) {
                currentTime -= diff - every;
                scope.getScene().getVariables().put(key, currentTime);
                return true;
            }
        } else {
            scope.getScene().getVariables().put(key, System.currentTimeMillis());
        }
        return false;
    }

    public boolean Once(int id) {
        String key = "_env_once_" + id;
        if (scope.getScene().getVariables().containsKey(key)) {
            return false;
        } else {
            scope.getScene().getVariables().put(key, true);
            return true;
        }
    }

    public boolean KeyPressed(int key) {
        // TODO: Key pressed
        //Gdx.input.
        /*if (validate) {
            System.out.println("Key " + key.key + " pressed.");
        } else {
            System.out.println("Key " + key.key + "(" + KeyEvent.getKeyText(key.key) + "," + Input.Keys.toString(key.key) + ") not pressed.");
        }
        return validate;*/
        return false;
    }

    public boolean CompareGlobalValueIntEqual(int id, int value) {
        if (scope.getGame().globalInts.containsKey(id)) {
            return value == scope.getGame().globalInts.get(id).intValue();
        } else {
            return value == 0;
        }
    }

    public boolean CompareGlobalString(int id,
            String value) {
        return value.equals(scope.getGame().globalStrings.get(id));
    }

    public boolean CompareAlterableValueInt(int id, int value) {
        for (ObjectInstance item : scope.getObjects()) {
            if (item.getVariables().containsKey("" + id)) {
                return value == (Integer) item.getVariables().get("" + id);
            }
        }
        return false;
    }

    public boolean TimerEquals(int value, int repeat/*?*/) {
        //int currentTimer = (int) (scope.getScene().getFrameCount() * (1f / 45f) * 1000);
        // Convert to frame
        value /= (1f / 45f) * 1000;
        return value == scope.getScene().getFrameCount();
        //System.out.println(value + ":" + currentTimer);
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) == value;
    }


    public boolean TimerGreater(int value) {
        return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > value;
    }

    public boolean DirectoryExists(String name) {
        return new File(name).exists();
    }

    public boolean SteamHasGameLicense() {
        return scope.getGame().getPlatformUtils().verifySteam();
    }

    public boolean IsFlagOff(int value) {
        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return !((Boolean) scope.getScene().getVariables().get(key));
        } else {
            return true;
        }
    }

    public boolean IsFlagOn(int value) {
        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    public boolean CompareY(int y) {
        ObjectInstance[] objects = scope.getObjects();
        if (objects.length == 0) {
            return false;
        }
        // TODO: More then? Less then?
        for (ObjectInstance instance : objects) {
            if (y == 57 && instance.getY() <= 57) {
                return true;
            } else if (y == 60 && instance.getY() >= 60) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean IsLayerVisible(int layer) {
        return scope.getScene().getLayers()[layer - 1].isVisible();
    }

    public boolean FacingInDirection(int direction) {
        // TODO: Directions
        return true;
    }

    public boolean GroupActivated(int id) {
        return scope.getScene().getActiveGroups().get(id);
    }

    public boolean AnimationPlaying(int num) {
        ObjectInstance[] objects = scope.getObjects();
        if (objects.length == 0) {
            return false;
        }
        for (ObjectInstance instance : objects) {
            if (instance.getAnimation() != num) {
                return false;
            }
        }
        return true;
    }
}
