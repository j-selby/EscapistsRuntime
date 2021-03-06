package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Application functions control application-wide functionality.
 */
public class Application extends FunctionCollection {
    @Action(subId = -3, id = 4)
    public void EndApplication() {
        // RIP
        scope.getGame().exit();
    }

    @Action(subId = -6, id = 0)
    public void HideCursor() {
        scope.getGame().getPlatformUtils().hideMouse();
    }
}
