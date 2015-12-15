package net.jselby.escapists.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.chunks.Frame;
import net.jselby.escapists.data.chunks.Layers;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.data.events.ParameterValue;
import net.jselby.escapists.game.events.Scope;
import net.jselby.escapists.game.objects.Active;
import net.jselby.escapists.game.objects.Backdrop;
import net.jselby.escapists.game.objects.QuickBackdrop;
import net.jselby.escapists.game.objects.Text;
import org.mini2Dx.core.graphics.Graphics;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Integer, Boolean> groupActivated = new HashMap<Integer, Boolean>();
    private Map<String, Integer> loops = new HashMap<String, Integer>();
    private int frameCount;
    private long startTime;

    private Scope scope;
    private List<Object> args = new ArrayList<Object>();

    private Context jsContext;
    private ScriptableObject jsScriptable;
    private Script jsScript;

    public Scene(EscapistsRuntime runtime, Frame frame, EscapistsGame game) {
        this.runtime = runtime;
        assert frame != null;
        name = frame.getName();
        background = frame.getBackground();
        objectInstanceDefs = frame.getObjects().instances;
        layerDefinitions = frame.getLayers().layers;
        events = frame.getEvents();

        width = frame.getWidth();
        height = frame.getHeight();

        scope = new Scope(game, this);
    }

    /**
     * Resets this scene, and creates a new set of ObjectInstances, ready to be used from fresh.
     */
    private void create() {
        instances = new ArrayList<ObjectInstance>();
        variables.clear();
        groupActivated.clear();
        loops.clear();

        // Check for event groups
        for (Events.EventGroup group : events.groups) {
            // Check for groups
            if (group.conditions.length != 0 && group.conditions[0].name != null
                    && group.conditions[0].name.equalsIgnoreCase("NewGroup")) {
                ParameterValue.Group selectedGroup = ((ParameterValue.Group) group.conditions[0].items[0].value);
                groupActivated.put(selectedGroup.id, ((selectedGroup.flags & 1) == 0));
            }
        }

        // Create new object instances from their data equivalents
        for (ObjectInstances.ObjectInstance originalDef : objectInstanceDefs) {
            // Find the stats for this object
            ObjectDefinition objectInfo = runtime.getApplication().objectDefs[originalDef.getObjectInfo()];

            ObjectInstance newInstance = objectInfo.createWorldInstance(originalDef);

            if (newInstance == null) {
                System.err.printf("Bad object: %s (Type: %s).\n", objectInfo, objectInfo.properties.getObjectType());
                continue;
            }

            instances.add(newInstance);
        }

        // Create layers
        layers = new Layer[layerDefinitions.length + 1]; // Additional layer for mouse etc
        for (int i = 0; i < layerDefinitions.length; i++) {
            layers[i] = new Layer(runtime, this, i, layerDefinitions[i]);
        }
        layers[layers.length - 1] = new Layer(runtime, this, layers.length - 1);

        firstFrame = true;

        // Prepare JS
        try {
            FileOutputStream out = new FileOutputStream("frame_" + getName() + ".js");
            out.write(events.toJS().getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsContext = Context.enter();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            // Android doesn't support Desktop-style bytecode optimization.
            jsContext.setOptimizationLevel(-1);
        }
        jsScriptable = jsContext.initStandardObjects();

        Object wrappedOut = Context.javaToJS(scope, jsScriptable);
        ScriptableObject.putProperty(jsScriptable, "env", wrappedOut);

        try {
            jsScript = jsContext.compileString(events.toJS(), "frame_" + getName() + ".js", 1, null);
        } catch (EvaluatorException e) {
            System.err.println("Compile error: " + e.getLocalizedMessage());
            throw e;
        }

        frameCount = 0;
        startTime = System.currentTimeMillis();
    }

    public void init(EscapistsGame game) {
        create();
    }

    public void tick(EscapistsGame game) {
        for (Layer layer : getLayers()) {
            if (!layer.isVisible()) { // "IsShow" flag
                continue;
            }

            layer.tick(game);

        }

        jsScript.exec(jsContext, jsScriptable);

        // TODO: Activate previous OnLoop instances (per condition countdown?)
        for (Object rawvalue : loops.entrySet().toArray()) {
            Map.Entry<String, Integer> value = (Map.Entry<String, Integer>) rawvalue;
            if (value.getValue() <= 1) {
                loops.remove(value.getKey());
            } else {
                value.setValue(value.getValue() - 1);
            }
        }

        firstFrame = false;
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
    public List<ObjectInstance> getObjects() {
        return instances;
    }

    /**
     * Returns the name of the scene.
     * @return The name of the scene.
     */
    public String getName() {
        return name.trim();
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

    public Map<Integer, Boolean> getActiveGroups() {
        return groupActivated;
    }

    public Map<String, Integer> getActiveLoops() {
        return loops;
    }
}
