package net.jselby.escapists.game.events;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import net.jselby.escapists.game.ObjectInstance;

import java.io.File;

/**
 * Implementations of conditions within the engine.
 *
 * @author j_selby
 */
public class ConditionFunctions extends CallbackFunctions {
    @Condition(subId = -1, id = -1)
    public boolean Always() {
        return true;
    }

    @Condition(subId = -1, id = -2)
    public boolean Never() {
        return false;
    }

    @Condition(subId = -1, id = -7)
    public boolean NotAlways() {
        // TODO: Who the fuck wrote this shit?
        return false;
    }

    // Special commands, not actually invoked at runtime
    @Condition(subId = -1, id = -24)
    public boolean OrFiltered() {
        return true;
    }

    @Condition(subId = -1, id = -10)
    public boolean GroupStart(int id) {
        scope.getGroupStack().push(id);
        return true;
    }

    @Condition(subId = -1, id = -11)
    public boolean GroupEnd(int id) {
        if (scope.getGroupStack().peek() == id) {
            scope.getGroupStack().pop();
        } else {
            System.err.println("Stack conflict: " + id);
        }
        return true;
    }

    // Standard commands below
    @Condition(subId = -1, id = -3, conditionRequired = true)
    public boolean Compare(int comparison1, Object str1, int comparison2, Object str2) {
        if (str1 instanceof String) {
            if (comparison2 == 0) { // EQUAL
                return str1.equals(str2);
            } else if (comparison2 == 1) { // DIFFERENT
                return !str1.equals(str2);
            } else {
                throw new IllegalArgumentException("Cannot parse comparsion.");
            }
        }

        double num1 = ((Number) str1).doubleValue();
        double num2 = ((Number) str2).doubleValue();

        //System.out.printf("%s=%s?%d.\n", str1.toString(), str2.toString(), comparison2);
        // TODO: comparison1, what does it do?
        if (comparison2 == 0) { // EQUAL
            return num1 == num2;
        } else if (comparison2 == 1) { // DIFFERENT
            return num1 != num2;
        } else if (comparison2 == 2) { // LOWER_OR_EQUAL
            return num1 <= num2;
        } else if (comparison2 == 3) { // LOWER
            return num1 < num2;
        } else if (comparison2 == 4) { // GREATER_OR_EQUAL
            return num1 >= num2;
        } else if (comparison2 == 5) { // GREATER
            return num1 > num2;
        } else {
            return false;
        }
    }

    @Condition(subId = -3, id = -1)
    public boolean StartOfFrame() {
        return scope.getScene().firstFrame;
    }

    @Condition(subId = -1, id = -16)
    public boolean OnLoop(String loopName) {
        return scope.getScene().getActiveLoops().containsKey(loopName);
    }

    @Condition(subId = -1, id = -23)
    public boolean OnGroupActivation() {
        return scope.getScene().wasGroupJustActivated(scope.getGroupStack().peek());
    }

