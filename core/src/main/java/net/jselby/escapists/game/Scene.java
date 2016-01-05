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
import org.mini2Dx.core.graphics.Graphics;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
    private Map<Integer, Boolean> groupJustActivated = new HashMap<Integer, Boolean>();
    private Map<String, Integer> loops = new HashMap<String, Integer>();
    private Map<String, Integer> nextLoops = new HashMap<String, Integer>();
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
        groupJustActivated.clear();
        loops.clear();

        // Check for event groups
        for (Events.EventGroup group : events.groups) {
            // Check for groups
            if (group.conditions.length != 0 && group.conditions[0].name != null
                    && group.conditions[0].name.equalsIgnoreCase("GroupStart")) {
                ParameterValue.Group selectedGroup = ((ParameterValue.Group) group.conditions[0].items[0].value);
                groupActivated.put(selectedGroup.id, ((selectedGroup.flags & 1) == 0));
                groupJustActivated.put(selectedGroup.id, ((selectedGroup.flags & 1) == 0));
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
        String javascript = events.toJS();

        // Inject mods
        for (String mod : scope.getGame().getMods()) {
            try {
                String[] lines = javascript.split("\n");

                boolean isScene = false;
                for (String line : mod.split("\n")) {
                    if (line.startsWith(":")) { // Set scene
                        isScene = line.substring(1).trim().equalsIgnoreCase(getName().trim());
                    } else if (isScene) {
                        if (line.startsWith("@")) { // Swap
                            int lineId = Integer.parseInt(line.split(":")[0].substring(1)) - 1;
                            String jsSwap = line.substring(line.indexOf(":") + 1);

                            lines[lineId] = jsSwap;
                        } else if (line.startsWith(">")) { // Insert before line
                            int lineId = Integer.parseInt(line.split(":")[0].substring(1)) - 1;
                            String jsLine = line.substring(line.indexOf(":") + 1);

                            String[] pre = new String[lineId];
                            String[] post = new String[lines.length - lineId];
                            System.arraycopy(lines, 0, pre, 0, pre.length);
                            System.arraycopy(lines, lineId, post, 0, post.length);
                            lines = new String[lines.length + 1];
                            lines[lineId] = jsLine;
                            System.arraycopy(pre, 0, lines, 0, pre.length);
                            System.arraycopy(post, 0, lines, pre.length + 1, post.length);
                        }
                    }
                }

                StringBuilder javascriptCP = new StringBuilder();
                for (String line : lines) {
                    javascriptCP.append(line).append("\n");
                }
                javascript = javascriptCP.toString();
            } catch (Exception e) {
                scope.getGame().fatalPrompt("Error while parsing mod: " + e.getLocalizedMessage());
            }
        }

        try {
            FileOutputStream out = new FileOutputStream("frame_" + getName() + ".js");
            out.write(javascript.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsContext = Context.enter();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            // Android doesn't support Desktop-style bytecode optimization.
            jsContext.setOptimizationLevel(-1);
        } else {
            jsContext.setOptimizationLevel(9);
        }
        jsScriptable = jsContext.initStandardObjects();

        Object wrappedOut = Context.javaToJS(scope, jsScriptable);
        ScriptableObject.putProperty(jsScriptable, "env", wrappedOut);

        try {
            jsScript = jsContext.compileString(javascript, "frame_" + getName() + ".js", 1, null);
        } catch (EvaluatorException e) {
            e.printStackTrace();
            scope.getGame().fatalPrompt("Compile error: " + e.getLocalizedMessage());
            return;
        }

        frameCount = 0;
        startTime = System.currentTimeMillis();
    }

    public void init(EscapistsGame game) {
        create();
        tick(game);
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

        for (Map.Entry<String, Integer> value : nextLoops.entrySet()) {
            loops.put(value.getKey(), value.getValue());
        }
        nextLoops.clear();

        for (Map.Entry<Integer, Boolean> entry : groupJustActivated.entrySet()) {
            entry.setValue(false);
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

    public void activateGroup(int id) {
        groupActivated.put(id, true);
        groupJustActivated.put(id, true);
    }

    public boolean wasGroupJustActivated(int id) {
        return groupJustActivated.get(id);
    }

    public Map<String, Integer> getNextLoops() {
        return nextLoops;
    }
}
