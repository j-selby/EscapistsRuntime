package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Basic logic functions.
 */
public class Logic extends FunctionCollection {
    @Action(subId = -1, id = 0)
    public void Skip() {
    }

    @Condition(subId = -1, id = -1)
    public boolean Always() {
        return true;
    }

    @Condition(subId = -1, id = -2)
    public boolean Never() {
        return false;
    }

    @Condition(subId = -1, id = -7)
    public boolean NotAlways() {
        // TODO: Who the fuck wrote this shit?
        return false;
    }

    // Special command, not actually invoked at runtime
    @Condition(subId = -1, id = -24)
    public boolean OrFiltered() {
        return true;
    }

    // Special command, not actually invoked at runtime
    @Condition(subId = 2, id = -27)
    public boolean ElseIf(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = -1, id = -6, hasInstanceRef = true, successCallback = "OnceFinalize")
    public boolean Once(int id) {
        return !scope.getScene().getVariables().containsKey("_env_once_" + id);
    }

    public void OnceFinalize(int id) {
        String key = "_env_once_" + id;
        if (!scope.getScene().getVariables().containsKey(key)) {
            scope.getScene().getVariables().put(key, true);
        }
    }

    @Condition(subId = -1, id = -16)
    public boolean OnLoop(String loopName) {
        return scope.getScene().getActiveLoops().containsKey(loopName);
    }

    @Action(subId = 2, id = 35)
    public void FlagOn(int value) {
        String key = "_env_flag_" + value;
        scope.getScene().getVariables().put(key, true);
    }

    @Action(subId = 2, id = 36)
    public void FlagOff(int value) {
        String key = "_env_flag_" + value;
        scope.getScene().getVariables().put(key, false);
    }

    @Condition(subId = 2, id = -24)
    public boolean IsFlagOff(int value) {
        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return !((Boolean) scope.getScene().getVariables().get(key));
        } else {
            return true;
        }
    }

    @Condition(subId = 2, id = -25)
    public boolean IsFlagOn(int value) {
        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    @Condition(subId = 2, id = -34)
    public boolean PickRandom(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Action(subId = -1, id = 14)
    public void StartLoop(String name, int times) {
        scope.getScene().activateLoop(name, times);
    }

}
