package net.jselby.escapists.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.IntMap;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.PlatformUtils;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EscapistsGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "net.jselby.escapists";
    private static double UPDATE_INTERVAL = 1000d / 45d;

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
    private int nextScene = -1;
    public IntMap<Number> globalInts;
    public IntMap<String> globalStrings;
    public ArrayList<String> mods;

    private boolean pauseError = false;
    private long lastFrame;
    private double diff;

    private long tpsSwitch;
    private int lastTPS;
    private int tps;

    private short[] mouseClicked = new short[3];

    public EscapistsGame(PlatformUtils utils) {
        this.utils = utils;
    }

    @Override
    public void initialise() {
        globalInts = new IntMap<Number>();
        globalStrings = new IntMap<String>();
        mods = new ArrayList<String>();
        audio = new AudioManager();

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

        // Add our default mod
        mods.add(":Leaderboards\n@170:    WorldInteraction.withObjects(546 /*hover*/).SetY((((Expressions.YMouse()/11) | 0)*11)+2);");

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

                            if (EscapistsRuntime.DEBUG) System.out.println("Callback from app, all assets prepared.");
                            try {
                                app.init(EscapistsGame.this);
                                UPDATE_INTERVAL = 1000d / ((double) app.getTargetFPS());
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
                    getPlatformUtils().showFatalMessageBox("Fatal Error", e.getLocalizedMessage());
                }
            }
        });
        loadingThread.setName("Asset Loading Thread");
        loadingThread.start();
    }

    private void loadFrame(Scene scene) {
        currentFrame = scene;
        System.out.println("Launching frame: " + currentFrame.getName().trim());
        scene.init();
    }

    public int getSceneIndex() {
        return sceneIndex;
    }

    public void loadScene(int id) {
        nextScene = id;
    }

    @Override
    public void update(float delta) {
        audio.tick();

        if (!pauseError && nextScene != -1) {
            // Load the next scene
            sceneIndex = nextScene;
            loadFrame(app.frames.get(nextScene));

            lastFrame = System.currentTimeMillis();
            tpsSwitch = lastFrame;
            tps = 0;
            diff = 0;

            nextScene = -1;
        }

        if (currentFrame == null) {
            return;
        }

        if (pauseError) {
            for (Layer layer : currentFrame.getLayers()) {
                if (!layer.isVisible()) { // "IsShow" flag
                    continue;
                }

                layer.tick(this);
            }

            return;
        }

        // Attempt to target 45fps
        long start = System.currentTimeMillis();
        diff += start - lastFrame;
        lastFrame = start;

        while(System.currentTimeMillis() - start < 10.00d && diff >= UPDATE_INTERVAL) {
            // Parse input
            for (int i = 0; i <= 2; i++) {
                if (Gdx.input.isButtonPressed(i)) {
                    if (mouseClicked[i] > 0) {
                        mouseClicked[i] = 2;
                    } else {
                        mouseClicked[i] = 1;
                    }
                } else {
                    mouseClicked[i] = 0;
                }
            }

            currentFrame.tick();

            tps++;
            diff -= UPDATE_INTERVAL;
        }

        if (diff >= UPDATE_INTERVAL) {
            System.err.println("Cannot keep up! Having to call tick() too many times per frame.");
            diff = 0;
        }

        if (System.currentTimeMillis() - tpsSwitch >= 1000) {
            tpsSwitch = System.currentTimeMillis() + System.currentTimeMillis() - tpsSwitch - 1000;
            lastTPS = tps;
            tps = 0;
        }

        // Update objects independently (animations, etc)
        for (Layer layer : currentFrame.getLayers()) {
            if (!layer.isVisible()) { // "IsShow" flag
                continue;
            }

            layer.tick(this);
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
            g.scale(g.getCurrentWidth() / ((float) app.getWindowWidth()),
                    g.getCurrentHeight() / ((float) app.getWindowHeight()));
            float scaleX = g.getScaleX();
            float scaleY = g.getScaleY();

            currentFrame.draw(this, g);

            g.scale(1f / g.getScaleX(), 1f / g.getScaleY());

            g.setFont(baseFont);
            g.setColor(Color.WHITE);

            int mouseX = getMouseX();
            int mouseY = getMouseY();

            if (EscapistsRuntime.DEBUG) {
                g.drawString("Mouse X: " + mouseX + ", Mouse Y: " + mouseY, 5, 35);
                g.drawString("scaleX: " + scaleX + ", scaleY: " + scaleY, 5, 50);
                g.drawString("Scene: " + currentFrame.getName(), 5, 65);
                g.drawString("Instance count: " + currentFrame.getObjects().size(), 5, 80);
                g.drawString("Event Handler implementation: " + currentFrame.eventTicker.getClass().getName(), 5, 95);

                String layers = "";
                Layer[] layersInScene = currentFrame.getLayers();
                for (int i = 0; i < layersInScene.length; i++) {
                    Layer layer = layersInScene[i];
                    if (layer.isVisible()) {
                        if (layers.length() > 0) {
                            layers += ", ";
                        }
                        layers += i + " (" + layer.getName() + ")";
                    }
                }
                g.drawString("Visible layers: [" + layers + "]", 5, 110);
                g.drawString("Active groups: [" + Arrays.toString(currentFrame.getActiveGroups()) + "]", 5, 125);
            }
        }

        if (EscapistsRuntime.DEBUG) {
            int usedMem = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

            g.drawString("Escapists Runtime v" + EscapistsRuntime.VERSION + " on " +
                    Gdx.app.getType().name(), 5, 5);
            g.drawString("FPS: " + Gdx.graphics.getFramesPerSecond() + ", TPS: " + lastTPS + ", Mem: "
                    + usedMem + " MB", 5, 20);
        }

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

    public boolean isButtonClicked(int buttonId) {
        return mouseClicked[buttonId] == 1;
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

    public void addMod(String contents) {
        mods.add(contents);
    }

    public AudioManager getAudio() {
        return audio;
    }
}