    @Condition(subId = 47, id = -85)
    public boolean HasItemValue(String key, String value) {
        for (ObjectInstance instance : scope.getScene().getObjects()) {
            return instance.getVariables().containsKey(key + ":" + value);
        }

        return false;
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

    @Conditions({
            @Condition(subId = -4, id = -8, hasInstanceRef = true),
            @Condition(subId = -4, id = -4, hasInstanceRef = true)
    })
    public boolean Every(int id, int every) {
        // TODO: Proper implementation of this
        String key = "_env_every_" + every;
        if (!scope.getScene().getVariables().containsKey(key)) {
            scope.getScene().getVariables().put(key, System.currentTimeMillis());
        }

        // Check if it has updated
        long currentTime = System.currentTimeMillis();
        long lastTime = (Long) scope.getScene().getVariables().get(key);
        long diff = currentTime - lastTime;
        if (diff > every) {
            scope.getScene().getVariables().put(key, currentTime);
            return true;
        }

        return false;
    }

    @Condition(subId = -1, id = -6, hasInstanceRef = true, successCallback = "OnceFinalize")
    public boolean Once(int id) {
        return !scope.getScene().getVariables().containsKey("_env_once_" + id);
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

    @Condition(subId = -1, id = -28)
    public boolean CompareGlobalValueIntEqual(int id, int value) {
        if (scope.getGame().globalInts.containsKey(id)) {
            return value == scope.getGame().globalInts.get(id).intValue();
        } else {
            return value == 0;
        }
    }

    @Condition(subId = -1, id = -20)
    public boolean CompareGlobalString(int id,
            String value) {
        return value.equals(scope.getGame().globalStrings.get(id));
    }

    @Condition(subId = 2, id = -36)
    public boolean CompareAlterableString(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 61, id = -27)
    public boolean CompareAlterableValue(int key, Object value) {
        for (ObjectInstance item : scope.getObjects()) {
            if (item.getVariables().containsKey("" + key)) {
                return value.equals(item.getVariables().get("" + key));
            }
        }
        return false;
    }

    @Condition(subId = 2, id = -42)
    public boolean CompareAlterableValueInt(int id, int value) {
        for (ObjectInstance item : scope.getObjects()) {
            if (item.getVariables().containsKey("" + id)) {
                return value == (Integer) item.getVariables().get("" + id);
            }
        }
        return false;
    }

    @Conditions({
            @Condition(subId = -4, id = -7),
            @Condition(subId = -4, id = -3)
    })
    public boolean TimerEquals(int value, int repeat/*?*/) {
        //int currentTimer = (int) (scope.getScene().getFrameCount() * (1f / 45f) * 1000);
        // Convert to frame
        value /= (1f / 45f) * 1000;
        return value == scope.getScene().getFrameCount();
        //System.out.println(value + ":" + currentTimer);
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) == value;
    }

    @Condition(subId = -4, id = -2)
    public boolean TimerLess(int value, int repeat/*?*/) {
        //int currentTimer = (int) (scope.getScene().getFrameCount() * (1f / 45f) * 1000);
        // Convert to frame
        value /= (1f / 45f) * 1000;
        return value < scope.getScene().getFrameCount();
        //System.out.println(value + ":" + currentTimer);
        //return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) == value;
    }

    @Condition(subId = -4, id = -1)
    public boolean TimerGreater(int value) {
        return (System.currentTimeMillis() - scope.getScene().getSceneStartTime()) > value;
    }

    @Condition(subId = 42, id = -86)
    public boolean DirectoryExists(String name) {
        return new File(name).exists();
    }

    @Condition(subId = 42, id = -83)
    public boolean isFileReadable(String name) {
        return new File(name).exists();
    }

    @Condition(subId = 64, id = -81)
    public boolean SteamHasGameLicense() {
        return scope.getGame().getPlatformUtils().verifySteam();
    }

    @Condition(subId = 39, id = -88)
    public boolean SteamHasOtherGameLicense(int id) {
        // TODO
        return false;
    }

    @Condition(subId = 2, id = -24)
    public boolean IsFlagOff(int value) {
        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return !((Boolean) scope.getScene().getVariables().get(key));
        } else {
            return true;
        }
    }

    @Condition(subId = 2, id = -25)
    public boolean IsFlagOn(int value) {
        String key = "_env_flag_" + value;
        if (scope.getScene().getVariables().containsKey(key)) {
            return (Boolean) scope.getScene().getVariables().get(key);
        } else {
            return false;
        }
    }

    @Condition(subId = 2, id = -17, conditionRequired = true)
    public boolean CompareX(int comparisonType, int x) {
        ObjectInstance[] objects = scope.getObjects();
        if (objects.length == 0) {
            return false;
        }

        for (ObjectInstance instance : objects) {
            if (comparisonType == 0) { // EQUAL
                return instance.getX() == x;
            } else if (comparisonType == 1) { // DIFFERENT
                return instance.getX() != x;
            } else if (comparisonType == 2) { // LOWER_OR_EQUAL
                return instance.getX() <= x;
            } else if (comparisonType == 3) { // LOWER
                return instance.getX() < x;
            } else if (comparisonType == 4) { // GREATER_OR_EQUAL
                return instance.getX() >= x;
            } else if (comparisonType == 5) { // GREATER
                return instance.getX() > x;
            } else {
                return false;
            }
        }
        return true;
    }

    @Condition(subId = 2, id = -16, conditionRequired = true)
    public boolean CompareY(int comparisonType, int y) {
        ObjectInstance[] objects = scope.getObjects();
        if (objects.length == 0) {
            return false;
        }

        for (ObjectInstance instance : objects) {
            if (comparisonType == 0) { // EQUAL
                return instance.getY() == y;
            } else if (comparisonType == 1) { // DIFFERENT
                return instance.getY() != y;
            } else if (comparisonType == 2) { // LOWER_OR_EQUAL
                return instance.getY() <= y;
            } else if (comparisonType == 3) { // LOWER
                return instance.getY() < y;
            } else if (comparisonType == 4) { // GREATER_OR_EQUAL
                return instance.getY() >= y;
            } else if (comparisonType == 5) { // GREATER
                return instance.getY() > y;
            } else {
                return false;
            }
        }
        return true;
    }

    @Condition(subId = 48, id = -91)
    public boolean IsLayerVisible(int layer) {
        return scope.getScene().getLayers()[layer - 1].isVisible();
    }

    @Condition(subId = 2, id = -8)
    public boolean FacingInDirection(int direction) {
        // TODO: Directions
        return true;
    }

    @Condition(subId = -1, id = -12)
    public boolean GroupActivated(int id) {
        return scope.getScene().getActiveGroups().get(id);
    }

