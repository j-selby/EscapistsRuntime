package net.jselby.escapists.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.PlatformUtils;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EscapistsGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "net.jselby.escapists";
    private static final long UPDATE_INTERVAL = 1000 / 45;

    private Sprite loadingLogo;
    private BitmapFont loadingFont;
    private BitmapFontCache loadingFontCache;
    private float loadingTextWidth;
    private String loadingText = "Loading...";

    private Application app;
    private Scene currentFrame;
    private AudioManager audio;

    private PlatformUtils utils;
    private int sceneIndex;
    public Map<Integer, Number> globalInts;
    public Map<Integer, String> globalStrings;
    private ArrayList<String> mods;

    private boolean pauseError = false;
    private long lastFrame;

    public EscapistsGame(PlatformUtils utils) {
        this.utils = utils;
    }

    @Override
    public void initialise() {
        globalInts = new HashMap<Integer, Number>();
        globalStrings = new HashMap<Integer, String>();
        mods = new ArrayList<String>();
        audio = new AudioManager();

        // Add default mod
        addMod("default_mod", ":pre-titles\n" +
                ">15:env.withObjects(18).SetString(env.CurrentText(18) + \"\\n\\nModding by jselby.\\nhttp://jselby.net\");\n" +
                "\n" +
                ":title_screen\n" +
                "@20:env.withObjects(24).SetString(env.GlobalString(1) + \" (UER)\");");

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

        loadingFontCache = loadingFont.newFontCache();
        loadingTextWidth = loadingFontCache.addText(loadingText, 0, 0).width;

        // Start up the loading thread
        Thread loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EscapistsRuntime runtime = new EscapistsRuntime();
                    if (!runtime.start(EscapistsGame.this)) {
                        return;
                    }
                    app = runtime.getApplication();

                    try {
                        // Let the GFX thread catch up
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            EscapistsGame.this.app = app;

                            System.out.println("Callback from app, all assets prepared.");
                            try {
                                app.init(EscapistsGame.this);
                                loadScene(0); // 2 = title screen, 6 = game
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            loadingFont.dispose();
                            loadingFont = null;
                            loadingLogo = null;
                            System.gc();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loadingThread.setName("Asset Loading Thread");
        loadingThread.start();
    }

    private void loadFrame(Scene scene) {
        currentFrame = scene;
        scene.init(this);

        System.out.println("Launching frame: " + currentFrame.getName().trim());
    }

    public int getSceneIndex() {
        return sceneIndex;
    }

    public void loadScene(int id) {
        sceneIndex = id;
        loadFrame(app.frames.get(id));
        lastFrame = System.currentTimeMillis();
    }

    @Override
    public void update(float delta) {
        audio.tick();

        if (pauseError) {
            return;
        }

        if (currentFrame == null) {
            return;
        }

        // Attempt to target 45fps
        long diff = System.currentTimeMillis() - lastFrame;
        int times = 0;
        while (diff > UPDATE_INTERVAL) {
            lastFrame = System.currentTimeMillis();
            currentFrame.tick(this);
            diff -= UPDATE_INTERVAL;
            times++;
            if (times > 2) {
                System.err.println("Cannot keep up! Having to call tick() too many times per frame.");
                break;
            }
        }

        getPlatformUtils().tick();
    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    
    @Override
    public void render(Graphics g) {
        if (pauseError) {
            return;
        }

        BitmapFont baseFont = g.getFont();

        if (currentFrame == null) {
            // Display loading logo
            g.drawSprite(loadingLogo,
                    g.getCurrentWidth() / 2 - loadingLogo.getWidth() / 2,
                    g.getCurrentHeight() / 2 - loadingLogo.getHeight() / 2 - 100);

            g.setFont(loadingFont);
            g.setColor(Color.WHITE);
            g.drawString(loadingText,
                    g.getCurrentWidth() / 2 - loadingTextWidth / 2,
                    g.getCurrentHeight() / 2 + loadingLogo.getHeight() / 2 + 10);
            g.setFont(baseFont);
        } else {

            g.setColor(currentFrame.getBackground());
            g.fillRect(0, 0, g.getCurrentWidth(), g.getCurrentHeight());
            g.scale(((float) g.getCurrentWidth()) / ((float) app.getWindowWidth()),
                    ((float) g.getCurrentHeight()) / ((float) app.getWindowHeight()));
            float scaleX = g.getScaleX();
            float scaleY = g.getScaleY();

            currentFrame.draw(this, g);

            g.scale(1f / g.getScaleX(), 1f / g.getScaleY());

            g.setFont(baseFont);
            g.setColor(Color.WHITE);

            int mouseX = getMouseX();
            int mouseY = getMouseY();

            g.drawString("Mouse X: " + mouseX + ", Mouse Y: " + mouseY, 5, 35);
            g.drawString("scaleX: " + scaleX + ", scaleY: " + scaleY, 5, 50);
            g.drawString("Scene: " + currentFrame.getName(), 5, 65);
        }

        int usedMem = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        g.drawString("Escapists Runtime v" + EscapistsRuntime.VERSION, 5, 5);
        g.drawString("FPS: " + Gdx.graphics.getFramesPerSecond() + ", Mem: "
                + usedMem + " MB", 5, 20);

    }

    public int getMouseX() {
        return (int) (((float) Gdx.input.getX())
                / (((float) getWidth())
                / ((float) EscapistsRuntime.getRuntime().getApplication().getWindowWidth())));
    }

    public int getMouseY() {
        return (int) (((float) Gdx.input.getY())
                / (((float) getHeight())
                / ((float) EscapistsRuntime.getRuntime().getApplication().getWindowHeight())));
    }

    public void setLoadingMessage(final String message) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (currentFrame != null) {
                    return;
                }
                // Recalculate message dimensions
                loadingText = message;
                loadingTextWidth = loadingFontCache.addText(loadingText, 0, 0).width;
            }
        });
    }

    public void fatalPrompt(final String msg) {
        pauseError = true;
        utils.showFatalMessageBox("Error", msg);
    }

    public void exit() {
        utils.exit();
    }

    public PlatformUtils getPlatformUtils() {
        return utils;
    }

    public Collection<String> getMods() {
        return mods;
    }

    public void addMod(String name, String contents) {
        mods.add(contents);
    }

    public AudioManager getAudio() {
        return audio;
    }
}
