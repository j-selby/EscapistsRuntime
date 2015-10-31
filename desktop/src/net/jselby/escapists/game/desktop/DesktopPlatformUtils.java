package net.jselby.escapists.game.desktop;

import com.badlogic.gdx.Gdx;
import net.jselby.escapists.PlatformUtils;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.io.File;

/**
 * PlatformUtils implementation for Desktop platforms.
 */
public class DesktopPlatformUtils extends PlatformUtils {
    public DesktopPlatformUtils() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
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
                JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
                exit();
            }
        });
    }

    /**
     * Closes the application, using the platforms native method.
     */
    @Override
    public void exit() {
        System.exit(1);
    }

    /**
     * Finds the game folder from Steam on desktop platforms.
     */
    @Override
    public File findGameFolder() {
        return SteamFinder.getSteamPath(this);
    }

    public void dialog(String s) {
        JOptionPane.showMessageDialog(null, s, "Escapists Runtime", JOptionPane.INFORMATION_MESSAGE);
    }
}
