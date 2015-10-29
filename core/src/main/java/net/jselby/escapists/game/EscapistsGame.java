package net.jselby.escapists.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import net.jselby.escapists.EscapistsRuntime;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.io.IOException;

public class EscapistsGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "net.jselby.escapists";
    private Application app;
    private Scene currentFrame;

    @Override
    public void initialise() {
        EscapistsRuntime runtime = new EscapistsRuntime();
        try {
            runtime.start();
            app = runtime.getApplication();
            app.init();
            System.out.println("Callback from app, all assets prepared.");
            loadFrame(app.frames.get(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFrame(Scene scene) {
        currentFrame = scene;

        System.out.println("Launching frame: " + currentFrame.getName().trim());
    }

    @Override
    public void update(float delta) {

    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    
    @Override
    public void render(Graphics g) {
        BitmapFont baseFont = g.getFont();

        g.setColor(currentFrame.getBackground());
        g.fillRect(0, 0, g.getCurrentWidth(), g.getCurrentHeight());
        g.scale(((float) g.getCurrentWidth()) / ((float) app.getWindowWidth()),
                ((float) g.getCurrentHeight()) / ((float) app.getWindowHeight()));

        for (Layer layer : currentFrame.getLayers()) {
            if (!layer.isVisible()) { // "IsShow" flag
                continue;
            }

            layer.draw(this, g);

        }

        g.scale(1f / g.getScaleX(), 1f / g.getScaleY());
        g.setColor(Color.WHITE);

        int usedMem = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        g.setFont(baseFont);
        g.drawString("FPS: " + Gdx.graphics.getFramesPerSecond() + ", Mem: " + usedMem + " MB", 5, 5);
    }
}
