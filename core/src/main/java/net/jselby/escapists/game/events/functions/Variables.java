package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Variable functions store data within objects/the application.
 */
public class Variables extends FunctionCollection {
    @Action(subId = 36, id = 91)
    public void ClearObjectVarArray() {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().clear();
        }
    }

    @Action(subId = 36, id = 80)
    public void SetIntegerVar(String name, int val) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, val);
        }
    }

    @Action(subId = 36, id = 81)
    public void AddIntegerVar(String name, int by) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, (Integer) object.getVariables().get(name) + by);
        }
    }

    @Action(subId = 36, id = 82)
    public void SubtractIntegerVar(String name, int by) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, (Integer) object.getVariables().get(name) - by);
        }
    }

    @Action(subId = 36, id = 88)
    public void SetStringVar(String name, String val) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getVariables().put(name, val);
        }
    }

    @Action(subId = -1, id = 27)
    public void SetGlobalValueInt(int id, int value) {
        scope.getGame().globalInts.put(id, value);
    }

    @Action(subId = -1, id = 19)
    public void SetGlobalString(int id, String value) {
        scope.getGame().globalStrings.put(id, value);
    }

    @Action(subId = -1, id = 3)
    public void SetGlobalValue(int id, Number value) {
        scope.getGame().globalInts.put(id, value);
    }

}
