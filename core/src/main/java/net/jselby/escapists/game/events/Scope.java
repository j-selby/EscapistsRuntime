package net.jselby.escapists.game.events;

import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Scope is the scope in which a condition/action can reach to.
 */
public class Scope extends ActionFunctions {
    private final EscapistsGame game;
    private final Scene scene;
    private final List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
    private final Stack<Integer> groupStack = new Stack<Integer>();

    public Scope(EscapistsGame game, Scene scene) {
        this.scope = this;
        this.game = game;
        this.scene = scene;
    }

    public Scope withObjects(int id) {
        objects.clear();
        for (ObjectInstance instance : scene.getObjects()) {
            if (instance.getObjectInfo() == id) {
                objects.add(instance);
            }
        }
        return this;
    }

    public ObjectInstance[] peekAtObjects() {
        return objects.toArray(new ObjectInstance[objects.size()]);
    }

    protected ObjectInstance[] getObjects() {
        ObjectInstance[] dump = objects.toArray(new ObjectInstance[objects.size()]);
        objects.clear();
        return dump;
    }

    public Scene getScene() {
        return scene;
    }

    public EscapistsGame getGame() {
        return game;
    }

    public void addObjectToScope(ObjectInstance instance) {
        objects.add(instance);
    }

    public Stack<Integer> getGroupStack() {
        return groupStack;
    }

}
