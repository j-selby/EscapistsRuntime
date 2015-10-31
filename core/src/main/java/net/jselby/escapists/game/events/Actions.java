package net.jselby.escapists.game.events;

import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.ParameterValue;

import java.lang.reflect.Method;

/**
 * Events are things that the engine can do to change the execution state.
 */
public class Actions {
    public static void Skip(Scope scope,
                            Events.Action action) {
    }

    public static void EndApplication(Scope scope,
                                      Events.Action action) {
        // RIP
        scope.getGame().exit();
    }

    public static void extension_SetDirection(Scope scope,
                                              Events.Action action,
                                              ParameterValue.Int newDirection) {
        //System.out.println(scope.objects);
    }

    // Utility methods

    /**
     * Returns a method for a particular condition, or null if one cannot be found.
     * @param name The name of the condition to return
     * @return A method, or null if one cannot be found
     */
    public static Method getMethodForAction(String name) {
        for (Method method : Actions.class.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
