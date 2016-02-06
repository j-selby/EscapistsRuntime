package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.EscapistsRuntime
import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.data.events.EventTicker
import net.jselby.escapists.data.events.ParameterValue
import net.jselby.escapists.game.events.Condition
import net.jselby.escapists.game.events.FunctionCollection
import net.jselby.escapists.game.events.FunctionRegistration
import net.jselby.escapists.game.events.Scope
import java.lang.reflect.Method
import java.util.*

/**
 * The interpreter iterates over a event stack and calls functions which can be called.
 */
class Interpreter(events : Events, scope : Scope) : EventTicker(events) {
    val groups = ArrayList<InterpreterEventGroup>();
    val queue : ArrayList<InterpreterEventGroup>;

    val functions = ArrayList<FunctionCollection>();

    init {
        // Build function registers
        for (collection in EscapistsRuntime.getRuntime().register.providers) {
            val instance: FunctionCollection
            try {
                instance = collection.newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }

            instance.scope = scope
            functions.add(instance);
        }

        // Parse events into event groups
        val groupStack = Stack<InterpreterEventGroup>();
        groupStack.add(InterpreterEventGroup(0));

        for (rawGroup in events.groups) {
            val targetGroup = groupStack.peek();

            if (rawGroup.conditions.size > 0 && rawGroup.conditions[0].name.equals("GroupStart", ignoreCase = true)) {
                val item = (rawGroup.conditions[0].items[0].value as ParameterValue.Group);
                val newGroup = InterpreterEventGroup(item.id);
                targetGroup.add(newGroup);
                groupStack.push(newGroup);
                continue;
            } else if (rawGroup.conditions.size > 0 && rawGroup.conditions[0].name.equals("GroupEnd", ignoreCase = true)) {
                groupStack.pop();
                continue;
            }

            targetGroup.add(InterpreterActionConditionGroup(rawGroup));
        }

        //println(groupStack);
        groups.addAll(groupStack);

        // Setup a queue for incoming event groups
        queue = ArrayList(groups.size);
    }

    override fun tick(scope: Scope) {
        for (group in groups) {
            group.invokeIfPossible(this, scope);
        }
    }

    fun callMethod(method: FunctionRegistration,
                           items: Array<Events.Parameter>,
                           identifier: Short) : Any? {
        println("Invoking: ${method.method.name}")
        // Find parent collection
        for (collection in functions) {
            if (method.parent.isInstance(collection)) {
                println("Found match: $method > $collection");

                // Build params
                val list = ArrayList<Any>();

                // Check annotations
                if (method.checkAnnotation is Condition) {
                    if (method.checkAnnotation.hasInstanceRef) { // Requires context
                        println("Context required!");
                        list.add(identifier);
                    }
                }

                for (item in items) {
                    if (method.checkAnnotation is Condition) {
                        if (method.checkAnnotation.conditionRequired
                                && item.value is ParameterValue.ExpressionParameter) {
                            list.add((item.value as ParameterValue.ExpressionParameter).comparison);
                        }
                    }
                    println(item);
                    item.add(this, list);
                }

                println("Completed list: $list");

                val arrayOut = list.toTypedArray();
                println("Final call: ${method}, ${method.method.name}, ${collection}, $arrayOut, ${list.size}")
                return method.method.invoke(collection, *arrayOut);
            }
        }

        throw IllegalStateException("No implementing collection for method $method");
    }

    fun callMethod(method: Method,
                   params: Array<Any>) : Any {
        // Find parent collection
        for (collection in functions) {
            if (method.declaringClass.isInstance(collection)) {
                println("Found string match: $method > $collection");

                println("Completed prebuilt list: ${params.toArrayList()}");

                return method.invoke(collection, *params);
            }
        }

        throw IllegalStateException("No implementing collection for method $method");
    }
}