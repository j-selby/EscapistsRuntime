package net.jselby.escapists.game;

import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ObjectInstances;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A instance of an object within the world.
 *
 * @author j_selby
 */
public abstract class ObjectInstance {
    private final String name;
    private final int id;
    private final int layerId;

    private final int x;
    private final int y;

    public ObjectInstance(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        this.name = definition.name;
        this.id = instance.handle;
        this.layerId = instance.layer;
        this.x = instance.x;
        this.y = instance.y;
    }

    /**
     * Returns the ID of this object.
     * @return An object ID
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the name of this object.
     * @return A String value
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the layer that this object exists on. Should not change post init.
     * @return The layer ID of this object.
     */
    public int getLayerID() {
        return layerId;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**
     * Ticks this object, accepting input and responding accordingly.
     * @param container The container to poll information from.
     */
    public abstract void tick(GameContainer container);

    /**
     * Draws this object onto the screen.
     * @param container The container to poll information from.
     * @param g The graphics instance to draw stuff onto.
     */
    public abstract void draw(GameContainer container, Graphics g);
}
