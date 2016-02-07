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
        println("-- Processing condition array: ${group.conditions.toArrayList()}");
        for (condition in group.conditions) {
            // TODO: OR statements
            if (EscapistsRuntime.getRuntime().application.objectDefs.size > condition.objectInfo) {
                val objectDef = EscapistsRuntime.getRuntime().application.objectDefs[condition.objectInfo];
                if (objectDef != null) {
                    if ((interpreter.callMethod(condition.method, condition.items,
                            condition.identifier, objectDef.handle.toInt()) as Boolean) == condition.inverted()) {
                        return;
                    }
                    continue;
                }
            }

            if ((interpreter.callMethod(condition.method, condition.items,
                    condition.identifier) as Boolean) == condition.inverted()) {
                return;
            }
        }

        println("-- Processing action array: ${group.actions.toArrayList()}");
        for (action in group.actions) {
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