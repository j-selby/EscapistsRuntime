package net.jselby.escapists.game;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.chunks.Frame;
import net.jselby.escapists.data.chunks.Layers;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.game.events.Scope;
import net.jselby.escapists.game.objects.Active;
import net.jselby.escapists.game.objects.Backdrop;
import net.jselby.escapists.game.objects.Text;
import org.mini2Dx.core.graphics.Graphics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    private final Events events;

    // Parsed type
    private List<ObjectInstance> instances;
    private String name;
    private int width;
    private int height;
    private Color background;
    private Layer[] layers;

    // Scripting
    public boolean firstFrame = true;
    private Map<String, Object> variables = new HashMap<String, Object>();
    private int frameCount;
    private long startTime;

    public Scene(EscapistsRuntime runtime, Frame frame) {
        this.runtime = runtime;
        name = frame.name;
        background = frame.background;
        objectInstanceDefs = frame.objects.instances;
        layerDefinitions = frame.layers.layers;
        events = frame.events;

        width = frame.width;
        height = frame.height;
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

        firstFrame = true;

        System.out.println(events.toJS());

        frameCount = 0;
        startTime = System.currentTimeMillis();
    }

    public void init(EscapistsGame game) {
        create();
    }

    public void tick(EscapistsGame game) {
        // Activate conditional objects
        Scope scope = new Scope(game, this);
        List<Object> args = new ArrayList<Object>();

        for (Events.EventGroup group : events.groups) {
            scope.objects.clear();

            boolean conditionsPassed = true;

            for (Events.Condition condition : group.conditions) {
                if (condition.name == null) {
                    continue;
                }

                if (condition.name.equalsIgnoreCase("NewGroup")
                        || condition.name.equalsIgnoreCase("GroupEnd")
                        || condition.name.equalsIgnoreCase("OnGroupActivation")
                        || condition.name.equalsIgnoreCase("GroupActivated")) {
                    // TODO: Groups
                    conditionsPassed = false;
                    continue;
                }

                if (condition.method == null) {
                    conditionsPassed = false;
                    //System.out.println("Condition method failed: " + condition);
                    continue;
                }

                args.clear();

                // Build args
                args.add(scope);
                args.add(condition);
                for (Events.Parameter parameter : condition.items) {
                    args.add(parameter.value);
                }

                try {
                    Boolean returnValue = (Boolean) condition.method.invoke(null, args.toArray());
                    if (returnValue == null || !returnValue) {
                        conditionsPassed = false;
                        break;
                    }
                } catch (Exception e) {
                    System.err.print("Condition execution error: ");
                    e.printStackTrace();
                    System.err.println("Within " + condition);
                }
            }

            if (!conditionsPassed) {
                continue;
            }

            // Yay, we can now execute our actions
            for (Events.Action action : group.actions) {
                if (action.name == null) {
                    continue;
                }

                if (action.method == null) {
                    //System.out.println("Action method failed: " + action);
                    continue;
                }

                args.clear();

                // Build args
                args.add(scope);
                args.add(action);
                for (Events.Parameter parameter : action.items) {
                    args.add(parameter.value);
                }

                try {
                    action.method.invoke(null, args.toArray());
                } catch (Exception e) {
                    System.err.print("Condition execution error: ");
                    e.printStackTrace();
                    System.err.println("Within " + action);
                }
            }
        }

        firstFrame = false;

        for (Layer layer : getLayers()) {
            if (!layer.isVisible()) { // "IsShow" flag
                continue;
            }

            layer.tick(game);

        }

        frameCount++;
    }

    public void draw(EscapistsGame game, Graphics g) {
        for (Layer layer : getLayers()) {
            if (!layer.isVisible()) { // "IsShow" flag
                continue;
            }

            layer.draw(game, g);
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public long getSceneStartTime() {
        return startTime;
    }
}
