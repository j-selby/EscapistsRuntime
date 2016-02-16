package net.jselby.escapists.game.events.functions;

import com.badlogic.gdx.*;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Expression;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Full-screen / windowed functions.
 */
public class Windows extends FunctionCollection {
    @Action(subId = 53, id = 81)
    public void SetWindowed() {
        if (Gdx.graphics.supportsDisplayModeChange()) {
            Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        } else {
            System.out.println("Platform rejected SetWindowed()");
        }
    }

    @Action(subId = 38, id = 93)
    public void SetWindowNormal() {
        System.out.println("STUB: SetWindowNormal()");
    }

    @Action(subId = 38, id = 83)
    public void SetWindowWidth(int width) {
        if (Gdx.graphics.supportsDisplayModeChange()) {
            Gdx.graphics.setDisplayMode(width, Gdx.graphics.getHeight(), Gdx.graphics.isFullscreen());
        } else {
            System.out.println("Platform rejected SetWindowWidth()");
        }
    }

    @Action(subId = 38, id = 84)
    public void SetWindowHeight(int height) {
        if (Gdx.graphics.supportsDisplayModeChange()) {
            Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), height, Gdx.graphics.isFullscreen());
        } else {
            System.out.println("Platform rejected SetWindowHeight()");
        }
    }

    @Action(subId = 38, id = 97)
    public void SetWindowPosition(int x, int y) {
        System.out.println("STUB: SetWindowPosition()");
    }

    @Expression(subId = 38, id = 84)
    public int GetScreenWidth() {
        return Gdx.graphics.getWidth();
    }

    @Expression(subId = 38, id = 85)
    public int GetScreenHeight() {
        return Gdx.graphics.getHeight();
    }
}
