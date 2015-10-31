package net.jselby.escapists.data.events;

import java.util.HashMap;
import java.util.Map;

/**
 * Names of various Conditions within the Events scripting "language".
 *
 * Modified from mmfparser - https://github.com/matpow2/anaconda
 */
public class ConditionNames {
    private final static Map<Integer, Map<Integer, String>> map = new HashMap<Integer, Map<Integer, String>>();
    private final static Map<Integer, String> extensionMap = new HashMap<Integer, String>();

    static {
        Map<Integer, String> tempMap = new HashMap<Integer, String>();
        tempMap.put(-81, "ObjectClicked"); // SPRCLICK
        map.put(2, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-83, "AnswerMatches");
        tempMap.put(-82, "AnswerFalse");
        tempMap.put(-81, "AnswerTrue");
        map.put(4, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-81, "CompareCounter");
        map.put(7, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-84, "SubApplicationPaused");
        tempMap.put(-83, "SubApplicationVisible");
        tempMap.put(-82, "SubApplicationFinished");
        tempMap.put(-81, "SubApplicationFrameChanged");
        map.put(9, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-1, "SampleNotPlaying");
        tempMap.put(-9, "ChannelPaused");
        tempMap.put(-8, "ChannelNotPlaying");
        tempMap.put(-7, "MusicPaused");
        tempMap.put(-6, "SamplePaused");
        tempMap.put(-5, "MusicFinished");
        tempMap.put(-4, "NoMusicPlaying");
        tempMap.put(-3, "NoSamplesPlaying");
        tempMap.put(-2, "SpecificMusicNotPlaying");
        map.put(-2, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-1, "PlayerPlaying"); // PlayerPlaying
        tempMap.put(-6, "PlayerKeyDown");
        tempMap.put(-5, "PlayerDied");
        tempMap.put(-4, "PlayerKeyPressed");
        tempMap.put(-3, "NumberOfLives");
        tempMap.put(-2, "CompareScore");
        map.put(-7, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-2, "KeyDown");
        tempMap.put(-12, "MouseWheelDown");
        tempMap.put(-11, "MouseWheelUp");
        tempMap.put(-10, "MouseVisible");
        tempMap.put(-9, "AnyKeyPressed");
        tempMap.put(-8, "WhileMousePressed");
        tempMap.put(-7, "ObjectClicked");
        tempMap.put(-6, "MouseClickedInZone");
        tempMap.put(-5, "MouseClicked");
        tempMap.put(-4, "MouseOnObject");
        tempMap.put(-3, "MouseInZone");
        tempMap.put(-1, "KeyPressed");
        map.put(-6, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-2, "AllObjectsInZone"); // AllObjectsInZone_Old
        tempMap.put(-23, "PickObjectsInLine");
        tempMap.put(-22, "PickFlagOff");
        tempMap.put(-21, "PickFlagOn");
        tempMap.put(-20, "PickAlterableValue");
        tempMap.put(-19, "PickFromFixed");
        tempMap.put(-18, "PickObjectsInZone");
        tempMap.put(-17, "PickRandomObject");
        tempMap.put(-16, "PickRandomObjectInZone");
        tempMap.put(-15, "CompareObjectCount");
        tempMap.put(-14, "AllObjectsInZone");
        tempMap.put(-13, "NoAllObjectsInZone");
        tempMap.put(-12, "PickFlagOff"); // PickFlagOff_Old
        tempMap.put(-11, "PickFlagOn"); // PickFlagOn_Old
        tempMap.put(-8, "PickAlterableValue"); // PickAlterableValue_Old
        tempMap.put(-7, "PickFromFixed"); // PickFromFixed_Old
        tempMap.put(-6, "PickObjectsInZone"); // PickObjectsInZone_Old
        tempMap.put(-5, "PickRandomObject"); // PickRandomObject_Old
        tempMap.put(-4, "PickRandomObjectInZoneOld");
        tempMap.put(-3, "CompareObjectCount"); // CompareObjectCount_Old
        tempMap.put(-1, "NoAllObjectsInZone"); // NoAllObjectsInZone_Old
        map.put(-5, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-8, "Every");
        tempMap.put(-7, "TimerEquals");
        tempMap.put(-6, "OnTimerEvent");
        tempMap.put(-5, "CompareAwayTime");
        tempMap.put(-4, "Every");
        tempMap.put(-3, "TimerEquals");
        tempMap.put(-2, "TimerLess");
        tempMap.put(-1, "TimerGreater");
        map.put(-4, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-1, "StartOfFrame");
        tempMap.put(-10, "FrameSaved");
        tempMap.put(-9, "FrameLoaded");
        tempMap.put(-8, "ApplicationResumed");
        tempMap.put(-7, "VsyncEnabled");
        tempMap.put(-6, "IsLadder");
        tempMap.put(-5, "IsObstacle");
        tempMap.put(-4, "EndOfApplication");
        tempMap.put(-3, "LEVEL");
        tempMap.put(-2, "EndOfFrame");
        map.put(-3, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(-1, "Always");
        tempMap.put(-2, "Never");
        tempMap.put(-3, "Compare");
        tempMap.put(-4, "RestrictFor");
        tempMap.put(-5, "Repeat");
        tempMap.put(-6, "Once");
        tempMap.put(-7, "NotAlways");
        tempMap.put(-8, "CompareGlobalValue");
        tempMap.put(-9, "Remark");
        tempMap.put(-10, "NewGroup");
        tempMap.put(-11, "GroupEnd");
        tempMap.put(-12, "GroupActivated");
        tempMap.put(-13, "RecordKey"); // RECORDKEY
        tempMap.put(-14, "MenuSelected");
        tempMap.put(-15, "FilesDropped");
        tempMap.put(-16, "OnLoop");
        tempMap.put(-17, "MenuChecked");
        tempMap.put(-18, "MenuEnabled");
        tempMap.put(-19, "MenuVisible");
        tempMap.put(-20, "CompareGlobalString");
        tempMap.put(-21, "CloseSelected");
        tempMap.put(-22, "ClipboardDataAvailable");
        tempMap.put(-23, "OnGroupActivation");
        tempMap.put(-24, "OrFiltered");
        tempMap.put(-25, "OrLogical");
        tempMap.put(-26, "Chance");
        tempMap.put(-27, "ElseIf");
        tempMap.put(-28, "CompareGlobalValueIntEqual");
        tempMap.put(-29, "CompareGlobalValueIntNotEqual");
        tempMap.put(-30, "CompareGlobalValueIntLessEqual");
        tempMap.put(-31, "CompareGlobalValueIntLess");
        tempMap.put(-32, "CompareGlobalValueIntGreaterEqual");
        tempMap.put(-33, "CompareGlobalValueIntGreater");
        tempMap.put(-34, "CompareGlobalValueDoubleEqual");
        tempMap.put(-35, "CompareGlobalValueDoubleNotEqual");
        tempMap.put(-36, "CompareGlobalValueDoubleLessEqual");
        tempMap.put(-37, "CompareGlobalValueDoubleLess");
        tempMap.put(-38, "CompareGlobalValueDoubleGreaterEqual");
        tempMap.put(-39, "CompareGlobalValueDoubleGreater");
        tempMap.put(-40, "RunningAs");
        map.put(-1, tempMap);

        extensionMap.put(-1, "AnimationFrame");
        extensionMap.put(-2, "AnimationFinished");
        extensionMap.put(-3, "AnimationPlaying");
        extensionMap.put(-4, "IsOverlapping");
        extensionMap.put(-5, "Reversed");
        extensionMap.put(-6, "Bouncing");
        extensionMap.put(-7, "MovementStopped");
        extensionMap.put(-8, "FacingInDirection");
        extensionMap.put(-9, "InsidePlayfield");
        extensionMap.put(-10, "OutsidePlayfield");
        extensionMap.put(-11, "EnteringPlayfield");
        extensionMap.put(-12, "LeavingPlayfield");
        extensionMap.put(-13, "OnBackgroundCollision");
        extensionMap.put(-14, "OnCollision");
        extensionMap.put(-15, "CompareSpeed");
        extensionMap.put(-16, "CompareY");
        extensionMap.put(-17, "CompareX");
        extensionMap.put(-18, "CompareDeceleration");
        extensionMap.put(-19, "CompareAcceleration");
        extensionMap.put(-20, "NodeReached");
        extensionMap.put(-21, "PathFinished");
        extensionMap.put(-22, "NearWindowBorder");
        extensionMap.put(-23, "IsOverlappingBackground");
        extensionMap.put(-24, "FlagOff");
        extensionMap.put(-25, "FlagOn");
        extensionMap.put(-26, "CompareFixedValue");
        extensionMap.put(-27, "CompareAlterableValue");
        extensionMap.put(-28, "ObjectInvisible");
        extensionMap.put(-29, "ObjectVisible");
        extensionMap.put(-30, "ObjectsInZone");
        extensionMap.put(-31, "NoObjectsInZone");
        extensionMap.put(-32, "NumberOfObjects");
        extensionMap.put(-33, "AllDestroyed");
        extensionMap.put(-34, "PickRandom");
        extensionMap.put(-35, "NamedNodeReached");
        extensionMap.put(-36, "CompareAlterableString");
        extensionMap.put(-37, "IsBold");
        extensionMap.put(-38, "IsItalic");
        extensionMap.put(-39, "IsUnderline");
        extensionMap.put(-40, "IsStrikeOut");
        extensionMap.put(-41, "OnObjectLoop");
        extensionMap.put(-42, "CompareAlterableValueInt");
        extensionMap.put(-43, "CompareAlterableValueDouble");
    }

    public static String getByID(int categoryID, int itemID) {
        if (map.containsKey(categoryID) && map.get(categoryID).containsKey(itemID)) {
            return map.get(categoryID).get(itemID);
        } else if (extensionMap.containsKey(itemID)) {
            return "extension_" + extensionMap.get(itemID);
        } else {
            return null;
        }
    }
}
