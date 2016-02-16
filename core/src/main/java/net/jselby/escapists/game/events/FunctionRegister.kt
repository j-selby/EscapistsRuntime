package net.jselby.escapists.game.events

import net.jselby.escapists.game.events.functions.*
import java.lang.reflect.Method
import java.util.*

/**
 * The FunctionRegister collects Actions, Conditions and Parameters and allows
 * them to be polled during runtime by chunk loaders/whatever.
 *
 * @author j_selby
 */
class FunctionRegister {
    val providers : Array<Class<out FunctionCollection>> = arrayOf(
            Application::class.java,
            Audio::class.java,
            Comparisons::class.java,
            Configuration::class.java,
            EncryptionUtils::class.java,
            net.jselby.escapists.game.events.functions.Expressions::class.java,
            FileIO::class.java,
            Frames::class.java,
            Graphics::class.java,
            Groups::class.java,
            Input::class.java,
            Logic::class.java,
            NetIO::class.java,
            Physics::class.java,
            Steamworks::class.java,
            StringLists::class.java,
            Timers::class.java,
            Unknown::class.java,
            Variables::class.java,
            WorldInteraction::class.java,
            Windows::class.java
    );

    private val functions : HashMap<Pair<Int, Int>, FunctionRegistration> = HashMap();
    private val expressions : HashMap<Pair<Int, Int>, Pair<Method, Expression>> = HashMap();

    init {
        for (provider in providers) {
            for (method in provider.methods) {
                // Check for annotations

                for (checkAnnotation in method.annotations) {
                    if (checkAnnotation is Action
                            || checkAnnotation is Condition
                            || checkAnnotation is Expression) {
                        registerAnnotation(checkAnnotation, method, provider);
                    } else if (checkAnnotation is Actions) {
                        for (annotation in checkAnnotation.value) {
                            registerAnnotation(annotation, method, provider);
                        }
                    } else if (checkAnnotation is Conditions) {
                        for (annotation in checkAnnotation.value) {
                            registerAnnotation(annotation, method, provider);
                        }
                    } else if (checkAnnotation is Expressions) {
                        for (annotation in checkAnnotation.value) {
                            registerAnnotation(annotation, method, provider);
                        }
                    }
                }
            }
        }
    }

    private fun registerAnnotation(checkAnnotation: Annotation, method: Method,
                                   provider : Class<out FunctionCollection>) {
        // This object is an action
        var subId : Int;
        var id : Int;

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
                functions.put(Pair(subId, id), FunctionRegistration(method, checkAnnotation,
                        provider))
            } else {
                error("Conflict in registering methods: $predefinedFunction and $method.");
            }
        }

        //println("Exported function $method.");
    }

    fun getFunction(subId : Int, id : Int) : FunctionRegistration? {
        return functions[Pair(subId, id)];
    }

    fun getExpressionFunction(subId : Int, id : Int) : Pair<Method, Annotation>? {
        return expressions[Pair(subId, id)];
    }
}

data class FunctionRegistration(val method: Method,
                                val checkAnnotation: Annotation,
                                val parent : Class<out FunctionCollection>);