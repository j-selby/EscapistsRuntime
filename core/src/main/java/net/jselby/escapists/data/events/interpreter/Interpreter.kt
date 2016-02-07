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
        groupStack.add(InterpreterEventGroup(-9999));

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
                           identifier: Short,
                           handle : Int = 0) : Any? {
        // Find parent collection
        for (collection in functions) {
            if (method.parent.isInstance(collection)) {

                // Build params
                val list = ArrayList<Any>();

                // Check annotations
                if (method.checkAnnotation is Condition) {
                    if (method.checkAnnotation.hasInstanceRef) { // Requires context
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
                    item.add(this, list);
                }

                if (handle != 0) {
                    println("Calling withObjects: $handle");
                    collection.withObjects(handle)
                }

                println("Calling: ${method.method.name}, $list")
                return method.method.invoke(collection, *(list.toTypedArray()));
            }
        }

        throw IllegalStateException("No implementing collection for method $method");
    }

    fun callMethod(method: Method,
                   params: Array<Any>) : Any {
        // Find parent collection
        for (collection in functions) {
            if (method.declaringClass.isInstance(collection)) {
                println("Invoking: ${method.name} with ${params.toList()}")
                return method.invoke(collection, *params);
            }
        }

        throw IllegalStateException("No implementing collection for method $method");
    }

    fun getCollectionInstance(targetClass: Class<out FunctionCollection>) : FunctionCollection {
        // Find parent collection
        for (collection in functions) {
            if (collection.javaClass == targetClass) {
                return collection;
            }
        }

        throw IllegalStateException("No implementing collection for name ${targetClass.name}");
    }
}