    @Condition(subId = 2, id = -3)
    public boolean AnimationPlaying(int num) {
        ObjectInstance[] objects = scope.getObjects();
        if (objects.length == 0) {
            return false;
        }
        for (ObjectInstance instance : objects) {
            if (instance.getAnimation() != num) {
                return false;
            }
        }
        return true;
    }

    @Condition(subId = 2, id = -1)
    public boolean AnimationFrame(int num) {
        // TODO: Proper animation implementation
        return false;
    }

    @Condition(subId = 2, id = -2)
    public boolean AnimationFinished(int num) {
        // TODO: Proper animation implementation
        return false;
    }

    @Condition(subId = -1, id = -29)
    public boolean CompareGlobalValueIntNotEqual(int id, int value) {
        if (scope.getGame().globalInts.containsKey(id)) {
            return value != scope.getGame().globalInts.get(id).intValue();
        } else {
            return value != 0;
        }
    }

    @Condition(subId = -1, id = -31)
    public boolean CompareGlobalValueIntLess(int id, int value) {
        if (scope.getGame().globalInts.containsKey(id)) {
            return value < scope.getGame().globalInts.get(id).intValue();
        } else {
            return value < 0;
        }
    }

    @Condition(subId = -1, id = -32)
    public boolean CompareGlobalValueIntGreaterEqual(int id, int value) {
        if (scope.getGame().globalInts.containsKey(id)) {
            return value >= scope.getGame().globalInts.get(id).intValue();
        } else {
            return value >= 0;
        }
    }

    @Conditions({
            @Condition(subId = -2, id = -33),
            @Condition(subId = -1, id = -33)
    })
    public boolean CompareGlobalValueIntGreater(int id, int value) {
        if (scope.getGame().globalInts.containsKey(id)) {
            return value > scope.getGame().globalInts.get(id).intValue();
        } else {
            return value > 0;
        }
    }

    @Condition(subId = 43, id = -82)
    public boolean unknown1(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 39, id = -102)
    public boolean unknown2(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 3, id = -32)
    public boolean unknown3(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 58, id = -81)
    public boolean unknown4(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 58, id = -82)
    public boolean unknown5(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 33, id = -81)
    public boolean unknown6(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 50, id = -85)
    public boolean unknown7(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 3, id = -29)
    public boolean unknown8(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 52, id = -81)
    public boolean unknown9(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 59, id = -81)
    public boolean unknown10(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 3, id = -28)
    public boolean unknown11(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 66, id = -96)
    public boolean unknown12(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 66, id = -88)
    public boolean unknown13(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 66, id = -89)
    public boolean unknown14(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 61, id = -32)
    public boolean unknown15(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 39, id = -81)
    public boolean unknown16(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 36, id = -25)
    public boolean unknown17(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 39, id = -136)
    public boolean unknown18(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 36, id = -24)
    public boolean unknown19(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 39, id = -138)
    public boolean unknown20(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 40, id = -81)
    public boolean unknown21(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 40, id = -87)
    public boolean unknown22(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 40, id = -85)
    public boolean unknown23(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 45, id = -32)
    public boolean unknown24(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 61, id = -25)
    public boolean unknown25(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 32, id = -85)
    public boolean unknown26(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 48, id = -92)
    public boolean unknown27(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 35, id = -83)
    public boolean unknown28(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 7, id = -32)
    public boolean unknown29(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 61, id = -41)
    public boolean unknown30(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 61, id = -42)
    public boolean unknown31(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 61, id = -24)
    public boolean unknown32(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 41, id = -85)
    public boolean unknown33(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 45, id = -42)
    public boolean unknown34(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = 7, id = -81)
    public boolean CompareCounter(int val1, int val2) {
        // TODO: Compare counters
        return true;
    }

    @Condition(subId = 2, id = -14)
    public boolean OnCollision(int val1, int val2) {
        // TODO: Um, WTF?
        return false;
    }

    @Condition(subId = 2, id = -41)
    public boolean OnObjectLoop(int val1, int val2) {
        // TODO: Um, WTF?
        return true;
    }

    @Condition(subId = 2, id = -27)
    public boolean ElseIf(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 2, id = -34)
    public boolean PickRandom(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
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

    @Condition(subId = -1, id = -4)
    public boolean RestrictFor(int val1, int vale2) {
        // TODO: I am so totally screwed
        return true;
    }

    @Condition(subId = 2, id = -7)
    public boolean MovementStopped(int id, int value) {
        // TODO: Unknown value
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

    @Condition(subId = -2, id = -1)
    public boolean SampleNotPlaying(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = -2, id = -8)
    public boolean ChannelNotPlaying(int id, int value) {
        // TODO: Unknown value
        return true;
    }
}
