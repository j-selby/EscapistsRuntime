package net.jselby.escapists.data.events;

import java.util.HashMap;
import java.util.Map;

/**
 * The names of expressions.
 */
public class ExpressionNames {
    private final static Map<Integer, Map<Integer, String>> map = new HashMap<Integer, Map<Integer, String>>();
    private final static Map<Integer, String> extensionMap = new HashMap<Integer, String>();

    static {

        Map<Integer, String> tempMap = new HashMap<Integer, String>(); // operators
        tempMap.put(0, "End");
        tempMap.put(2, "Plus");
        tempMap.put(4, "Minus");
        tempMap.put(6, "Multiply");
        tempMap.put(8, "Divide");
        tempMap.put(10, "Modulus");
        tempMap.put(12, "Power");
        tempMap.put(14, "AND");
        tempMap.put(16, "OR");
        tempMap.put(18, "XOR");
        map.put(0, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "GetColorAt");
        tempMap.put(81, "GetXScale");
        tempMap.put(82, "GetYScale");
        tempMap.put(83, "GetAngle");
        map.put(2, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "CurrentParagraphIndex");
        tempMap.put(81, "CurrentText");
        tempMap.put(82, "GetParagraph");
        tempMap.put(83, "TextAsNumber");
        tempMap.put(84, "ParagraphCount");
        map.put(3, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "CounterValue");
        tempMap.put(81, "CounterMinimumValue");
        tempMap.put(82, "CounterMaximumValue");
        tempMap.put(83, "CounterColor1");
        tempMap.put(84, "CounterColor2");
        map.put(7, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "RTFXPOS");
        tempMap.put(81, "RTFYPOS");
        tempMap.put(82, "RTFSXPAGE");
        tempMap.put(83, "RTFSYPAGE");
        tempMap.put(84, "RTFZOOM");
        tempMap.put(85, "RTFWORDMOUSE");
        tempMap.put(86, "RTFWORDXY");
        tempMap.put(87, "RTFWORD");
        tempMap.put(88, "RTFXWORD");
        tempMap.put(89, "RTFYWORD");
        tempMap.put(90, "RTFSXWORD");
        tempMap.put(91, "RTFSYWORD");
        tempMap.put(92, "RTFLINEMOUSE");
        tempMap.put(93, "RTFLINEXY");
        tempMap.put(94, "RTFXLINE");
        tempMap.put(95, "RTFYLINE");
        tempMap.put(96, "RTFSXLINE");
        tempMap.put(97, "RTFSYLINE");
        tempMap.put(98, "RTFPARAMOUSE");
        tempMap.put(99, "RTFPARAXY");
        tempMap.put(100, "RTFXPARA");
        tempMap.put(101, "RTFYPARA");
        tempMap.put(102, "RTFSXPARA");
        tempMap.put(103, "RTFSYPARA");
        tempMap.put(104, "RTFXWORDTEXT");
        tempMap.put(105, "RTFYWORDTEXT");
        tempMap.put(106, "RTFXLINETEXT");
        tempMap.put(107, "RTFYLINETEXT");
        tempMap.put(108, "RTFXPARATEXT");
        tempMap.put(109, "RTFYPARATEXT");
        tempMap.put(110, "RTFMEMSIZE");
        tempMap.put(111, "RTFGETFOCUSWORD");
        tempMap.put(112, "RTFGETHYPERLINK");
        map.put(8, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "SubApplicationFrameNumber");
        tempMap.put(81, "SubApplicationGlobalValue");
        tempMap.put(82, "SubApplicationGlobalString");
        map.put(9, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "GetMainVolume");
        tempMap.put(1, "GetSampleVolume");
        tempMap.put(2, "GetChannelVolume");
        tempMap.put(3, "GetMainPan");
        tempMap.put(4, "GetSamplePan");
        tempMap.put(5, "GetChannelPan");
        tempMap.put(6, "GetSamplePosition");
        tempMap.put(7, "GetChannelPosition");
        tempMap.put(8, "GetSampleDuration");
        tempMap.put(9, "GetChannelDuration");
        tempMap.put(10, "GetSampleFrequency");
        tempMap.put(11, "GetChannelFrequency");
        map.put(-2, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "PlayerScore");
        tempMap.put(1, "PlayerLives");
        tempMap.put(2, "PlayerInputDevice");
        tempMap.put(3, "PlayerKeyName");
        tempMap.put(4, "PlayerName");
        map.put(-7, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "XMouse");
        tempMap.put(1, "YMouse");
        tempMap.put(2, "MouseWheelValue");
        map.put(-6, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "TotalObjectCount");
        map.put(-5, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "TimerValue");
        tempMap.put(1, "TimerHundreds");
        tempMap.put(2, "TimerSeconds");
        tempMap.put(3, "TimerHours");
        tempMap.put(4, "TimerMinutes");
        tempMap.put(5, "TimerEventIndex");
        map.put(-4, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "CurrentFrameOld");
        tempMap.put(1, "PlayerCount");
        tempMap.put(2, "XLeftFrame");
        tempMap.put(3, "XRightFrame");
        tempMap.put(4, "YTopFrame");
        tempMap.put(5, "YBottomFrame");
        tempMap.put(6, "FrameWidth");
        tempMap.put(7, "FrameHeight");
        tempMap.put(8, "CurrentFrame");
        tempMap.put(9, "GetCollisionMask");
        tempMap.put(10, "FrameRate");
        tempMap.put(11, "GetVirtualWidth");
        tempMap.put(12, "GetVirtualHeight");
        tempMap.put(13, "FrameBackgroundColor");
        tempMap.put(14, "DisplayMode");
        tempMap.put(15, "PixelShaderVersion");
        tempMap.put(16, "FrameAlphaCoefficient");
        tempMap.put(17, "FrameRGBCoefficient");
        tempMap.put(18, "FrameEffectParameter");
        map.put(-3, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "Long");
        tempMap.put(1, "Random");
        tempMap.put(2, "GlobalValueExpression");
        tempMap.put(3, "String");
        tempMap.put(4, "ToString");
        tempMap.put(5, "ToNumber");
        tempMap.put(6, "ApplicationDrive");
        tempMap.put(7, "ApplicationDirectory");
        tempMap.put(8, "ApplicationPath");
        tempMap.put(9, "ApplicationFilename");
        tempMap.put(10, "Sin");
        tempMap.put(11, "Cos");
        tempMap.put(12, "Tan");
        tempMap.put(13, "SquareRoot");
        tempMap.put(14, "Log");
        tempMap.put(15, "Ln");
        tempMap.put(16, "Hex");
        tempMap.put(17, "Bin");
        tempMap.put(18, "Exp");
        tempMap.put(19, "LeftString");
        tempMap.put(20, "RightString");
        tempMap.put(21, "MidString");
        tempMap.put(22, "StringLength");
        tempMap.put(23, "Double");
        tempMap.put(24, "GlobalValue");
        tempMap.put(28, "ToInt");
        tempMap.put(29, "Abs");
        tempMap.put(30, "Ceil");
        tempMap.put(31, "Floor");
        tempMap.put(32, "Acos");
        tempMap.put(33, "Asin");
        tempMap.put(34, "Atan");
        tempMap.put(35, "Not");
        tempMap.put(36, "DroppedFileCount");
        tempMap.put(37, "DroppedFilename");
        tempMap.put(38, "GetCommandLine");
        tempMap.put(39, "GetCommandItem");
        tempMap.put(40, "Min");
        tempMap.put(41, "Max");
        tempMap.put(42, "GetRGB");
        tempMap.put(43, "GetRed");
        tempMap.put(44, "GetGreen");
        tempMap.put(45, "GetBlue");
        tempMap.put(46, "LoopIndex");
        tempMap.put(47, "NewLine");
        tempMap.put(48, "Round");
        tempMap.put(49, "GlobalStringExpression");
        tempMap.put(50, "GlobalString");
        tempMap.put(51, "LowerString");
        tempMap.put(52, "UpperString");
        tempMap.put(53, "Find");
        tempMap.put(54, "ReverseFind");
        tempMap.put(55, "GetClipboard");
        tempMap.put(56, "TemporaryPath");
        tempMap.put(57, "TemporaryBinaryFilePath");
        tempMap.put(58, "FloatToString");
        tempMap.put(59, "Atan2");
        tempMap.put(60, "Zero");
        tempMap.put(61, "Empty");
        tempMap.put(62, "DistanceBetween");
        tempMap.put(63, "AngleBetween");
        tempMap.put(64, "ClampValue");
        tempMap.put(65, "RandomRange");
        tempMap.put(-1, "Parenthesis");
        tempMap.put(-3, "Virgule");
        tempMap.put(-2, "EndParenthesis");
        map.put(-1, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(81, "SteamAccountUserId");
        tempMap.put(82, "SteamAccountUserName");
        map.put(39, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(107, "GetDataDirectory");
        map.put(42, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(88, "GetItemValue");
        tempMap.put(89, "GetItemString");
        map.put(47, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(85, "GroupItemString");
        map.put(63, tempMap);

        extensionMap.put(16, "ExtensionValue");
        extensionMap.put(19, "ExtensionString");
    }


    public static String getByID(int categoryID, int itemID) {
        if (map.containsKey(categoryID) && map.get(categoryID).containsKey(itemID)) {
            return map.get(categoryID).get(itemID);
        } else {
            return null;
        }
    }

    public static String getByExtensionID(int itemID) {
        return extensionMap.get(itemID);
    }
}
