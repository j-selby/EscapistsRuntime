package net.jselby.escapists.graphics;

import net.jselby.escapists.game.Application;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * The main thread that the engine runs on.
 *
 * @author j_selby
 */
public class EngineThread extends Thread {
    private AppGameContainer container;
    private EscapistsGame game;

    public EngineThread(AppGameContainer container, EscapistsGame game) {
        this.container = container;
        this.game = game;
        setName("Engine Thread");
    }

    @Override
    public void run() {
        try {
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void setApplication(Application app) {
        game.app = app;
        game.appNeedsUpdate = true;
    }
}
