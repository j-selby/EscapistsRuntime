package net.jselby.escapists.game.events;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.ExpressionValue;
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
    public boolean Always(
    ) {
        return true;
    }

    public boolean Compare(String str1, String str2) {
        return str1.equals(str2);
    }

    public boolean StartOfFrame(
    ) {
        return scope.getScene().firstFrame;
    }

    public boolean OnLoop(ParameterValue.ExpressionParameter expString) {
        // TODO: Expression expansion
        return false;
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

    public boolean MouseClicked(ParameterValue.Click click) {
        // We only get left clicks on mobile platforms
//System.out.println("Bad click type for platform: " + click.click);
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && click.click != 0)
                && Gdx.input.isButtonPressed(click.click);

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

    public boolean CompareGlobalValueIntEqual(ParameterValue.Short id,
            ParameterValue.ExpressionParameter value) {
        if (scope.getGame().globalInts.containsKey((int) id.value)) {
            return ((ExpressionValue.Long) value.expressions[0].value).value == scope.getGame().globalInts.get((int) id.value);
        } else {
            return ((ExpressionValue.Long) value.expressions[0].value).value == 0;
        }
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
    }

    public boolean CompareGlobalString(ParameterValue.Short id,
            ParameterValue.String value) {
        return false;
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
    }


    public boolean TimerGreater(ParameterValue.Time time) {
        return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
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

    /*public boolean GroupActivated(
                                     ) {
        // TODO: Support group disabling
        return true;
    }

    public boolean GroupStart(
                                   ) {
        // TODO: Support group disabling
        return true;
    }*/

    public boolean GroupEnd() {
        return true;
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
