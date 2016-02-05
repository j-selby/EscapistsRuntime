package net.jselby.escapists.data.events.interpreter

import net.jselby.escapists.data.chunks.Events
import net.jselby.escapists.data.events.EventTicker
import net.jselby.escapists.game.events.Scope

/**
 * The interpreter iterates over a event stack and calls functions which can be called.
 */
class Interpreter(events : Events) : EventTicker(events) {
    override fun tick(scope: Scope) {

    }
}
