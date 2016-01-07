package net.jselby.escapists.data.events

//import com.google.javascript.jscomp.*
import net.jselby.escapists.EscapistsRuntime
import net.jselby.escapists.data.ObjectDefinition
import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.game.events.Scope
import java.util.*

/**
 * The compiler converts raw event types to a Javascript representation,
 * ready to be parsed by Rhino.
 */
class EventCompiler {
    /**
     * Compiles a event stack to Javascript.
     */
    fun compileEvents(events : Events): String {
        var output = "";
        val scope = CompilerScope();

        // Firstly, grab out loop statements, so we can insert them at their target.
        for (group in events.groups) {
            var isOutOfOrder = false;
            var validCondition : Events.Condition? = null;
            for (condition in group.conditions) {
                if (condition.name.equals("OnLoop")) {
                    isOutOfOrder = true;
                    validCondition = condition;
                    break;
                }
            }

            if (isOutOfOrder) {
                var key = validCondition!!.items[0].value.toString();

                var oldIndent = scope.indentValue;
                scope.resetIndent();
                var loopcontent = compileEventGroup(scope, group).substring(4);

                var parentLoops = "";
                for (groupStackElement in scope.groupStack) {
                    parentLoops += "env.GroupActivated($groupStackElement) && ";
                }

                loopcontent = "if ($parentLoops$loopcontent";

                if (!scope.loopFunctions.containsKey(key)) {
                    scope.loopFunctions[key] = ArrayList();
                }
                scope.loopFunctions[key]!!.add(loopcontent);
                scope.indentValue = oldIndent;
            }

            output += compileEventGroup(scope, group);
        }
        return output;
    }

    fun closureJS(input : String) : String {
        /*var compiler = com.google.javascript.jscomp.Compiler();

        var options = CompilerOptions();
        CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(
                options);
        options.setWarningLevel(DiagnosticGroups.EXTERNS_VALIDATION, CheckLevel.OFF);

        // Build external refs
        var refs = "env = {";
        refs += "withObjects: function(count) {return this}";
        for (method in Scope::class.java.methods) {
            val params = method.parameterTypes.size;
            var paramMsg = "";
            for (i in 1..params) {
                if (i != 1) {
                    paramMsg += ",";
                }
                paramMsg += "arg$i";
            }
            refs += ",${method.name}: function($paramMsg){}";
        }
        refs += "};"

        compiler.compile(JSSourceFile.fromCode("externs.js", refs),
                JSSourceFile.fromCode("input.js", input), options);

        return compiler.toSource();*/
        return "";
    }

    private fun compileEventGroup(scope : CompilerScope, group: Events.EventGroup) : String {
        var indent = scope.getIndent();
        var output = "";

        // Check for special event groups first
        if (group.conditions.size > 0 && group.conditions[0].name.equals("GroupStart", ignoreCase = true)) {
            val item = (group.conditions[0].items[0].value as ParameterValue.Group);
            scope.groupStack.push(item.id);

            output += "${indent}env.GroupStart(${item.id});\n";
            output += "${indent}if (env.GroupActivated(${item.id})) { " +
                    "// Flags: ${Integer.toBinaryString(item.flags)}, " +
                    "Name: ${item.name}\n";

            scope.increaseIndent();

            return output;
        } else if (group.conditions.size > 0 && group.conditions[0].name.equals("GroupEnd", ignoreCase = true)) {
            scope.decreaseIndent();
            indent = scope.getIndent();

            output += "${indent}}\n";
            output += "${indent}env.GroupEnd(${scope.groupStack.pop()});\n";

            return output;
        }

        // Compile conditions
        output += compileConditions(scope, group.conditions);

        indent = scope.getIndent(); // Automatically increased by above function

        for (action in scope.successCallbacks) {
            output += "$indent$action\n";
        }
        scope.successCallbacks.clear();

        // Compile actions
        for (action in group.actions) {
            output += "$indent${compileAction(action)}\n";
            if (action.name != null && action.name.equals("StartLoop")) {
                var key = action.items[0].value.toString();
                println(key);

                if (scope.loopFunctions[key] != null) {
                    output += "${indent}if (env.OnLoop($key)) {\n";
                    scope.increaseIndent();
                    indent = scope.getIndent();
                    for (item in scope.loopFunctions[key]!!) {
                        output += "$indent${item.replace("\n", "\n$indent")}";
                    }
                    scope.decreaseIndent();
                    indent = scope.getIndent();
                    output += "\n$indent}\n";
                    output += "${indent}env.DecreaseLoop($key);\n";
                }

            }
        }

        scope.decreaseIndent();
        output += "${scope.getIndent()}}\n";

        return output;
    }

