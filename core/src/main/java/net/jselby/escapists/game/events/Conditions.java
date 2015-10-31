package net.jselby.escapists.game.events;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.data.chunks.Events;
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
        return false;
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

    public static boolean ObjectClicked(Scope scope,
                                        Events.Condition condition,
                                        ParameterValue.Click click,
                                        ParameterValue.Object object) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop
                && click.click != 0) {
            // We only get left clicks on mobile platforms
            System.out.println("Bad click type for platform: " + click.click);
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
        boolean isTime = false;
        if (scope.getScene().getVariables().containsKey(key)) {
            // Check if it has updated
            long currentTime = System.currentTimeMillis();
            long lastTime = (Long) scope.getScene().getVariables().get(key);
            long diff = currentTime - lastTime;
            if (diff > every.delay) {
                currentTime -= diff - every.delay;
                isTime = true;
                scope.getScene().getVariables().put(key, currentTime);
            }
        } else {
            scope.getScene().getVariables().put(key, System.currentTimeMillis());
        }
        return isTime;
    }

    public static boolean Once(Scope scope,
                                Events.Condition condition) {
        String key = "_env_every_" + condition.identifier;
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

    public static boolean TimerGreater(Scope scope,
                                       Events.Condition condition,
                                       ParameterValue.Time time) {
        // TODO: Timers
        /*String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }*/
        return false;
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
