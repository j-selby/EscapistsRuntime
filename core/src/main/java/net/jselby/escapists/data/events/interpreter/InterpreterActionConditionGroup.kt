package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.game.events.Scope

/**
 * A interpreter event loop object is a EventLoop that is able to hold state.
 */
open class InterpreterActionConditionGroup(val group: Events.EventGroup?) {
    open fun invokeIfPossible(interpreter: Interpreter, scope: Scope) {
        group!!

        // Check conditions
        for (condition in group.conditions) {
            // TODO: Inversion, OR statements
            if (!(interpreter.callMethod(condition.method, condition.items, condition.identifier) as Boolean)) {
                return;
            }
        }

        for (action in group.actions) {
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