package net.jselby.escapists.game.events.functions;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Input functions handle user input.
 */
public class Input extends FunctionCollection {
    @Condition(subId = -6, id = -4, requiresScopeCleanup = true)
    public boolean MouseOnObject(int objectId) {
        // Find all objects which correspond to this
        int mouseX = scope.getGame().getMouseX();
        int mouseY = scope.getGame().getMouseY();

        boolean mouseOnObject = false;
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == objectId
                    && instance.getScreenX() <= mouseX
                    && instance.getScreenY() <= mouseY
                    && instance.getScreenX() + instance.getWidth() >= mouseX
                    && instance.getScreenY() + instance.getHeight() >= mouseY)  {
                mouseOnObject = true;
                scope.addObjectToScope(instance);
            }
        }

        return mouseOnObject;
    }

    @Condition(subId = -6, id = -5)
    public boolean MouseClicked(int mouseButton, boolean doubleClick) {
        // We only get left clicks on mobile platforms
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && mouseButton != 0)
                && scope.getGame().isButtonClicked(mouseButton);

    }

    @Condition(subId = -6, id = -6)
    public boolean MouseClickedInZone(int mouseButton, boolean doubleClick) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = -6, id = -3)
    public boolean MouseInZone(int mouseButton, boolean doubleClick) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = -6, id = -8)
    public boolean WhileMousePressed(int mouseButton) {
        // We only get left clicks on mobile platforms
        return !(Gdx.app.getType() != Application.ApplicationType.Desktop && mouseButton != 0)
                && Gdx.input.isButtonPressed(mouseButton);

    }

    @Condition(subId = -6, id = -7, successCallback = "Vibrate", requiresScopeCleanup = true)
    public boolean ObjectClicked(int mouseButton, boolean doubleClicked,
                                 int object) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop
                && mouseButton != 0) {
            // We only get left clicks on mobile platforms
            //System.out.println("Bad click type for platform: " + click.click);
            return false;
        }

        // TODO: Support double clicking

        if (!scope.getGame().isButtonClicked(mouseButton)) {
            return false;
        }

        // Find all objects which correspond to this
        int mouseX = scope.getGame().getMouseX();
        int mouseY = scope.getGame().getMouseY();

        if (mouseX == -1 && mouseY == -1) {
            return false;
        }

        boolean mouseOver = false;
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            if (instance.getObjectInfo() == object
                    && instance.getScreenX() <= mouseX
                    && instance.getScreenY() <= mouseY
                    && instance.getScreenX() + instance.getWidth() >= mouseX
                    && instance.getScreenY() + instance.getHeight() >= mouseY) {
                mouseOver = true;
                scope.addObjectToScope(instance);
            }
        }

        return mouseOver;
    }

    public void Vibrate(Object... ignored) {
        Gdx.input.vibrate(100);
    }

    @Condition(subId = -6, id = -1)
    public boolean KeyPressed(int key) {
        // TODO: Key pressed
        //Gdx.input.
        /*if (validate) {
            System.out.println("Key " + key.key + " pressed.");
        } else {
            System.out.println("Key " + key.key + "(" + KeyEvent.getKeyText(key.key) + "," + Input.Keys.toString(key.key) + ") not pressed.");
        }
        return validate;*/
        return false;
    }

    @Condition(subId = -6, id = -9)
    public boolean AnyKeyPressed(int key) {
        // TODO: Key pressed
        //Gdx.input.
        /*if (validate) {
            System.out.println("Key " + key.key + " pressed.");
        } else {
            System.out.println("Key " + key.key + "(" + KeyEvent.getKeyText(key.key) + "," + Input.Keys.toString(key.key) + ") not pressed.");
        }
        return validate;*/
        return false;
    }

    @Condition(subId = -6, id = -2)
    public boolean KeyDown(int key) {
        // TODO: Key down
        return false;
    }

    @Action(subId = -7, id = 9)
    public void ChangeInputKey(int key, int keycode) {
        // TODO: Key binds
    }

}
