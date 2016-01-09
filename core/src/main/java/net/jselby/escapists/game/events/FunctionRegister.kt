package net.jselby.escapists.game.events

import java.lang.reflect.Method
import java.util.*

/**
 * The FunctionRegister collects Actions, Conditions and Parameters and allows
 * them to be polled during runtime by chunk loaders/whatever.
 *
 * @author j_selby
 */
class FunctionRegister {
    private val functions : HashMap<Pair<Int, Int>, Pair<Method, Annotation>> = HashMap();
    private val expressions : HashMap<Pair<Int, Int>, Pair<Method, Expression>> = HashMap();

    init {
        for (method in Scope::class.java.methods) {
            // Check for annotations

            for (checkAnnotation in method.annotations) {
                if (checkAnnotation is Action
                    || checkAnnotation is Condition
                    || checkAnnotation is Expression) {
                    registerAnnotation(checkAnnotation, method);
                } else if (checkAnnotation is Actions) {
                    for (annotation in checkAnnotation.value) {
                        registerAnnotation(annotation, method);
                    }
                } else if (checkAnnotation is Conditions) {
                    for (annotation in checkAnnotation.value) {
                        registerAnnotation(annotation, method);
                    }
                } else if (checkAnnotation is Expressions) {
                    for (annotation in checkAnnotation.value) {
                        registerAnnotation(annotation, method);
                    }
                }
            }
        }
    }

    private fun registerAnnotation(checkAnnotation: Annotation, method: Method) {
        // This object is an action
        var subId : Int = 0;
        var id : Int = 0;

        if (checkAnnotation is Action) {
            subId = checkAnnotation.subId;
            id = checkAnnotation.id;
        } else if (checkAnnotation is Condition) {
            subId = checkAnnotation.subId;
            id = checkAnnotation.id;
        } else if (checkAnnotation is Expression) {
            subId = checkAnnotation.subId;
            id = checkAnnotation.id;
        } else {
            error("Failed to parse annotation @ $method.");
        }

        if (checkAnnotation is Expression) {
            val predefinedFunction = getExpressionFunction(subId, id);
            if (predefinedFunction == null) {
                expressions.put(Pair(subId, id), Pair(method, checkAnnotation))
            } else {
                error("Conflict in registering methods: $predefinedFunction and $method.");
            }
        } else {
            val predefinedFunction = getFunction(subId, id);
            if (predefinedFunction == null) {
                functions.put(Pair(subId, id), Pair(method, checkAnnotation))
            } else {
                error("Conflict in registering methods: $predefinedFunction and $method.");
            }
        }

        //println("Exported function $method.");
    }

    fun getFunction(subId : Int, id : Int) : Pair<Method, Annotation>? {
        return functions[Pair(subId, id)];
    }

    fun getExpressionFunction(subId : Int, id : Int) : Pair<Method, Annotation>? {
        return expressions[Pair(subId, id)];
    }
}