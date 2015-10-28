package net.jselby.escapists.game.objects;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.ObjectInstance;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A backdrop is a static image in the background/foreground.
 */
public class Backdrop extends ObjectInstance {
    private final Image image;

    public Backdrop(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        image = EscapistsRuntime.getRuntime()
                .getApplication().images[
                ((net.jselby.escapists.data.objects.Backdrop) definition.properties.properties).image];
    }

    @Override
    public void tick(GameContainer container) {

    }

    @Override
    public void draw(GameContainer container, Graphics g) {
        if (image != null) {
            g.drawImage(image, getX(), getY());
        }
    }
}
