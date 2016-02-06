package net.jselby.escapists.data.events.javascript

//import com.google.javascript.jscomp.*
import net.jselby.escapists.EscapistsRuntime
import net.jselby.escapists.data.ObjectDefinition
import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.data.events.ParameterValue
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

        for (group in events.groups) {
            // Firstly, grab out loop statements, so we can insert them at their target.
            var isOutOfOrder = false;
            var validCondition : Events.Condition? = null;
            for (condition in group.conditions) {
                if (condition.name == null) {
                    throw IllegalArgumentException("Invalid name for function: " + compileCondition(scope, condition));
                }
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
                    parentLoops += "Groups.GroupActivated($groupStackElement) && ";
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

            output += "${indent}Groups.GroupStart(${item.id});\n";
            output += "${indent}if (Groups.GroupActivated(${item.id})) { " +
                    "// Flags: ${Integer.toBinaryString(item.flags)}, " +
                    "Name: ${item.name}\n";

            scope.increaseIndent();

            return output;
        } else if (group.conditions.size > 0 && group.conditions[0].name.equals("GroupEnd", ignoreCase = true)) {
            scope.decreaseIndent();
            indent = scope.getIndent();

            output += "${indent}}\n";
            output += "${indent}Groups.GroupEnd(${scope.groupStack.pop()});\n";

            return output;
        }

        // Compile conditions
        val conditions = compileConditions(scope, group.conditions);
        output += conditions;

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

                if (scope.loopFunctions[key] != null) {
                    output += "${indent}if (Logic.OnLoop($key)) {\n";
                    scope.increaseIndent();
                    indent = scope.getIndent();
                    output += indent;
                    for (item in scope.loopFunctions[key]!!) {
                        output += item.replace("\n", "\n$indent");
                    }
                    scope.decreaseIndent();
                    indent = scope.getIndent();
                    output += "\n$indent}\n";
                }

            }
        }

        if (conditions.length > 0) {
            scope.decreaseIndent();
            output += "${scope.getIndent()}}\n";
        }

        if (scope.requiresCleanup) {
            output += "${scope.getIndent()}env.clearScopeObjects();\n";
        }
        scope.requiresCleanup = false;

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
                + (if (objectDef == null) "null" + condition.objectType else objectDef.name) + "*/").trim();
        if (condition.method == null) {
            throw IllegalStateException("Invalid condition definition: " + condition.objectType + ":" + condition.num);
        }
        val objDeclaration = condition.method.parent.simpleName +
                "." + (if (id == 0) "" else ("withObjects($objName)."));
        val objMethod = (condition.name ?: "${condition.objectType}:${condition.num}").trim();
        val annotation = condition.method.checkAnnotation as net.jselby.escapists.game.events.Condition;

        val requiresContext = annotation.hasInstanceRef
        val requiresCondition = annotation.conditionRequired

        if (annotation.requiresScopeCleanup) {
            scope.requiresCleanup = true;
        }

        if (condition.inverted()) {
            output += "!";
        }

        if (annotation.syntax.length > 0) {
            output += annotation.syntax;
        } else {
            output += "$objDeclaration$objMethod(%args%)";
        }

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

        output = output.replace("%args%", args);

        if (!condition.inverted() && annotation.successCallback.length != 0) {
            scope.successCallbacks.add("${condition.method.parent.simpleName}.${annotation.successCallback}($args);");
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

        if (conditionsString.equals("true")) {
            return "";
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
        val classSelector = if (action.name == null) "env" else action.method.parent.simpleName;

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
            actionRef = "${action.method.method.name}";
            actionStart = "";
            actionClosing = "";
        }

        var params = "";
        var paramCount = 0

        for (param in action.items) {
            params += (if (paramCount != 0) ", " else "") + param.value;
            paramCount++;
        }

        return "$actionStart$classSelector.$actionSelector$actionRef($params);$actionClosing";
    }
}

class CompilerScope {
    private val INDENT_SIZE = 4;

    var indentValue = 0;

    var requiresCleanup = false;
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