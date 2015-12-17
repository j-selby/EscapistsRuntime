package net.jselby.escapists.game.events

/**
 * Declares various types of methods accessible at runtime.
 */

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Action(val subId : Int, val id : Int)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Actions(val value : Array<Action>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Condition(val subId : Int, val id : Int,
                           val hasInstanceRef : Boolean = false, val conditionRequired : Boolean = false)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Conditions(val value : Array<Condition>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Expression(val subId : Int, val id : Int, val hasInstanceRef : Boolean = false)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Expressions(val value : Array<Expression>)