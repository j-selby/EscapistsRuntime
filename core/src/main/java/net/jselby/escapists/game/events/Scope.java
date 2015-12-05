package net.jselby.escapists.game.events;

import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.Expression;
import net.jselby.escapists.data.events.ParameterValue;
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
    public List<ObjectInstance> objects = new ArrayList<ObjectInstance>();

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

    public Scene getScene() {
        return scene;
    }

    public EscapistsGame getGame() {
        return game;
    }
}
