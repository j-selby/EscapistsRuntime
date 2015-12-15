package net.jselby.escapists.data.events;

import java.util.HashMap;
import java.util.Map;

/**
 * Names of various Actions within the Events scripting "language".
 *
 * Modified from mmfparser - https://github.com/matpow2/anaconda
 */
public class ActionNames {
    private final static Map<Integer, Map<Integer, String>> map = new HashMap<Integer, Map<Integer, String>>();

    static {
        Map<Integer, String> tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "PasteActive");
        tempMap.put(81, "BringToFront"); // BringActiveToFront
        tempMap.put(82, "BringToBack"); // BringActiveToBack
        tempMap.put(83, "AddBackdrop");
        tempMap.put(84, "ReplaceColor");
        tempMap.put(85, "SetScale");
        tempMap.put(86, "SetXScale");
        tempMap.put(87, "SetYScale");
        tempMap.put(88, "SetAngle");
        tempMap.put(89, "LoadActiveFrame");
        map.put(2, tempMap);

        tempMap = new HashMap<Integer, String>(); // String
        tempMap.put(80, "EraseText");
        tempMap.put(81, "DisplayText");
        tempMap.put(82, "FlashText");
        tempMap.put(83, "SetTextColor");
        tempMap.put(84, "SetParagraph");
        tempMap.put(85, "PreviousParagraph");
        tempMap.put(86, "NextParagraph");
        tempMap.put(87, "DisplayAlterableString");
        tempMap.put(88, "SetString");

