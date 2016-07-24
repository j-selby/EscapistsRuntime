package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Steamworks functions hook into Steam.
 */
public class Steamworks extends FunctionCollection {
    @Condition(subId = 64, id = -81)
    public boolean steamHasGameLicense() {
        return scope.getGame().getPlatformUtils().verifySteam();
    }

    @Condition(subId = 39, id = -88)
    public boolean steamHasOtherGameLicense(int id) {
        // TODO
        return false;
    }

    @Condition(subId = 39, id = -81, hasInstanceRef = true, successCallback = "Steamworks:finalizeSteamConnection(true)")
    public boolean onSteamConnected(int handleId) {
        return !scope.getScene().getVariables().containsKey("_env_steamworks_onetime_" + handleId);
    }

    public void finalizeSteamConnection(int handleId) {
        String key = "_env_steamworks_onetime_" + handleId;
        if (!scope.getScene().getVariables().containsKey(key)) {
            scope.getScene().getVariables().put(key, true);
        }
    }
}
