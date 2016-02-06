package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.game.events.Scope
import java.util.*

/**
 * A event group is a selection of EventLoops (The individual if statements).
 */
class InterpreterEventGroup(val id: Int) : InterpreterActionConditionGroup(null) {
    val children = ArrayList<InterpreterActionConditionGroup>();

    fun add(child: InterpreterActionConditionGroup) {
        children.add(child);
    }

    override fun invokeIfPossible(interpreter: Interpreter, scope : Scope) {
        println("Invoked: " + id);
        for (child in children) {
            child.invokeIfPossible(interpreter, scope);
        }
    }

    override fun toString(): String {
        return "InterpreterEventGroup={children=${children.toString()}";
    }

}
