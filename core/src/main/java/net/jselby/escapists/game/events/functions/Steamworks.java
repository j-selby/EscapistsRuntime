package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Steamworks functions hook into Steam.
 */
public class Steamworks extends FunctionCollection {
    @Condition(subId = 64, id = -81)
    public boolean SteamHasGameLicense() {
        return scope.getGame().getPlatformUtils().verifySteam();
    }

    @Condition(subId = 39, id = -88)
    public boolean SteamHasOtherGameLicense(int id) {
        // TODO
        return false;
    }
}
