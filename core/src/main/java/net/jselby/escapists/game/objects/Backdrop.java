package net.jselby.escapists.game.objects;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ImageBank;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import org.mini2Dx.core.graphics.Graphics;

/**
 * A backdrop is a static image in the background/foreground.
 */
public class Backdrop extends ObjectInstance {
    private final ImageBank.ImageItem image;

    public Backdrop(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        image = EscapistsRuntime.getRuntime()
                .getApplication().images[
                ((net.jselby.escapists.data.objects.Backdrop) definition.properties.properties).image + 1];
    }

    @Override
    public float getWidth() {
        return image.image.getWidth();
    }

    @Override
    public float getHeight() {
        return image.image.getHeight();
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
            // TODO: Does this need {x,y}Hotspots?
            g.drawSprite(image.image, getX(), getY());
        }
    }
}
