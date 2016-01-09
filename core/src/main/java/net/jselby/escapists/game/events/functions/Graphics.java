package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;
import net.jselby.escapists.game.objects.Text;

/**
 * Graphics functions handle animations, frames, directions and other visual-tweaks.
 */
public class Graphics extends FunctionCollection {
    @Action(subId = 2, id = 17)
    public void SetAnimation(int newAnimation) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setAnimation(newAnimation);
        }
    }

    @Action(subId = 2, id = 40)
    public void SetAnimationFrame(int frame) {
        if (frame < 0) {
            throw new IllegalArgumentException("Bad frame: " + frame);
        }

        for (ObjectInstance object : scope.getObjects()) {
            object.setAnimationFrame(frame);
        }
    }

    @Action(subId = 2, id = 15)
    public void StopAnimation() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setAnimation(0);
        }
    }

    @Action(subId = 3, id = 83)
    public void SetColor(int value) {
        // TODO: Set color
    }

    @Action(subId = 3, id = 56)
    public void SetFontColor(int value) {
        // TODO: Set color
    }

    @Action(subId = 2, id = 65)
    public void SetAlphaCoefficient(int value) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setImageAlpha(256 - value);
        }
    }

    @Action(subId = 3, id = 52)
    public void SetBold(int value) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setBold(value == 1);
        }
    }

    @Action(subId = 3, id = 88)
    public void SetString(String value) {
        for (ObjectInstance object : scope.getObjects()) {
            if (object instanceof Text) {
                ((Text) object).setString(value);
            }
        }
    }

    @Action(subId = 2, id = 23)
    public void SetDirection(int newDir) {
        // TODO: Better Animation implementation that supports this
        //System.out.println("STUB: Direction == " + newDir);
    }

    @Condition(subId = 2, id = -8)
    public boolean FacingInDirection(int direction) {
        // TODO: Directions
        return true;
    }

    @Condition(subId = 2, id = -3)
    public boolean AnimationPlaying(int num) {
        ObjectInstance[] objects = scope.getObjects();
        if (objects.length == 0) {
            return false;
        }
        for (ObjectInstance instance : objects) {
            if (instance.getAnimation() != num) {
                return false;
            }
        }
        return true;
    }

    @Condition(subId = 2, id = -1)
    public boolean AnimationFrame(int num) {
        // TODO: Proper animation implementation
        return false;
    }

    @Condition(subId = 2, id = -2)
    public boolean AnimationFinished(int num) {
        // TODO: Proper animation implementation
        return false;
    }

    @Condition(subId = 2, id = -7)
    public boolean MovementStopped(int id, int value) {
        // TODO: Unknown value
        return true;
    }
}
