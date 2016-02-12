package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Expression;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Full-screen / windowed functions.
 */
public class Windows extends FunctionCollection {
    @Action(subId = 53, id = 81)
    public void SetWindowed() {
        System.out.println("STUB: SetWindowed()");
    }

    @Action(subId = 38, id = 93)
    public void SetWindowNormal() {
        System.out.println("STUB: SetWindowNormal()");
    }

    @Action(subId = 38, id = 83)
    public void SetWindowWidth(int width) {
        System.out.println("STUB: SetWindowWidth()");
    }

    @Action(subId = 38, id = 84)
    public void SetWindowHeight(int height) {
        System.out.println("STUB: SetWindowHeight()");
    }

    @Action(subId = 38, id = 97)
    public void SetWindowPosition(int x, int y) {
        System.out.println("STUB: SetWindowPosition()");
    }

    @Expression(subId = 38, id = 84)
    public double GetScreenWidth() {
        System.out.println("STUB: GetScreenWidth()");
        return 0;
    }

    @Expression(subId = 38, id = 85)
    public double GetScreenHeight() {
        System.out.println("STUB: GetScreenHeight()");
        return 0;
    }
}
