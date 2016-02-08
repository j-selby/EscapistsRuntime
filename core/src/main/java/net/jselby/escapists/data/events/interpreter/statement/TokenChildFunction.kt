package net.jselby.escapists.data.events.interpreter.statement

import net.jselby.escapists.data.events.expression.ExpressionFunction

/**
 * A token function is a link to a function stored in the statement's function array.
 */
class TokenChildFunction(val id: Int) : Token() {
    override fun toString(): String {
        return "TokenChildFunction($id)"
    }
}