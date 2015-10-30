package net.jselby.escapists.game.objects;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ImageBank;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.data.objects.ObjectCommon;
import net.jselby.escapists.data.objects.sections.AnimationHeader;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import org.mini2Dx.core.graphics.Graphics;

/**
 * A Active object is a object that is actively changing within the world.
 *
 * @author j_selby
 */
public class Active extends ObjectInstance {
    private final AnimationHeader animations;
    private int frame;

    public Active(ObjectDefinition definition,
                  ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        ObjectCommon common = ((ObjectCommon) definition.properties.properties);
        animations = common.animations;
    }

    @Override
    public void tick(EscapistsGame container) {
        // TODO: Animation implementation
        //frame++;
    }

    @Override
    public void draw(EscapistsGame container, Graphics g) {
        if (!isVisible()) {
            return;
        }

        // TODO: Please no hack

        // Find first applicable direction
        int frame = -1;
        AnimationHeader.Animation animation = animations.loadedAnimations[0];

        if (animation == null) {
            return;
        }

        for (AnimationHeader.AnimationDirection dir
                : animation.localDirections) {
            if (dir != null && dir.frames != null) {
                frame = dir.frames[0];
            }
        }

        if (frame == 0) {
            // Nope. Nope. Nope.
            return;
        }

        ImageBank.ImageItem image = EscapistsRuntime.getRuntime().getApplication().images[frame + 1];
        g.drawSprite(image.image, getX() - image.xHotspot, getY() - image.yHotspot);
    }
}
