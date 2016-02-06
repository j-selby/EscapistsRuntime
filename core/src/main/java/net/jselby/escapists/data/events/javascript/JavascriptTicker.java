package net.jselby.escapists.data.events.javascript;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.EventTicker;
import net.jselby.escapists.game.Scene;
import net.jselby.escapists.game.events.FunctionCollection;
import net.jselby.escapists.game.events.Scope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A JavascriptTicker runs the event stack through a Javascript evaluator (Rhino).
 */
public class JavascriptTicker extends EventTicker {
    private final Context jsContext;
    private final ScriptableObject jsScriptable;
    private Script jsScript;

    public JavascriptTicker(Scene scene, Scope scope, Events events) {
        super(events);

        String javascript = new EventCompiler().compileEvents(events);

        // Inject mods
        /*for (String mod : scope.getGame().getMods()) {
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

                javascript = "";
                for (String line : lines) {
                    javascript += line + "\n";
                }
            } catch (Exception e) {
                scope.getGame().fatalPrompt("Error while parsing mod: " + e.getLocalizedMessage());
            }
        }*/

        try {
            File target;
            if (EscapistsRuntime.DEBUG) {
                target = new File("frame_" + scene.getName() + ".js");
            } else {
                File parent = new File(scope.getGame().getPlatformUtils().getSaveLocation(),
                        "The Escapists" + File.separator + "source");
                if (!parent.exists() && !parent.mkdirs()) {
                    throw new IOException("Failed to create source directories.");
                }
                target = new File(parent, "frame_" + scene.getName() + ".js");
            }

            FileOutputStream out = new FileOutputStream(
                    target);
            out.write(javascript.replace("\r\n", "\n").replace("\n", "\r\n").getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Compress Javascript
        if (EscapistsRuntime.USING_CLOSURE) {
            javascript = new EventCompiler().closureJS(javascript);
        }

        jsContext = Context.enter();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            // Android doesn't support Desktop-style bytecode optimization.
            jsContext.setOptimizationLevel(-1);
        } else {
            jsContext.setOptimizationLevel(9);
        }
        jsScriptable = jsContext.initStandardObjects();

        // Convert our functions
        for (Class<? extends FunctionCollection> collection
                : EscapistsRuntime.getRuntime().getRegister().getProviders()) {
            FunctionCollection instance;
            try {
                instance = collection.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            instance.scope = scope;

            Object wrappedOut = Context.javaToJS(instance, jsScriptable);
            ScriptableObject.putProperty(jsScriptable, instance.getClass().getSimpleName(), wrappedOut);
        }

        Object wrappedOut = Context.javaToJS(scope, jsScriptable);
        ScriptableObject.putProperty(jsScriptable, "env", wrappedOut);


        try {
            jsScript = jsContext.compileString(javascript, "frame_" + scene.getName() + ".js", 1, null);
        } catch (EvaluatorException e) {
            if (e.getMessage().contains("Program too complex")) {
                // Try again
                System.err.println("Rhino threw a program too complex error, attempting to run in interpreted mode.");
                jsContext.setOptimizationLevel(-1);
                try {
                    jsScript = jsContext.compileString(javascript, "frame_" + scene.getName() + ".js", 1, null);
                } catch (EvaluatorException e1) {
                    e1.printStackTrace();
                    scope.getGame().fatalPrompt("Compile error: " + e1.getLocalizedMessage());
                }
            } else {
                e.printStackTrace();
                scope.getGame().fatalPrompt("Compile error: " + e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void tick(Scope scope) {
        jsScript.exec(jsContext, jsScriptable);
    }
}
