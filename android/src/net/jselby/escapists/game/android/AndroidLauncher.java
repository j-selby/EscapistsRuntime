package net.jselby.escapists.game.android;

import org.mini2Dx.android.AndroidMini2DxConfig;

import com.badlogic.gdx.backends.android.AndroidMini2DxGame;

import android.os.Bundle;

import net.jselby.escapists.game.EscapistsGame;

public class AndroidLauncher extends AndroidMini2DxGame {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidMini2DxConfig config = new AndroidMini2DxConfig(EscapistsGame.GAME_IDENTIFIER);
        config.targetFPS = 45;
        config.useImmersiveMode = true;
        initialize(new EscapistsGame(), config);
    }
}
