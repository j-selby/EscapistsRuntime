package net.jselby.escapists.game.desktop;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamUser;
import net.jselby.escapists.DebugFrame;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.PlatformUtils;
import net.jselby.escapists.game.EscapistsGame;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.io.File;

/**
 * PlatformUtils implementation for Desktop platforms.
 */
public class DesktopPlatformUtils extends PlatformUtils {
    private DesktopDebugFrame debugFrame;// = new DesktopDebugFrame();

    private SteamUser user;
    private SteamID steamId;
    private SteamID ownerSteamID;
    private EscapistsGame game;

    public DesktopPlatformUtils() {
        // Write the game ID to the filesystem
        /*try {
            FileOutputStream out = new FileOutputStream("steam_appid.txt");
            out.write("298630".getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!SteamAPI.init()) {
                JOptionPane.showMessageDialog(null, "Steam could not be detected.",
                        "Escapists Runtime", JOptionPane.ERROR_MESSAGE);
                exit();
                return;
            }

            if (SteamAPI.restartAppIfNecessary(298630)) {
                // Well, the real game was launched. RIP.
                exit();
                return;
            }
        } finally {
            new File("steam_appid.txt").delete();
        }*/

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                //SteamAPI.shutdown();
            }
        }));

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Keep the game so we can access raw info
     * @param game
     */
    public void setGame(EscapistsGame game) {
        this.game = game;
    }

    /**
     * Shows a message box.
     *
     * @param title The title of the message box
     * @param msg   The message to display
     */
    @Override
    public void showFatalMessageBox(final String title, final String msg) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Display.destroy();
                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setAlwaysOnTop(true);
                frame.setVisible(true);
                frame.requestFocusInWindow();
                JOptionPane.showConfirmDialog(frame, msg, title,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                frame.dispose();
                exit();
            }
        });
    }

    /**
     * Closes the application, using the platforms native method.
     */
    @Override
    public void exit() {
        Gdx.app.exit();
    }

    /**
     * Finds the game folder from Steam on desktop platforms.
     */
    @Override
    public File findGameFolder() {
        return SteamFinder.getSteamPath(this);
    }

    /**
     * Verifies if this game was purchased via Steam.
     *
     * @return If the game has been purchased.
     */
    @Override
    public boolean verifySteam() {
        // If we are at this point, this should return true.
        return true;
    }

    /**
     * Ticks any system-exclusive processes.
     */
    @Override
    public void tick() {
        if (EscapistsRuntime.DEBUG && debugFrame != null) {
            try {
                debugFrame.tick(game);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //SteamAPI.runCallbacks();
    }

    @Override
    public void hideMouse() {
        try {
            Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
            Mouse.setNativeCursor(emptyCursor);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DebugFrame getDebugFrame() {
        return debugFrame;
    }

    @Override
    public File getSaveLocation() {
        if (isWindows()) {
            if (System.getProperty("os.name").toLowerCase().contains("xp")) {
                File file = new File(System.getProperty("user.home") + File.separator + "My Documents");
                if (file.exists()) {
                    return file;
                }
            }

            File file = new File(System.getProperty("user.home") + File.separator + "Documents");
            if (!file.exists()) {
                file = new File(System.getProperty("user.home") + File.separator + "My Documents");
            }
            return file;
        }
        return new File(System.getProperty("user.home"));
    }

    @Override
    public File getCacheLocation() {
        return getSaveLocation();
    }

    @Override
    public String getStorageName() {
        return "same directory as application, or a standard Steam install location";
    }

    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public void dialog(String s) {
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.requestFocusInWindow();
        JOptionPane.showMessageDialog(frame, s, "Escapists Runtime",
                JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }
}
