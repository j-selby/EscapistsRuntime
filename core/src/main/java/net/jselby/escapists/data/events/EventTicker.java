package net.jselby.escapists.data.events;

import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.game.events.Scope;

/**
 * A event ticker ticks events using varying implementations.
 */
public abstract class EventTicker {
    protected final Events events;

    public EventTicker(Events events) {
        this.events = events;
    }

    public abstract void tick(Scope scope);

    public abstract String getAsDebuggingString();
}
