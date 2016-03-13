package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Timers allow for comparisons against the system clock.
 */
public class Timers extends FunctionCollection {
    @Condition(subId = -3, id = -1)
    public boolean StartOfFrame() {
        return scope.getScene().firstFrame;
    }

    @Condition(subId = -4, id = -4, hasInstanceRef = true)
    public boolean Every(int id, int every) {
        // TODO: Proper implementation of this
        String key = "_env_every_" + every;
        if (!scope.getScene().getVariables().containsKey(key)) {
            scope.getScene().getVariables().put(key, System.currentTimeMillis());
        }

        // Check if it has updated
        long currentTime = System.currentTimeMillis();
        long lastTime = (Long) scope.getScene().getVariables().get(key);
        long diff = currentTime - lastTime;
        if (diff > every) {
            scope.getScene().getVariables().put(key, currentTime);
            return true;
        }

        return false;
    }

    @Condition(subId = -4, id = -8, hasInstanceRef = true)
    public boolean EveryWithUnknown(int id, int every, int unknown) {
        // TODO: Find out what "unknown" does
        return Every(id, every);
    }

    @Condition(subId = -4, id = -7)
    public boolean TimerEquals(int value, int repeat/*?*/) {
        //int currentTimer = (int) (scope.getScene().getFrameCount() * (1f / 45f) * 1000);
        // Convert to frame
        value /= (1f / 45f) * 1000;
        return value == scope.getScene().getFrameCount();
        //System.out.println(value + ":" + currentTimer);
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) == value;
    }

    @Condition(subId = -4, id = -3)
    public boolean TimerEquals(int value) {
        //int currentTimer = (int) (scope.getScene().getFrameCount() * (1f / 45f) * 1000);
        // Convert to frame
        value /= (1f / 45f) * 1000;
        return value == scope.getScene().getFrameCount();
        //System.out.println(value + ":" + currentTimer);
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) == value;
    }

    @Condition(subId = -4, id = -2)
    public boolean TimerLess(int value, int repeat/*?*/) {
        value /= (1f / 45f) * 1000;
        return value < scope.getScene().getFrameCount();
    }

    @Condition(subId = -4, id = -1)
    public boolean TimerGreater(int value) {
        value /= (1f / 45f) * 1000;
        return value > scope.getScene().getFrameCount();
    }
}
