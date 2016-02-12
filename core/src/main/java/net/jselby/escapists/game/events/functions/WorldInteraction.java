package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Actions;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Functions that directly modify layers/objects in some visual way.
 */
public class WorldInteraction extends FunctionCollection {
    @Action(subId = 2, id = 2)
    public void SetX(float newX) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setX(newX);
        }
    }

    @Action(subId = 2, id = 3)
    public void SetY(float newY) {
        for (ObjectInstance object : scope.getObjects()) {
            object.setY(newY);
        }
    }

    @Condition(subId = 2, id = -28, requiresScopeCleanup = true)
    public boolean IsObjectInvisible() {
        boolean isInvisible = false;
        for (ObjectInstance instance : scope.getObjects()) {
            if (!instance.isVisible()) {
                isInvisible = true;
                scope.addObjectToScope(instance);
            }
        }
        return isInvisible;
    }

    @Condition(subId = 2, id = -29, requiresScopeCleanup = true)
    public boolean IsObjectVisible() {
        boolean isVisible = false;
        for (ObjectInstance instance : scope.getObjects()) {
            if (instance.isVisible()) {
                isVisible = true;
                scope.addObjectToScope(instance);
            }
        }
        return isVisible;
    }

    @Condition(subId = 2, id = -4)
    public boolean IsOverlapping(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 2, id = -23)
    public boolean IsOverlappingBackground(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 2, id = -30)
    public boolean ObjectInZone(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 2, id = -32)
    public boolean NumberOfObjects(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 2, id = -9)
    public boolean InsidePlayfield(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 2, id = -10)
    public boolean OutsidePlayfield(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 2, id = -31)
    public boolean NoObjectsInZone(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 2, id = -13)
    public boolean OnBackgroundCollision(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 2, id = -33)
    public boolean AllDestroyed(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Action(subId = 2, id = 24)
    public void Destroy() {
        for (ObjectInstance object : scope.getObjects()) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            scope.getScene().getObjects().remove(object);
        }
    }


    @Actions({
            @Action(subId = 2, id = 26),
            @Action(subId = 3, id = 26)
    })
    public void Disappear() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setVisible(false);
        }
    }

    @Actions({
            @Action(subId = 2, id = 27),
            @Action(subId = 3, id = 27)
    })
    public void Reappear() {
        for (ObjectInstance object : scope.getObjects()) {
            object.setVisible(true);
        }
    }

    @Action(subId = 2, id = 34)
    public void SpreadValue(int id, int value, int start) {
        for (ObjectInstance instance : scope.getObjects()) {
            instance.getAlterableValues()[id] = value++;
        }
    }

    @Action(subId = 2, id = 60)
    public void MoveInFrontOfObject(int id) {
        // Find target layer
        int layerID = -1;
        for (ObjectInstance object : scope.getScene().getObjects()) {
            if (object.getObjectInfo() == id) {
                layerID = object.getLayerID();
                break;
            }
        }

        if (layerID == -1) {
            return;
        }

        for (ObjectInstance object : scope.getObjects()) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            Layer[] layers = scope.getScene().getLayers();
            layers[layerID].objects.add(object);
        }
    }
}
