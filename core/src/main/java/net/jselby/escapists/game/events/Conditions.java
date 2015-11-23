package net.jselby.escapists.game.events;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.ExpressionValue;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.ObjectInstance;

import java.lang.reflect.Method;

/**
 * Implementations of conditions within the engine.
 *
 * @author j_selby
 */
public class Conditions {
    public static boolean Always(Scope scope,
                                        Events.Condition condition) {
        return true;
    }

    public static boolean Compare(Scope scope,
                                  Events.Condition condition,
                                  ParameterValue.ExpressionParameter expression,
                                  ParameterValue.ExpressionParameter comparison) {
        // TODO: Compare
        if (expression.toString().contains("RUN_EDITOR")) {
            return false;
        }
        return true;
    }

    public static boolean StartOfFrame(Scope scope,
                                        Events.Condition condition) {
        return scope.getScene().firstFrame;
    }

    public static boolean OnLoop(Scope scope,
                                 Events.Condition condition,
                                 ParameterValue.ExpressionParameter expString) {
        // TODO: Expression expansion
        return false;
    }

    public static boolean MouseOnObject(Scope scope,
                                        Events.Condition condition,
                                        ParameterValue.Object object) {
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

    public static boolean MouseClicked(Scope scope,
                                        Events.Condition condition,
                                        ParameterValue.Click click) {
        // We only get left clicks on mobile platforms
//System.out.println("Bad click type for platform: " + click.click);
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && click.click != 0)
                && Gdx.input.isButtonPressed(click.click);

    }

    public static boolean ObjectClicked(Scope scope,
                                        Events.Condition condition,
                                        ParameterValue.Click click,
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

    public static boolean Every(Scope scope,
                                Events.Condition condition,
                                ParameterValue.Every every) {
        String key = "_env_every_" + condition.identifier;
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

    public static boolean Once(Scope scope,
                                Events.Condition condition) {
        String key = "_env_once_" + condition.identifier;
        if (scope.getScene().getVariables().containsKey(key)) {
            return false;
        } else {
            scope.getScene().getVariables().put(key, true);
            return true;
        }
    }

    public static boolean KeyPressed(Scope scope,
                                     Events.Condition condition,
                                     ParameterValue.KeyParameter key) {
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

    public static boolean CompareGlobalValueIntEqual(Scope scope,
                                                     Events.Condition condition,
                                                     ParameterValue.Short id,
                                                     ParameterValue.ExpressionParameter value) {
        if (scope.getGame().globalInts.containsKey((int) id.value)) {
            return ((ExpressionValue.Long) value.expressions[0].value).value == scope.getGame().globalInts.get((int) id.value);
        } else {
            return ((ExpressionValue.Long) value.expressions[0].value).value == 0;
        }
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
    }

    public static boolean CompareGlobalString(Scope scope,
                                                      Events.Condition condition,
                                                      ParameterValue.Short id,
                                                      ParameterValue.String value) {
        return false;
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
    }


    public static boolean TimerGreater(Scope scope,
                                       Events.Condition condition,
                                       ParameterValue.Time time) {
        return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > time.timer;
    }

    public static boolean SteamHasGameLicense(Scope scope,
                                       Events.Condition condition) {
        return scope.getGame().getPlatformUtils().verifySteam();
    }


    public static boolean extension_FlagOff(Scope scope,
                                            Events.Condition condition,
                                            ParameterValue.ExpressionParameter expression) {
        // TODO: Evaluate expressions
        int value = expression.expressions[0].num;

        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return !((Boolean) scope.getScene().getVariables().get(key));
        } else {
            return true;
        }
    }

    public static boolean extension_FlagOn(Scope scope,
                                           Events.Condition condition,
                                           ParameterValue.ExpressionParameter expression) {
        // TODO: Evaluate expressions
        int value = expression.expressions[0].num;

        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    public static boolean extension_CompareY(Scope scope,
                                           Events.Condition condition,
                                           ParameterValue.ExpressionParameter expression) {
        // TODO: Evaluate expressions
        int value = expression.expressions[0].num;

        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    /*public static boolean GroupActivated(Scope scope,
                                     Events.Condition condition) {
        // TODO: Support group disabling
        return true;
    }

    public static boolean GroupStart(Scope scope,
                                   Events.Condition condition) {
        // TODO: Support group disabling
        return true;
    }*/

    public static boolean GroupEnd(Scope scope,
                                             Events.Condition condition) {
        return true;
    }

    public static boolean extension_AnimationPlaying(Scope scope,
                                   Events.Condition condition,
                                                     ParameterValue.Short num) {
        // TODO: Animations
        return true;
    }

    // Utility methods

    /**
     * Returns a method for a particular condition, or null if one cannot be found.
     * @param name The name of the condition to return
     * @return A method, or null if one cannot be found
     */
    public static Method getMethodForCondition(String name) {
        for (Method method : Conditions.class.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
