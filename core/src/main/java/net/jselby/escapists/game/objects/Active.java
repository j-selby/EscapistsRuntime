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
    private int frame = 0;

    private float width;
    private float height;
    private short xHotspot;
    private short yHotspot;

    public Active(ObjectDefinition definition,
                  ObjectInstances.ObjectInstance instance) {
        super(definition, instance);

        ObjectCommon common = ((ObjectCommon) definition.properties.getProperties());
        animations = common.animations;

        tick(null);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getScreenX() {
        return super.getX() - xHotspot;
    }

    @Override
    public float getScreenY() {
        return super.getY() - yHotspot;
    }

    @Override
    public void tick(EscapistsGame container) {
        // Calculate dimensions
        AnimationHeader.Animation animation = animations.loadedAnimations[getAnimation()];

        if (animation != null) {
            for (AnimationHeader.AnimationDirection dir
                    : animation.localDirections) {
                if (dir != null && dir.frames != null) {
                    frame = dir.frames[getAnimationFrame()];
                    break;
                }
            }
        }

        if (frame != 0) {
            ImageBank.ImageItem image = EscapistsRuntime.getRuntime().getApplication().images[frame + 1];
            if (image == null) {
                return;
            }
            width = image.getImage().getWidth();
            height = image.getImage().getHeight();
            xHotspot = image.getXHotspot();
            yHotspot = image.getYHotspot();
        }

        if (container == null) {
            return;
        }
        // TODO: Animation implementation
        //frame++;
    }

    @Override
    public void draw(EscapistsGame container, Graphics g) {
        if (!isVisible()) {
            return;
        }

        // TODO: Please no hack
        if (frame == 0) {
            return;
        }

        ImageBank.ImageItem image = EscapistsRuntime.getRuntime().getApplication().images[frame + 1];
        if (image == null) {
            return;
        }
        image.getImage().setAlpha(((float) getImageAlpha()) / 256f);
        g.drawSprite(image.getImage(), getScreenX(), getScreenY());
        image.getImage().setAlpha(1f);
    }
}
