package net.jselby.escapists.game.objects;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ImageBank;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import org.jetbrains.annotations.NotNull;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

/**
 * A backdrop is a static image in the background/foreground.
 */
public class Backdrop extends ObjectInstance {
    private final short imageBase;
    private ImageBank.ImageItem image;
    private Sprite imageValue;

    public Backdrop(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        assert definition.properties.getProperties() != null;

        imageBase = ((net.jselby.escapists.data.objects.Backdrop) definition.properties.getProperties()).image;
        image = EscapistsRuntime.getRuntime()
                .getApplication().images[imageBase + 1];

        assert image.getImage() != null;

        imageValue = image.getImage();
    }

    @Override
    public float getWidth() {
        return imageValue.getWidth();
    }

    @Override
    public float getHeight() {
        return imageValue.getHeight();
    }

    @Override
    public void tick(@NotNull EscapistsGame container) {
    }

    @Override
    public void draw(@NotNull EscapistsGame container, @NotNull Graphics g) {
        if (!isVisible()) {
            return;
        }

        if (image != null) {
            // TODO: Does this need {x,y}Hotspots?
            imageValue.setAlpha(((float) getImageAlpha()) / 256f);
            g.drawSprite(imageValue, getX(), getY());
            imageValue.setAlpha(1f);
        }
    }
}
