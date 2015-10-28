package net.jselby.escapists.game.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import net.jselby.escapists.game.EscapistsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(EscapistsGame.GAME_IDENTIFIER);
        config.foregroundFPS = 45;
        config.backgroundFPS = 45;
        config.targetFPS = 45; // Games framerate, don't blame me
        config.width = 944;
        config.height = 684;
		new DesktopMini2DxGame(new EscapistsGame(), config);
	}
}