    private fun compileCondition(scope: CompilerScope, condition : Events.Condition) : String {
        var output = "";

        var objectDef: ObjectDefinition? = null;
        if (EscapistsRuntime.getRuntime().application.objectDefs.size > condition.objectInfo) {
            objectDef = EscapistsRuntime.getRuntime().application.objectDefs[condition.objectInfo];
        }

        val id = (if (objectDef == null) -1 else objectDef.handle).toInt();
        val objName = ("$id /*"
                + (if (objectDef == null) "null" + condition.objectInfo else objectDef.name) + "*/").trim();
        val objDeclaration = "env." + (if (id == 0) "" else ("withObjects($objName)."));
        val objMethod = (condition.name ?: "${condition.objectType}:${condition.num}").trim();
        val annotation = condition.method.second as net.jselby.escapists.game.events.Condition;

        val requiresContext = annotation.hasInstanceRef
        val requiresCondition = annotation.conditionRequired

        if (condition.inverted()) {
            output += "!";
        }

        output += objDeclaration + objMethod + "(";

        var paramCount = 0;
        var args = "";
        if (requiresContext) {
            args += condition.identifier;
            paramCount++;
        }

        for (param in condition.items) {
            if (requiresCondition && param.value is ParameterValue.ExpressionParameter) {
                args += (if (paramCount != 0) ", " else "") +
                        (param.value as ParameterValue.ExpressionParameter).comparison;
                paramCount++;
            }
            args += (if (paramCount != 0) ", " else "") + param.value.toString();
            // + ":" + param.code;//param.loader.name() + " " + param.name.toLowerCase();
            paramCount++;
        }

        output += "$args)"

        if (!condition.inverted() && annotation.successCallback.length != 0) {
            scope.successCallbacks.add("env.${annotation.successCallback}($args);");
        }

        return output;
    }

    private fun compileConditions(scope: CompilerScope, conditions: Array<out Events.Condition>): String {
        // Check for ORs within the group
        var needsBracedOR = false;
        if (conditions.size > 3) {
            for (condition in conditions) {
                if (condition.name != null
                        && condition.name.trim().equals("OrFiltered", ignoreCase = true)) {
                    needsBracedOR = true;
                    break;
                }
            }
        }

        var conditionsString = "";
        if (needsBracedOR) {
            conditionsString += "(";
        }

        var lastWasOR = false;
        var count = 0;
        for (condition in conditions) {
            if (condition.name == "OrFiltered") {
                conditionsString += (if (needsBracedOR) ")" else "") + " || " + (if (needsBracedOR) "(" else "");
                lastWasOR = true;
                continue;
            } else if (!lastWasOR && count > 0) {
                conditionsString += " && ";
            } else {
                lastWasOR = false;
            }

            conditionsString += compileCondition(scope, condition);
            count++;
        }

        if (needsBracedOR) {
            conditionsString += ")";
        }

        val indent = scope.getIndent();
        scope.increaseIndent();
        return "${indent}if ($conditionsString) {\n";
    }

    private fun compileAction(action: Events.Action): String {
        var objectDef: ObjectDefinition? = null
        if (EscapistsRuntime.getRuntime().application.objectDefs.size > action.objectInfo) {
            objectDef = EscapistsRuntime.getRuntime().application.objectDefs[action.objectInfo]
        }

        val actionHandle = (objectDef?.handle ?: -1).toInt()
        val actionSelector = if (actionHandle == 0) "" else ("withObjects($actionHandle /*" +
                "${(if (objectDef == null) "null" + action.objectInfo else objectDef.name)}" +
                (if (action.name == null) "* /" else "*/") + ").");

        val actionStart : String;
        val actionRef : String;
        val actionClosing : String;

        // Check if this is a valid action
        if (action.name == null) {
            actionStart = "/* ";
            actionRef = "${action.objectType}:${action.num}"
            actionClosing = " */";
        } else {
            // Resolve the annotation pair
            actionRef = "${action.method.first.name}";
            actionStart = "";
            actionClosing = "";
        }

        var params = "";
        var paramCount = 0

        for (param in action.items) {
            params += (if (paramCount != 0) ", " else "") + param.value;
            paramCount++;
        }

        return "${actionStart}env.$actionSelector$actionRef($params);$actionClosing";
    }
}

class CompilerScope {
    private val INDENT_SIZE = 4;

    var indentValue = 0;

    val groupStack : Stack<Int> = Stack();
    val successCallbacks = ArrayList<String>();
    val loopFunctions = HashMap<String, ArrayList<String>>();

    fun getIndent() : String {
        return " ".repeat(indentValue);
    }

    fun increaseIndent() {
        indentValue += INDENT_SIZE;
    }

    fun decreaseIndent() {
        if (indentValue == 0) {
            return;
        }
        indentValue -= INDENT_SIZE;
    }

    fun resetIndent() {
        indentValue = 0;
    }
}
