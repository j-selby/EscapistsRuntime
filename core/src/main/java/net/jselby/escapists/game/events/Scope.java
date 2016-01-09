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
public class Scope {
    private final EscapistsGame game;
    private final Scene scene;
    private final List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
    private final List<ObjectInstance> scopeObjects = new ArrayList<ObjectInstance>();
    private final Stack<Integer> groupStack = new Stack<Integer>();

    public Scope(EscapistsGame game, Scene scene) {
        this.game = game;
        this.scene = scene;
    }

    public Scope withObjects(int id) {
        objects.clear();

        boolean checkObjects = false;
        if (scopeObjects.size() > 0) {
            // An event has injected an object for us, lets see if its id matches
            for (ObjectInstance instance : scopeObjects) {
                if (instance.getObjectInfo() == id) {
                    checkObjects = true;
                    break;
                }
            }
        }

        for (ObjectInstance instance : scene.getObjects()) {
            if (instance.getObjectInfo() == id) {
                if (checkObjects) {
                    for (ObjectInstance checkInstance : scopeObjects) {
                        if (instance == checkInstance) {
                            objects.add(instance);
                            break;
                        }
                    }
                } else {
                    objects.add(instance);
                }
            }
        }
        return this;
    }

    public ObjectInstance[] peekAtObjects() {
        return objects.toArray(new ObjectInstance[objects.size()]);
    }

    public ObjectInstance[] getObjects() {
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
        scopeObjects.add(instance);
    }

    public void clearScopeObjects() {
        scopeObjects.clear();
    }

    public Stack<Integer> getGroupStack() {
        return groupStack;
    }

}
