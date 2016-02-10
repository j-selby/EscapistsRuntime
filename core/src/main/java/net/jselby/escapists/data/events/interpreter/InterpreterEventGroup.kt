package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.game.events.Scope
import net.jselby.escapists.game.events.functions.Groups
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
        if (id != -9999 && !(scope.scene.activeGroups[id] as Boolean)) {
            //println("Exiting group: $id");
            return;
        }
        (interpreter.getCollectionInstance(Groups::class.java) as Groups).GroupStart(id);
        for (child in children) {
            child.invokeIfPossible(interpreter, scope);
        }
        (interpreter.getCollectionInstance(Groups::class.java) as Groups).GroupEnd(id);
    }

    override fun toString(): String {
        return "InterpreterEventGroup={children=${children.toString()}";
    }

    override fun getAsDebuggingString() : String {
        var events = "";
        events += "enter-group=$id\n\t";

        for (child in children) {
            events += child.getAsDebuggingString().replace("\n", "\n\t");
        }

        events += "leave-group=$id\n";
        return events;
    }
}
