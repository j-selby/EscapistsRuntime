package net.jselby.escapists.game.events;

import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.Expression;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Scope is the scope in which a condition/action can reach to.
 */
public class Scope {
    private EscapistsGame game;
    private Scene scene;
    public List<ObjectInstance> objects = new ArrayList<ObjectInstance>();

    public Scope(EscapistsGame game, Scene scene) {
        this.game = game;
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public EscapistsGame getGame() {
        return game;
    }

    public Object evaluate(Events.Condition condition, ParameterValue.ExpressionParameter expression) {
        for (Expression expression1 : expression.expressions) {
            //System.out.println(expression1.value);
        }
        return false;
        /*if (expression.)
        EQUAL = 0
        DIFFERENT = 1
        LOWER_OR_EQUAL = 2
        LOWER = 3
        GREATER_OR_EQUAL = 4
        GREATER = 5
        expression.expressions[0].
        return 0;*/
    }
}
