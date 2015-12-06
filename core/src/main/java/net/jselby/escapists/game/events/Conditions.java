package net.jselby.escapists.game.events;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.ObjectInstance;

import java.io.File;
import java.lang.reflect.Method;

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

    public boolean MouseOnObject(ParameterValue.Object object) {
        // Find all objects which correspond to this
        int mouseX = scope.getGame().getMouseX();
        int mouseY = scope.getGame().getMouseY();

        if (mouseX == -1 && mouseY == -1) {
            return false;
        }

        boolean mouseOver = false;
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == object.objectInfo
                    && instance.getX() <= mouseX && instance.getY() <= mouseY
                    && instance.getX() + instance.getWidth() >= mouseX
                    && instance.getY() + instance.getHeight() >= mouseY) {
                mouseOver = true;
                scope.objects.add(instance);
            }
        }

        return mouseOver;
    }

    public boolean MouseClicked(int mouseButton, boolean doubleClick) {
        // We only get left clicks on mobile platforms
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && mouseButton != 0)
                && Gdx.input.isButtonPressed(mouseButton);

    }

    public boolean ObjectClicked(ParameterValue.Click click,
            ParameterValue.Object object) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop
                && click.click != 0) {
            // We only get left clicks on mobile platforms
            //System.out.println("Bad click type for platform: " + click.click);
            return false;
        }

        // TODO: Support double clicking
        // TODO: Clicked means once

        if (!Gdx.input.isButtonPressed(click.click)) {
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
            if (instance.getObjectInfo() == object.objectInfo
                    && instance.getX() <= mouseX && instance.getY() <= mouseY
                    && instance.getX() + instance.getWidth() >= mouseX
                    && instance.getY() + instance.getHeight() >= mouseY) {
                mouseOver = true;
                scope.objects.add(instance);
            }
        }

        if (mouseOver) {
            Gdx.input.vibrate(100);
        }

        return mouseOver;
    }

    public boolean Every(int id, ParameterValue.Every every) {
        String key = "_env_every_" + id;
        if (scope.getScene().getVariables().containsKey(key)) {
            // Check if it has updated
            long currentTime = System.currentTimeMillis();
            long lastTime = (Long) scope.getScene().getVariables().get(key);
            long diff = currentTime - lastTime;
            if (diff > every.delay) {
                currentTime -= diff - every.delay;
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

    public boolean KeyPressed(ParameterValue.KeyParameter key) {
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

    public boolean CompareGlobalString(ParameterValue.Short id,
            ParameterValue.String value) {
        return false;
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
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


    public boolean extension_FlagOff(ParameterValue.ExpressionParameter expression) {
        // TODO: Evaluate expressions
        int value = expression.expressions[0].num;

        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return !((Boolean) scope.getScene().getVariables().get(key));
        } else {
            return true;
        }
    }

    public boolean extension_FlagOn(ParameterValue.ExpressionParameter expression) {
        // TODO: Evaluate expressions
        int value = expression.expressions[0].num;

        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    public boolean extension_CompareY(ParameterValue.ExpressionParameter expression) {
        // TODO: Evaluate expressions
        int value = expression.expressions[0].num;

        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    public boolean groupActive(int id) {
        return scope.getScene().getActiveGroups().get(id);
    }

    public boolean extension_AnimationPlaying(ParameterValue.Short num) {
        // TODO: Animations
        return true;
    }

    // Utility methods

    /**
     * Returns a method for a particular condition, or null if one cannot be found.
     *
     * @param name The name of the condition to return
     * @return A method, or null if one cannot be found
     */
    public Method getMethodForCondition(String name) {
        for (Method method : Conditions.class.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
