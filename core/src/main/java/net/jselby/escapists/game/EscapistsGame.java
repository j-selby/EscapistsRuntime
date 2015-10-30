package net.jselby.escapists.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.jselby.escapists.EscapistsRuntime;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import java.io.IOException;

public class EscapistsGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "net.jselby.escapists";

    private boolean firstFrameDrawn = false;
    private Sprite loadingLogo;
    private BitmapFont loadingFont;
    private float loadingTextWidth;

    private Application app;
    private Scene currentFrame;

    @Override
    public void initialise() {
        loadingLogo = new Sprite(new Texture(Gdx.files.internal("logo.png")));

        // Load the initial font, if possible
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Escapists.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.size = height / 10;
        parameter.genMipMaps = false;
        parameter.flip = true;

        loadingFont = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        loadingTextWidth = loadingFont.newFontCache().addText("Loading...", 0, 0).width;
    }

    private void loadFrame(Scene scene) {
        currentFrame = scene;
        scene.init(this);

        System.out.println("Launching frame: " + currentFrame.getName().trim());
    }

    @Override
    public void update(float delta) {
        if (currentFrame == null) {
            if (firstFrameDrawn) {
                EscapistsRuntime runtime = new EscapistsRuntime();
                try {
                    runtime.start();
                    app = runtime.getApplication();
                    app.init();
                    System.out.println("Callback from app, all assets prepared.");
                    loadFrame(app.frames.get(2)); // 2 = title screen, 6 = game
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadingFont.dispose();
                System.gc();
            } else {
                return;
            }
        }

        currentFrame.tick(this);
    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    
    @Override
    public void render(Graphics g) {
        if (currentFrame == null) {
            // Display loading logo
            g.drawSprite(loadingLogo,
                    g.getCurrentWidth() / 2 - loadingLogo.getWidth() / 2,
                    g.getCurrentHeight() / 2 - loadingLogo.getHeight() / 2 - 100);

            BitmapFont baseFont = g.getFont();
            g.setFont(loadingFont);
            g.setColor(Color.WHITE);
            g.drawString("Loading...",
                    g.getCurrentWidth() / 2 - loadingTextWidth / 2,
                    g.getCurrentHeight() / 2 + loadingLogo.getHeight() / 2 + 10);
            g.setFont(baseFont);

            firstFrameDrawn = true;
            return;
        }

        BitmapFont baseFont = g.getFont();

        g.setColor(currentFrame.getBackground());
        g.fillRect(0, 0, g.getCurrentWidth(), g.getCurrentHeight());
        g.scale(((float) g.getCurrentWidth()) / ((float) app.getWindowWidth()),
                ((float) g.getCurrentHeight()) / ((float) app.getWindowHeight()));

        currentFrame.draw(this, g);

        g.scale(1f / g.getScaleX(), 1f / g.getScaleY());
        g.setColor(Color.WHITE);

        int usedMem = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        g.setFont(baseFont);
        g.drawString("FPS: " + Gdx.graphics.getFramesPerSecond() + ", Mem: " + usedMem + " MB", 5, 5);
    }
}
