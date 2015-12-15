package net.jselby.escapists.game.events;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Scope is the scope in which a condition/action can reach to.
 */
public class Scope extends Actions {
    private EscapistsGame game;
    private Scene scene;
    private List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
    private int lastRef = -1;

    public Scope(EscapistsGame game, Scene scene) {
        this.scope = this;
        this.game = game;
        this.scene = scene;
    }

    public Scope withObjects(int id) {
        lastRef = id;
        objects.clear();
        for (ObjectInstance instance : scene.getObjects()) {
            if (instance.getObjectInfo() == id) {
                objects.add(instance);
            }
        }
        return this;
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
}
