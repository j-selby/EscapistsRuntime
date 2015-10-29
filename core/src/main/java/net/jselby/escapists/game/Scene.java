package net.jselby.escapists.game;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.Frame;
import net.jselby.escapists.data.chunks.Layers;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.objects.Active;
import net.jselby.escapists.game.objects.Backdrop;
import net.jselby.escapists.game.objects.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene is a particular set of layouts. This can be used for levels, etc.
 *
 * @author j_selby
 */
public class Scene {
    private final EscapistsRuntime runtime;

    // Raw type
    private final ObjectInstances.ObjectInstance[] objectInstanceDefs;
    private final Layers.Layer[] layerDefinitions;

    // Parsed type
    private List<ObjectInstance> instances;
    private String name;
    private Color background;
    private Layer[] layers;

    public Scene(EscapistsRuntime runtime, Frame frame) {
        this.runtime = runtime;
        name = frame.name;
        background = frame.background;
        objectInstanceDefs = frame.objects.instances;
        layerDefinitions = frame.layers.layers;
        create();
    }

    /**
     * Resets this scene, and creates a new set of ObjectInstances, ready to be used from fresh.
     */
    private void create() {
        instances = new ArrayList<ObjectInstance>();

        // Create new object instances from their data equivalents
        for (ObjectInstances.ObjectInstance originalDef : objectInstanceDefs) {
            // Find the stats for this object
            ObjectDefinition objectInfo = runtime.getApplication().objectDefs[originalDef.objectInfo];
            ObjectInstance newInstance = null;

            if (objectInfo == null || objectInfo.properties == null || objectInfo.properties.objectType == null) {
                continue;
            }

            switch(objectInfo.properties.objectType) {
                case Player:
                    break;
                case Keyboard:
                    break;
                case Create:
                    break;
                case Timer:
                    break;
                case Game:
                    break;
                case Speaker:
                    break;
                case System:
                    break;
                case QuickBackdrop:
                    break;
                case Backdrop:
                    newInstance = new Backdrop(objectInfo, originalDef);
                    break;
                case Active:
                    newInstance = new Active(objectInfo, originalDef);
                    break;
                case Text:
                    newInstance = new Text(objectInfo, originalDef);
                    break;
                case Question:
                    break;
                case Score:
                    break;
                case Lives:
                    break;
                case Counter:
                    break;
                case RTF:
                    break;
                case SubApplication:
                    break;
            }

            if (newInstance == null) {
                //System.err.printf("Bad object: %s (Type: %s).\n", objectInfo, objectInfo.properties.objectType);
                continue;
            }

            instances.add(newInstance);
        }

        // Create layers
        layers = new Layer[layerDefinitions.length];
        for (int i = 0; i < layerDefinitions.length; i++) {
            layers[i] = new Layer(runtime, this, i, layerDefinitions[i]);
        }
    }

    /**
     * Returns the currently allocated objects within this scene.
     * @return A object array
     */
    public ObjectInstance[] getObjects() {
        return instances.toArray(new ObjectInstance[instances.size()]);
    }

    /**
     * Returns the name of the scene.
     * @return The name of the scene.
     */
    public String getName() {
        return name;
    }

    public Color getBackground() {
        return background;
    }

    public Layer[] getLayers() {
        return layers;
    }
}
