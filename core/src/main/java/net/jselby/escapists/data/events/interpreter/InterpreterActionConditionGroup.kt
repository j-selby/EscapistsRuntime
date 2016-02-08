package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.EscapistsRuntime
import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.game.events.Scope

/**
 * A interpreter event loop object is a EventLoop that is able to hold state.
 */
open class InterpreterActionConditionGroup(val group: Events.EventGroup?) {
    open fun invokeIfPossible(interpreter: Interpreter, scope: Scope) {
        group!!

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
            var response : Boolean;
            if (EscapistsRuntime.getRuntime().application.objectDefs.size > condition.objectInfo) {
                val objectDef = EscapistsRuntime.getRuntime().application.objectDefs[condition.objectInfo];
                if (objectDef != null) {
                    response = (interpreter.callMethod(condition.method, condition.items,
                            condition.identifier, objectDef.handle.toInt()) as Boolean);
                } else {
                    response = (interpreter.callMethod(condition.method, condition.items,
                            condition.identifier) as Boolean);
                }
            } else {
                response = (interpreter.callMethod(condition.method, condition.items,
                        condition.identifier) as Boolean);
            }

            if (response == condition.inverted()) {
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
                i++;
            }
        }

        if (VERBOSE) println("-- Processing action array: ${group.actions.toMutableList()}");
        for (action in group.actions) {
            if (action.method == null) {
                println("Action $action has no method!");
                continue;
            }

            if (EscapistsRuntime.getRuntime().application.objectDefs.size > action.objectInfo) {
                val objectDef = EscapistsRuntime.getRuntime().application.objectDefs[action.objectInfo]
                if (objectDef != null) {
                    interpreter.callMethod(action.method, action.items,
                            action.objectInfo.toShort(), objectDef.handle.toInt())
                    continue;
                }
            }

            interpreter.callMethod(action.method, action.items, action.objectInfo.toShort())
        }
    }

    override fun toString(): String {
        if (group != null) {
            return "Action-ConditionGroup={children=${group.toString()}";
        }

        throw UnsupportedOperationException("implementer hasn't implemented toString()")
    }
}