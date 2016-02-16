package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.EscapistsRuntime
import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.game.events.Action
import net.jselby.escapists.game.events.Condition
import net.jselby.escapists.game.events.Scope
import java.util.*

/**
 * A interpreter event loop object is a EventLoop that is able to hold state.
 */
open class InterpreterActionConditionGroup(val group: Events.EventGroup?) {
    private val eventCallbacks = ArrayList<Pair<String, Array<Any>>>();

    open fun invokeIfPossible(interpreter: Interpreter, scope: Scope) {
        group!!
        eventCallbacks.clear();

        try {
            // Check conditions
            if (VERBOSE) println("-- Processing condition array: ${group.conditions.toMutableList()}");
            // Search for OR statement, so we don't instantly short-circuit the result
            var hasOR = false;
            for (condition in group.conditions) {
                if (condition.method.method.name.equals("OrFiltered")) {
                    hasOR = true;
                    break;
                }
            }

            var i = 0;
            while (i < group.conditions.size) {
                val condition = group.conditions[i];
                try {
                    if (condition.method.method.name.equals("OrFiltered")) {
                        if (i == 0) {
                            throw IllegalStateException("OR token placed at first position?!?!?")
                        }
                        // If we have successfully made it to this point, we are good to go
                        break;
                    }

                    var response: Pair<Any?, Array<Any>>;
                    val callback = (condition.method.checkAnnotation as Condition).successCallback;
                    if (EscapistsRuntime.getRuntime().application.objectDefs.size > condition.objectInfo) {
                        val objectDef = EscapistsRuntime.getRuntime().application.objectDefs[condition.objectInfo];
                        if (objectDef != null) {
                            response = interpreter.callMethod(condition.method, condition.items,
                                    condition.identifier, objectDef.handle.toInt());
                        } else {
                            response = interpreter.callMethod(condition.method, condition.items,
                                    condition.identifier);
                        }
                    } else {
                        response = interpreter.callMethod(condition.method, condition.items,
                                condition.identifier);
                    }

                    if ((response.first as Boolean) == condition.inverted()) {
                        // Comparison failed
                        if (hasOR) {
                            // We need to handle this directly
                            while (i < group.conditions.size) {
                                if (group.conditions[i].method.method.name.equals("OrFiltered")) {
                                    i++;
                                    break;
                                }
                                i++;
                            }

                            if (i == group.conditions.size) {
                                // We reached our last OR statement
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        if (callback.length > 0) {
                            eventCallbacks.add(Pair(callback, response.second));
                        }

                        i++;
                    }
                } catch (e : Exception) {
                    throw IllegalStateException("Interpreter error processing condition: $condition", e);
                }
            }

            callCallbacks(interpreter);

            if (VERBOSE) println("-- Processing action array: ${group.actions.toMutableList()}");
            for (action in group.actions) {
                try {
                    if (action.method == null) {
                        println("Action $action has no method!");
                        continue;
                    }

                    val callback = (action.method.checkAnnotation as Action).successCallback;

                    if (EscapistsRuntime.getRuntime().application.objectDefs.size > action.objectInfo) {
                        val objectDef = EscapistsRuntime.getRuntime().application.objectDefs[action.objectInfo]
                        if (objectDef != null) {
                            val response = interpreter.callMethod(action.method, action.items,
                                    action.objectInfo.toShort(), objectDef.handle.toInt())

                            if (callback.length > 0) {
                                eventCallbacks.add(Pair(callback, response.second));
                            }
                            continue;
                        }
                    }

                    val response = interpreter.callMethod(action.method, action.items, action.objectInfo.toShort())

                    if (callback.length > 0) {
                        eventCallbacks.add(Pair(callback, response.second));
                    }
                } catch (e : Exception) {
                    throw IllegalStateException("Interpreter error processing action: $action", e);
                }
            }

            callCallbacks(interpreter);
        } finally {
            interpreter.functions[0].scope.clearScopeObjects();
        }
    }

    private fun callCallbacks(interpreter: Interpreter) {
        // A callback is in the format class:method name
        for (callback in eventCallbacks) {
            val args = callback.first.split(":");

            val methodArgs = args[1].split("(");
            val targetFunctionCollectionName = args[0];

            val targetMethod = methodArgs[0];
            val targetMethodArgs =
                    if (methodArgs.size > 1)
                        methodArgs[1].split(")")[0].split(",")
                    else
                        emptyList<String>();

            val requiresArgs = if (targetMethodArgs.size > 0) targetMethodArgs[0].toBoolean() else false;

            // Get the collection
            var found = false;
            for (group in interpreter.functions) {
                if (group.javaClass.simpleName.equals(targetFunctionCollectionName)) {
                    // We found our group
                    for (method in group.javaClass.declaredMethods) {
                        if (method.name.equals(targetMethod)) {
                            if (requiresArgs) {
                                method.invoke(group, *(callback.second));
                            } else {
                                method.invoke(group);
                            }
                            found = true;
                            break;
                        }
                    }
                    break;
                }
            }

            if (!found) {
                throw IllegalStateException("Unable to find group $targetFunctionCollectionName for method $targetMethod");
            }
        }

        eventCallbacks.clear();
    }

    open fun runFastLoop(interpreter: Interpreter, scope: Scope, name: String) {
        // Check if we have this fastloop element, else skip
        var list : ArrayList<Any>? = null;
        for (condition in group!!.conditions) {
            if (condition.name.equals("OnLoop")) {
                if (list == null) {
                    list = ArrayList<Any>();
                }
                condition.items[0].add(interpreter, list);
                val conditionName : String = list[0] as String;
                if (name.equals(conditionName)) {
                    // We do!
                    invokeIfPossible(interpreter, scope);
                    return;
                }
                list.clear();
            }
        }
    }

    override fun toString(): String {
        if (group != null) {
            return "Action-ConditionGroup={children=${group.toString()}";
        }

        throw UnsupportedOperationException("implementer hasn't implemented toString()")
    }

    open fun getAsDebuggingString(): String {
        var events = "";
        events += "new statement\n";
        events += "\tconditions\n";
        for (condition in group!!.conditions) {
            events += "\t\t" + condition.toString() + "\n";
        }


        events += "\tactions\n";
        for (action in group.actions) {
            events += "\t\t" + action.toString() + "\n";
        }

        events += "end statement\n\n"

        return events;
    }
}