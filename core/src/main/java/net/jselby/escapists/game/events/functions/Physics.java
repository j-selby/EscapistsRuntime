package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Physics functions handle physics-related tasks.
 */
public class Physics extends FunctionCollection {
    @Condition(subId = 2, id = -14)
    public boolean OnCollision(int val1, int val2) {
        // TODO: Um, WTF?
        return false;
    }
}
