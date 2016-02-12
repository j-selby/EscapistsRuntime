package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Frame functions handle scenes and layers.
 */
public class Frames extends FunctionCollection {
    @Action(subId = -3, id = 0)
    public void NextFrame() {
        scope.getGame().loadScene(scope.getGame().getSceneIndex() + 1);
    }

    @Action(subId = -3, id = 2)
    public void JumpToFrame(int scene) {
        scope.getGame().loadScene(scene);
    }

    @Action(subId = 48, id = 111)
    public void ShowLayer(int id) {
        scope.getScene().getLayers()[id - 1].setVisible(true);
    }

    @Action(subId = 48, id = 112)
    public void HideLayer(int id) {
        scope.getScene().getLayers()[id - 1].setVisible(false);
    }

    @Condition(subId = 48, id = -91)
    public boolean IsLayerVisible(int layer) {
        return scope.getScene().getLayers()[layer - 1].isVisible();
    }

    @Action(subId = 2, id = 57)
    public void BringToFront() {
        for (ObjectInstance object : scope.getObjects()) {
            Layer[] layers = scope.getScene().getLayers();
            for (Layer layer : layers) {
                layer.objects.remove(object);
            }
            layers[layers.length - 1].objects.add(object);
        }
    }

    @Action(subId = 2, id = 61)
    public void SetLayer(int id) {
        for (ObjectInstance object : scope.getObjects()) {
            for (Layer layer : scope.getScene().getLayers()) {
                layer.objects.remove(object);
            }
            scope.getScene().getLayers()[id].objects.add(object);
        }
    }
}
