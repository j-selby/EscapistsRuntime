package net.jselby.escapists.game;

import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.data.objects.ObjectCommon;
import org.mini2Dx.core.graphics.Graphics;

/**
 * A instance of an object within the world.
 *
 * @author j_selby
 */
public abstract class ObjectInstance {
    private final String name;
    private final int id;
    private final int layerId;
    private final int objectInfo;

    private int x;
    private int y;

    private boolean isVisible = true;

    public ObjectInstance(ObjectDefinition definition, ObjectInstances.ObjectInstance instance) {
        this.name = definition.name;
        this.id = instance.getHandle();
        this.objectInfo = instance.getObjectInfo();
        this.layerId = instance.getLayer();
        this.x = instance.getX();
        this.y = instance.getY();

        if (definition.properties.isCommon()) {
            isVisible = ((ObjectCommon) definition.properties.getProperties()).isVisibleAtStart();
        }
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

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    /**
     * Returns the width of this object
     * @return A width argument
     */
    public abstract float getWidth();

    /**
     * Returns the height of this object
     * @return A height argument
     */
    public abstract float getHeight();

    /**
     * Ticks this object, accepting input and responding accordingly.
     * @param container The container to poll information from.
     */
    public abstract void tick(EscapistsGame container);

    /**
     * Draws this object onto the screen.
     * @param container The container to poll information from.
     * @param g The graphics instance to draw stuff onto.
     */
    public abstract void draw(EscapistsGame container, Graphics g);

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Returns this Object's ObjectInfo information.
     * @return A ObjectInfo handle.
     */
    public int getObjectInfo() {
        return objectInfo;
    }

}
