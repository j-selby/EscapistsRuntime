package net.jselby.escapists.data.chunks;

/**
 * FadeOut is a fading out Transition.
 */
public class FadeOut extends Transition {
    @Override
    public boolean getFadeIn() {
        return false;
    }
}
