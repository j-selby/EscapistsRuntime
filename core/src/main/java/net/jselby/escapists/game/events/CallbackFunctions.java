package net.jselby.escapists.game.events;

import com.badlogic.gdx.Gdx;

/**
 * Functions called on callback of a successful triggering of a statement.
 */
public class CallbackFunctions extends ExpressionFunctions {
    public void Vibrate(Object... ignored) {
        Gdx.input.vibrate(100);
    }

    public void OnceFinalize(int id) {
        String key = "_env_once_" + id;
        if (!scope.getScene().getVariables().containsKey(key)) {
            scope.getScene().getVariables().put(key, true);
        }
    }
}
