package net.jselby.escapists.game.objects;

import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import org.jetbrains.annotations.NotNull;
import org.mini2Dx.core.graphics.Graphics;

/**
 * An counter object stores a integer.
 */
public class Counter extends ObjectInstance {
    public Counter(@NotNull ObjectDefinition definition, @NotNull ObjectInstances.ObjectInstance instance) {
        super(definition, instance);
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public void tick(@NotNull EscapistsGame container) {

    }

    @Override
    public void draw(@NotNull EscapistsGame container, @NotNull Graphics g) {

    }
}
