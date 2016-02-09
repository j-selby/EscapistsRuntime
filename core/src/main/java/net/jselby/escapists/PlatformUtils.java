package net.jselby.escapists;

import java.io.File;

/**
 * Platform specific utilities.
 *
 * @author j_selby
 */
public abstract class PlatformUtils {
    /**
     * Shows a message box. Will terminate application after call.
     * @param title The title of the message box
     * @param msg The message to display
     */
    public abstract void showFatalMessageBox(String title, String msg);

    /**
     * Closes the application using the platforms native method.
     */
    public abstract void exit();

    /**
     * Finds the game folder from Steam on desktop platforms.
     */
    public abstract File findGameFolder();

    /**
     * Verifies if this game was purchased via Steam.
     *
     * @return If the game has been purchased.
     */
    public abstract boolean verifySteam();

    /**
     * Ticks any system-exclusive processes.
     */
    public abstract void tick();

    /**
     * Hides the mouse.
     */
    public abstract void hideMouse();

    /**
     * Gets the save location for this platform
     * @return A save location
     */
    public abstract File getSaveLocation();

    /**
     * Gets the cache location for this platform
     * @return A cache location
     */
    public abstract File getCacheLocation();

    /**
     * Gets the storage type name that we are saving to
     * @return A human readable storage type
     */
    public abstract String getStorageName();
}
