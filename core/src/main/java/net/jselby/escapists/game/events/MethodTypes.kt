package net.jselby.escapists.game.events

/**
 * Declares various types of methods accessible at runtime.
 */

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Action(val subId : Int, val id : Int,
                        val successCallback: String = "")

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Actions(val value : Array<Action>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Condition(val subId : Int, val id : Int,
                           val hasInstanceRef : Boolean = false, val conditionRequired : Boolean = false,
                           val successCallback : String = "", val requiresScopeCleanup : Boolean = false,
                           val syntax : String = "")

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Conditions(val value : Array<Condition>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Expression(val subId : Int, val id : Int,
                            val requiresArg1 : Boolean = false,
                            val requiresArg2 : Boolean = false,
                            val requiresArg3 : Boolean = false,

                            // Requires further expression(s) to call function
                            val openEnded : Boolean = false)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Expressions(val value : Array<Expression>)