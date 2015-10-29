package net.jselby.escapists.game.objects;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

/**
 * A backdrop is a static image in the background/foreground.
 */
public class QuickBackdrop extends ObjectInstance {
    private final Sprite image;

    public QuickBackdrop(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        net.jselby.escapists.data.objects.QuickBackdrop data
                = (net.jselby.escapists.data.objects.QuickBackdrop) definition.properties.properties;
        image = EscapistsRuntime.getRuntime()
                .getApplication().images[
                data.shape.image + 1];
    }

    @Override
    public void tick(EscapistsGame container) {

    }

    @Override
    public void draw(EscapistsGame container, Graphics g) {
        if (!isVisible()) {
            return;
        }

        if (image != null) {
            g.drawSprite(image, getX(), getY());
        }
    }
}
