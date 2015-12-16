package net.jselby.escapists.game.desktop;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;
import net.jselby.escapists.PlatformUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Cursor;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PlatformUtils implementation for Desktop platforms.
 */
public class DesktopPlatformUtils extends PlatformUtils {
    private SteamUser user;
    private SteamID steamId;
    private SteamID ownerSteamID;

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
        Display.destroy();
        System.exit(1);
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
    public File getSaveLocation() {
        if (isWindows()) {
            File file = new File(System.getProperty("user.home") + File.separator + "My Documents");
            if (!file.exists()) {
                file = new File(System.getProperty("user.home") + File.separator + "Documents");
            }
            return file;
        }
        return new File(System.getProperty("user.home"));
    }

    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public void dialog(String s) {
        JOptionPane.showMessageDialog(null, s, "Escapists Runtime", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Escaped!");
    }
}
