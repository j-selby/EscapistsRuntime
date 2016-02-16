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
public class QuickBackdrop extends ObjectInstance {
    private ImageBank.ImageItem image;
    private final short imageBase;
    private final Sprite imageValue;

    public QuickBackdrop(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        net.jselby.escapists.data.objects.QuickBackdrop data
                = (net.jselby.escapists.data.objects.QuickBackdrop) definition.properties.getProperties();

        assert data != null;

        imageBase = data.shape.image;
        image = EscapistsRuntime.getRuntime()
                .getApplication().images[
                imageBase + 1];
        imageValue = image.getImage();
        assert imageValue != null;
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
