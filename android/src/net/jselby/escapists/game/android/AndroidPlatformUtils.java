package net.jselby.escapists.game.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import net.jselby.escapists.PlatformUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;

/**
 * Android specific platform utilities implementation.
 */
public class AndroidPlatformUtils extends PlatformUtils {
    private AndroidLauncher parent;

    public AndroidPlatformUtils(AndroidLauncher parent) {
        this.parent = parent;
    }

    @Override
    public void showFatalMessageBox(final String title, final String msg) {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(parent.getContext())
                        .setTitle(title)
                        .setMessage(msg)
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                exit();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    /**
     * Closes the application, using the platforms native method.
     */
    @Override
    public void exit() {
        parent.finish();
    }

    /**
     * Finds the game folder from Steam on desktop platforms.
     */
    @Override
    public File findGameFolder() {
        throw new NotImplementedException("Steam cannot be detected on a mobile platform!");
    }
}
