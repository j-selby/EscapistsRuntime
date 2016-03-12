package net.jselby.escapists

import net.jselby.escapists.data.events.interpreter.InterpreterActionConditionGroup
import net.jselby.escapists.game.Scene

/**
 * The interface for debugging frames, a visible component used to track an Interpreter's status, as well
 * as the state of the environment.
 *
 * Implementation of this class is optional (returning null in the PlatformUtils class is sufficient).
 */
abstract class DebugFrame {
    abstract fun start()

    abstract fun setScene(scene: Scene)

    abstract fun setEventsForScene(events : Array<InterpreterActionConditionGroup>)
}

abstract class NodeProxy {
    abstract fun setState(state : Boolean);
}