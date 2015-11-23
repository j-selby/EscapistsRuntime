package net.jselby.escapists.game.events;

import com.badlogic.gdx.Gdx;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.ExpressionValue;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.objects.Text;

import java.lang.reflect.Method;

/**
 * Events are things that the engine can do to change the execution state.
 */
public class Actions {
    public static void Skip(Scope scope,
                            Events.Action action) {
    }

    public static void NextFrame(Scope scope,
                            Events.Action action) {
        scope.getGame().loadScene(scope.getGame().getSceneIndex() + 1);
    }

    public static void JumpToFrame(Scope scope,
                                 Events.Action action,
                                   ParameterValue.Short scene) {
        scope.getGame().loadScene(scene.value);
    }

    public static void EndApplication(Scope scope,
                                      Events.Action action) {
        // RIP
        scope.getGame().exit();
    }

    public static void SetX(Scope scope,
                            Events.Action action,
                            ParameterValue.ExpressionParameter xPos) {
        for (ObjectInstance object : scope.objects) {
            if (xPos.expressions[0].value instanceof ExpressionValue.XMouse) {
                object.setIsVisible(true);
                object.setX(scope.getGame().getMouseX());
            }
        }
        //System.out.println(scope.objects);
    }

    public static void SetY(Scope scope,
                            Events.Action action,
                            ParameterValue.ExpressionParameter xPos) {
        for (ObjectInstance object : scope.objects) {
            if (xPos.expressions[0].value instanceof ExpressionValue.YMouse) {
                object.setY(scope.getGame().getMouseY());
            }
        }
        //System.out.println(scope.objects);
    }

    public static void BringToFront(Scope scope,
                            Events.Action action) {
        for (ObjectInstance object : scope.objects) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            Layer[] layers = scope.getScene().getLayers();
            layers[layers.length - 1].objects.add(object);
        }
        //System.out.println(scope.objects);
    }

    public static void Reappear(Scope scope,
                                 Events.Action action) {
        for (ObjectInstance object : scope.objects) {
            object.setIsVisible(true);
        }
    }

    public static void SetString(Scope scope,
                                Events.Action action,
                                 ParameterValue.ExpressionParameter value) {
        for (ObjectInstance object : scope.objects) {
            if (object instanceof Text && value.expressions[0].value instanceof ExpressionValue.String) {
                ((Text) object).setString(((ExpressionValue.String) value.expressions[0].value).getValue());
            }
        }
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
