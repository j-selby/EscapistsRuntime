package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Group functions handle group related tasks - enabling, disabling, etc.
 */
public class Groups extends FunctionCollection {
    @Condition(subId = -1, id = -23)
    public boolean OnGroupActivation() {
        return scope.getScene().wasGroupJustActivated(scope.getGroupStack().peek());
    }

    @Condition(subId = -1, id = -12)
    public boolean GroupActivated(int id) {
        return scope.getScene().getActiveGroups()[id];
    }

    @Condition(subId = -1, id = -10)
    public boolean GroupStart(int id) {
        scope.getGroupStack().push(id);
        return true;
    }

    @Condition(subId = -1, id = -11)
    public boolean GroupEnd(int id) {
        if (scope.getGroupStack().peek() == id) {
            scope.getGroupStack().pop();
        } else {
            System.err.println("Stack conflict: " + id);
        }
        return true;
    }

    @Action(subId = -1, id = 6)
    public void ActivateGroup(int id) {
        scope.getScene().activateGroup(id);
    }

    @Action(subId = -1, id = 7)
    public void DeactivateGroup(int id) {
        scope.getScene().disableGroup(id);
    }
}