        tempMap.put(26, "Disappear"); // Extension
        tempMap.put(27, "Reappear"); // Extension
        map.put(3, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "AskQuestion");
        map.put(4, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "SetCounterValue");
        tempMap.put(81, "AddCounterValue");
        tempMap.put(82, "SubtractCounterValue");
        tempMap.put(83, "SetMinimumValue");
        tempMap.put(84, "SetMaximumValue");
        tempMap.put(85, "SetCounterColor1");
        tempMap.put(86, "SetCounterColor2");
        map.put(7, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "RTFSETXPOS");
        tempMap.put(81, "RTFSETYPOS");
        tempMap.put(82, "RTFSETZOOM");
        tempMap.put(83, "RTFSELECT_CLEAR");
        tempMap.put(84, "RTFSELECT_WORDSTRONCE");
        tempMap.put(85, "RTFSELECT_WORDSTRNEXT");
        tempMap.put(86, "RTFSELECT_WORDSTRALL");
        tempMap.put(87, "RTFSELECT_WORD");
        tempMap.put(88, "RTFSELECT_LINE");
        tempMap.put(89, "RTFSELECT_PARAGRAPH");
        tempMap.put(90, "RTFSELECT_PAGE");
        tempMap.put(91, "RTFSELECT_ALL");
        tempMap.put(92, "RTFSELECT_RANGE");
        tempMap.put(93, "RTFSELECT_BOOKMARK");
        tempMap.put(94, "RTFSETFOCUSWORD");
        tempMap.put(95, "RTFHLIGHT_OFF");
        tempMap.put(96, "RTFHLIGHTTEXT_COLOR");
        tempMap.put(97, "RTFHLIGHTTEXT_BOLD");
        tempMap.put(98, "RTFHLIGHTTEXT_ITALIC");
        tempMap.put(99, "RTFHLIGHTTEXT_UNDERL");
        tempMap.put(100, "RTFHLIGHTTEXT_OUTL");
        tempMap.put(101, "RTFHLIGHTBACK_COLOR");
        tempMap.put(102, "RTFHLIGHTBACK_RECT");
        tempMap.put(103, "RTFHLIGHTBACK_MARKER");
        tempMap.put(104, "RTFHLIGHTBACK_HATCH");
        tempMap.put(105, "RTFHLIGHTBACK_INVERSE");
        tempMap.put(106, "RTFDISPLAY");
        tempMap.put(107, "RTFSETFOCUSPREV");
        tempMap.put(108, "RTFSETFOCUSNEXT");
        tempMap.put(109, "RTFREMOVEFOCUS");
        tempMap.put(110, "RTFAUTOON");
        tempMap.put(111, "RTFAUTOOFF");
        tempMap.put(112, "RTFINSERTSTRING");
        tempMap.put(113, "RTFLOADTEXT");
        tempMap.put(114, "RTFINSERTTEXT");
        map.put(8, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "RestartSubApplication");
        tempMap.put(81, "RestartSubApplicationFrame");
        tempMap.put(82, "NextSubApplicationFrame");
        tempMap.put(83, "PreviousSubApplicationFrame");
        tempMap.put(84, "EndSubApplication");
        tempMap.put(85, "LoadApplication");
        tempMap.put(86, "JumpSubApplicationFrame");
        tempMap.put(87, "SetSubApplicationGlobalValue");
        tempMap.put(88, "ShowSubApplication");
        tempMap.put(89, "HideSubApplication");
        tempMap.put(90, "SetSubApplicationGlobalString");
        tempMap.put(91, "PauseSubApplication");
        tempMap.put(92, "ResumeSubApplication");
        map.put(9, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "Skip");
        tempMap.put(1, "SKIPMONITOR");
        tempMap.put(2, "ExecuteFixedProgram");
        tempMap.put(3, "SetGlobalValue");
        tempMap.put(4, "SubtractGlobalValue");
        tempMap.put(5, "AddGlobalValue");
        tempMap.put(6, "ActivateGroup");
        tempMap.put(7, "DeactivateGroup");
        tempMap.put(8, "ActivateMenu");
        tempMap.put(9, "DeactivateMenu");
        tempMap.put(10, "CheckMenu");
        tempMap.put(11, "UncheckMenu");
        tempMap.put(12, "ShowMenu");
        tempMap.put(13, "HideMenu");
        tempMap.put(14, "StartLoop");
        tempMap.put(15, "StopLoop");
        tempMap.put(16, "SetLoopIndex");
        tempMap.put(17, "SetRandomSeed");
        tempMap.put(18, "SendMenuCommand");
        tempMap.put(19, "SetGlobalString");
        tempMap.put(20, "SetClipboard");
        tempMap.put(21, "ClearClipboard");
        tempMap.put(22, "ExecuteEvaluatedProgram");
        tempMap.put(23, "OpenDebugger");
        tempMap.put(24, "PauseDebugger");
        tempMap.put(25, "ExtractBinaryFile");
        tempMap.put(26, "ReleaseBinaryFile");
        tempMap.put(27, "SetGlobalValueInt");
        tempMap.put(28, "SetGlobalValue2");
        tempMap.put(29, "SetGlobalValueDouble");
        tempMap.put(30, "SetGlobalValue3");
        tempMap.put(31, "AddGlobalValueInt");
        tempMap.put(32, "AddGlobalValue2");
        tempMap.put(33, "AddGlobalValueDouble");
        tempMap.put(34, "AddGlobalValue3");
        tempMap.put(35, "SubtractGlobalValueInt");
        tempMap.put(36, "SubtractGlobalValue2");
        tempMap.put(37, "SubtractGlobalValueDouble");
        tempMap.put(38, "SubtractGlobalValue3");
        map.put(-1, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "SetScore");
        tempMap.put(1, "SetLives");
        tempMap.put(2, "IgnoreControls");
        tempMap.put(3, "RestoreControls");
        tempMap.put(4, "AddScore");
        tempMap.put(5, "AddLives");
        tempMap.put(6, "SubtractScore");
        tempMap.put(7, "SubtractLives");
        tempMap.put(8, "ChangeControlType");
        tempMap.put(9, "ChangeInputKey");
        tempMap.put(10, "SetPlayerName");
        map.put(-7, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "HideCursor");
        tempMap.put(1, "ShowCursor");
        map.put(-6, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "CreateObject");
        tempMap.put(1, "CreateObjectByName");
        map.put(-5, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "SetTimer");
        tempMap.put(1, "ScheduleEvent");
        tempMap.put(2, "ScheduleEventTimes");
        map.put(-4, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(0, "NextFrame");
        tempMap.put(1, "PreviousFrame");
        tempMap.put(2, "JumpToFrame");
        tempMap.put(3, "PauseApplication");
        tempMap.put(4, "EndApplication");
        tempMap.put(5, "RestartApplication");
        tempMap.put(6, "RestartFrame");
        tempMap.put(7, "CenterDisplay");
        tempMap.put(8, "CenterDisplayX");
        tempMap.put(9, "CenterDisplayY");
        tempMap.put(10, "LOADGAME");
        tempMap.put(11, "SAVEGAME");
        tempMap.put(12, "ClearScreen");
        tempMap.put(13, "ClearZone");
        tempMap.put(14, "FullscreenMode");
        tempMap.put(15, "WindowedMode");
        tempMap.put(16, "SetFrameRate");
        tempMap.put(17, "PauseApplicationWithKey");
        tempMap.put(18, "PauseApplication");
        tempMap.put(19, "EnableVsync");
        tempMap.put(20, "DisableVsync");
        tempMap.put(21, "SetVirtualWidth");
        tempMap.put(22, "SetVirtualHeight");
        tempMap.put(23, "SetFrameBackgroundColor");
        tempMap.put(24, "DeleteCreatedBackdrops");
        tempMap.put(25, "DeleteAllCreatedBackdrops");
        tempMap.put(26, "SetFrameWidth");
        tempMap.put(27, "SetFrameHeight");
        tempMap.put(28, "SaveFrame");
        tempMap.put(29, "LoadFrame");
        tempMap.put(30, "LoadApplication");
        tempMap.put(31, "PlayDemo");
        tempMap.put(32, "SetFrameEffect");
        tempMap.put(33, "SetFrameEffectParameter");
        tempMap.put(34, "SetFrameEffectImage");
        tempMap.put(35, "SetFrameAlphaCoefficient");
        tempMap.put(36, "SetFrameRGBCoefficient");
        map.put(-3, tempMap);

        tempMap = new HashMap<Integer, String>(); // Sound and Music
        tempMap.put(0, "PlaySample");
        tempMap.put(1, "StopAllSamples");
        tempMap.put(2, "PlayMusic");
        tempMap.put(3, "StopMusic");
        tempMap.put(4, "PlayLoopingSample");
        tempMap.put(5, "PlayLoopingMusic");
        tempMap.put(6, "StopSample");
        tempMap.put(7, "PauseSample");
        tempMap.put(8, "ResumeSample");
        tempMap.put(9, "PauseMusic");
        tempMap.put(10, "ResumeMusic");
        tempMap.put(11, "PlayChannelSample");
        tempMap.put(12, "PlayLoopingChannelSample");
        tempMap.put(13, "PauseChannel");
        tempMap.put(14, "ResumeChannel");
        tempMap.put(15, "StopChannel");
        tempMap.put(16, "SetChannelPosition");
        tempMap.put(17, "SetChannelVolume");
        tempMap.put(18, "SetChannelPan");
        tempMap.put(19, "SetSamplePosition");
        tempMap.put(20, "SetMainVolume");
        tempMap.put(21, "SetSampleVolume");
        tempMap.put(22, "SetMainPan");
        tempMap.put(23, "SetSamplePan");
        tempMap.put(24, "PauseAllSounds");
        tempMap.put(25, "ResumeAllSounds");
        tempMap.put(26, "PlayMusicFile");
        tempMap.put(27, "PlayLoopingMusicFile");
        tempMap.put(28, "PlayChannelFileSample");
        tempMap.put(29, "PlayLoopingChannelFileSample");
        tempMap.put(30, "LockChannel");
        tempMap.put(31, "UnlockChannel");
        tempMap.put(32, "SetChannelFrequency");
        tempMap.put(33, "SetSampleFrequency");
        map.put(-2, tempMap);

        // Custom Extensions
        tempMap = new HashMap<Integer, String>();
        tempMap.put(82, "CreateDirectory");
        map.put(42, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(158, "LoadPropertiesFile");
        map.put(47, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(86, "LoadIniFile");
        map.put(63, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "EmbedFont");
        map.put(65, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(95, "CreateDirectory");
        map.put(66, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(82, "CreateDirectory");
        map.put(13, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(2, "SetX");
        tempMap.put(3, "SetY");
        tempMap.put(17, "SetAnimation");
        tempMap.put(23, "SetDirection");
        tempMap.put(35, "FlagOn");
        tempMap.put(36, "FlagOff");
        tempMap.put(57, "BringToFront");
        map.put(2, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(80, "OpenURL");
        map.put(56, tempMap);

        tempMap = new HashMap<Integer, String>();
        tempMap.put(91, "ClearObjectVarArray");
        tempMap.put(80, "SetIntegerVar");
        tempMap.put(81, "AddIntegerVar");
        tempMap.put(82, "SubtractIntegerVar");
        tempMap.put(88, "SetStringVar");
        map.put(36, tempMap);

        /*extensionMap.put(1, "SetPosition");
        extensionMap.put(2, "SetX");
        extensionMap.put(3, "SetY");
        extensionMap.put(4, "Stop");
        extensionMap.put(5, "Start");
        extensionMap.put(6, "SetSpeed");
        extensionMap.put(7, "SetMaximumSpeed");
        extensionMap.put(8, "Wrap");
        extensionMap.put(9, "Bounce");
        extensionMap.put(10, "Reverse");
        extensionMap.put(11, "NextMovement");
        extensionMap.put(12, "PreviousMovement");
        extensionMap.put(13, "SelectMovement");
        extensionMap.put(14, "LookAt");
        extensionMap.put(15, "StopAnimation");
        extensionMap.put(16, "StartAnimation");
        extensionMap.put(17, "ForceAnimation");
        extensionMap.put(18, "ForceDirection");
        extensionMap.put(19, "ForceSpeed");
        extensionMap.put(20, "RestoreAnimation");
        extensionMap.put(21, "RestoreDirection");
        extensionMap.put(22, "RestoreSpeed");
        extensionMap.put(23, "SetDirection");
        extensionMap.put(24, "Destroy");
        extensionMap.put(25, "SwapPosition");
        extensionMap.put(26, "Hide");
        extensionMap.put(27, "Show");
        extensionMap.put(28, "FlashDuring");
        extensionMap.put(29, "Shoot");
        extensionMap.put(30, "ShootToward");
        extensionMap.put(31, "SetAlterableValue");
        extensionMap.put(32, "AddToAlterable");
        extensionMap.put(33, "SubtractFromAlterable");
        extensionMap.put(34, "SpreadValue");
        extensionMap.put(35, "EnableFlag");
        extensionMap.put(36, "DisableFlag");
        extensionMap.put(37, "ToggleFlag");
        extensionMap.put(38, "SetInkEffect");
        extensionMap.put(39, "SetSemiTransparency");
        extensionMap.put(40, "ForceFrame");
        extensionMap.put(41, "RestoreFrame");
        extensionMap.put(42, "SetAcceleration");
        extensionMap.put(43, "SetDeceleration");
        extensionMap.put(44, "SetRotatingSpeed");
        extensionMap.put(45, "SetDirections");
        extensionMap.put(46, "BranchNode");
        extensionMap.put(47, "SetGravity");
        extensionMap.put(48, "GoToNode");
        extensionMap.put(49, "SetAlterableString");
        extensionMap.put(50, "SetFontName");
        extensionMap.put(51, "SetFontSize");
        extensionMap.put(52, "SetBold");
        extensionMap.put(53, "SetItalic");
        extensionMap.put(54, "SetUnderline");
        extensionMap.put(55, "SetStrikeOut");
        extensionMap.put(56, "SetTextColor");
        extensionMap.put(57, "BringToFront");
        extensionMap.put(58, "BringToBack");
        extensionMap.put(59, "MoveBehind");
        extensionMap.put(60, "MoveInFront");
        extensionMap.put(61, "MoveToLayer");
        extensionMap.put(62, "AddToDebugger");
        extensionMap.put(63, "SetEffect");
        extensionMap.put(64, "SetEffectParameter");
        extensionMap.put(65, "SetAlphaCoefficient");
        extensionMap.put(66, "SetRGBCoefficient");
        extensionMap.put(67, "SetEffectImage");
        extensionMap.put(68, "SetFriction");
        extensionMap.put(69, "SetElasticity");
        extensionMap.put(70, "ApplyImpulse");
        extensionMap.put(71, "ApplyAngularImpulse");
        extensionMap.put(72, "ApplyForce");
        extensionMap.put(73, "ApplyTorque");
        extensionMap.put(74, "SetLinearVelocity");
        extensionMap.put(75, "SetAngularVelocity");
        extensionMap.put(76, "Foreach");
        extensionMap.put(77, "ForeachTwoObjects");
        extensionMap.put(78, "StopForce");
        extensionMap.put(79, "StopTorque");
        //extensionMap.put(80, "SetDensity");
        extensionMap.put(81, "SetGravityScale");*/
    }

    public static String getByID(int categoryID, int itemID) {
        if (map.containsKey(categoryID) && map.get(categoryID).containsKey(itemID)) {
            return map.get(categoryID).get(itemID);
        } else {
            return null;
        }
    }
}
