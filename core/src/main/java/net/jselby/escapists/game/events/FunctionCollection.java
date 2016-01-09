package net.jselby.escapists.game.events;

/**
 * Function collections extend this class to be provided with useful functions.
 */
public abstract class FunctionCollection {
    public Scope scope;

    public FunctionCollection withObjects(int id) {
        scope.withObjects(id);
        return this;
    }
}
