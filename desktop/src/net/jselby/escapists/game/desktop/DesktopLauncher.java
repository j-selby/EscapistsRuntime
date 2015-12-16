package net.jselby.escapists.game.desktop;

import com.badlogic.gdx.Files;
import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import net.jselby.escapists.game.EscapistsGame;

import java.io.IOException;

public class DesktopLauncher {
    public static void main(String[] args) {
        DesktopMini2DxConfig config = new DesktopMini2DxConfig(EscapistsGame.GAME_IDENTIFIER);
        config.title = "The Escapists";
        config.vSyncEnabled = true;
        config.width = 944;
        config.height = 684;
        config.samples = 1;
        config.addIcon("icon_16.png", Files.FileType.Internal);
        config.addIcon("icon_32.png", Files.FileType.Internal);
        config.addIcon("icon_48.png", Files.FileType.Internal);
        config.addIcon("icon_256.png", Files.FileType.Internal);
		new DesktopMini2DxGame(new EscapistsGame(new DesktopPlatformUtils()), config);
	}
}
