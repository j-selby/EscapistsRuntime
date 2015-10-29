package net.jselby.escapists.game;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.chunks.Layers;
import org.mini2Dx.core.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * The layer is a index within the screen that dictates in what order objects
 * should draw.
 *
 * @author j_selby
 */
public class Layer {
    private final List<ObjectInstance> objects;
    private final String name;
    private long flags;

    public Layer(EscapistsRuntime runtime, Scene parent, int index, Layers.Layer layerDefinition) {
        this.name = layerDefinition.name;
        this.flags = layerDefinition.flags;

        // Grab our objects
        objects = new ArrayList<ObjectInstance>();
        for (ObjectInstance child : parent.getObjects()) {
            if (child.getLayerID() == index) {
                objects.add(child);
            }
        }
    }

    public void tick(EscapistsGame game) {
        for (ObjectInstance instance : objects) {
            instance.tick(game);
        }
    }

    public void draw(EscapistsGame game, Graphics g) {
        for (ObjectInstance instance : objects) {
            instance.draw(game, g);
        }
    }

    /**
     * Returns the name of this layer.
     * @return A String name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the flags of this layer.
     * TODO: Return a set of flags, rather then a integer
     * @return A raw flag.
     */
    public long getFlags() {
        return flags;
    }

    /**
     * Returns if this layer is currently visible.
     * @return A boolean representing this objects visibility.
     */
    public boolean isVisible() {
        return ((getFlags() >> 17) & 1) == 0;
    }
}
