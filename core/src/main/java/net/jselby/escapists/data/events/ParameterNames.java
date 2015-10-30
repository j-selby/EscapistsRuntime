package net.jselby.escapists.data.events;

import java.util.HashMap;
import java.util.Map;

/**
 * The names of Parameters within the engine.
 */
public class ParameterNames {
    private final static Map<Integer, String> map = new HashMap<Integer, String>();

    static {
        map.put(1, "OBJECT");
        map.put(2, "TIME");
        map.put(3, "SHORT");
        map.put(4, "SHORT");
        map.put(5, "INT");
        map.put(6, "SAMPLE");
        map.put(7, "SAMPLE");
        map.put(9, "CREATE");
        map.put(10, "SHORT");
        map.put(11, "SHORT");
        map.put(12, "SHORT");
        map.put(13, "Every");
        map.put(14, "KEY");
        map.put(15, "EXPRESSION");
        map.put(16, "POSITION");
        map.put(17, "JOYDIRECTION");
        map.put(18, "SHOOT");
        map.put(19, "ZONE");
        map.put(21, "SYSCREATE");
        map.put(22, "EXPRESSION");
        map.put(23, "COMPARISON");
        map.put(24, "COLOUR");
        map.put(25, "BUFFER4");
        map.put(26, "FRAME");
        map.put(27, "SAMLOOP");
        map.put(28, "MUSLOOP");
        map.put(29, "NEWDIRECTION");
        map.put(31, "TEXTNUMBER");
        map.put(32, "Click");
        map.put(33, "PROGRAM");
        map.put(34, "OLDPARAM_VARGLO");
        map.put(35, "CNDSAMPLE");
        map.put(36, "CNDMUSIC");
        map.put(37, "REMARK");
        map.put(38, "GROUP");
        map.put(39, "GROUPOINTER");
        map.put(40, "FILENAME");
        map.put(41, "STRING");
        map.put(42, "CMPTIME");
        map.put(43, "PASTE");
        map.put(44, "VMKEY");
        map.put(45, "EXPSTRING");
        map.put(46, "CMPSTRING");
        map.put(47, "INKEFFECT");
        map.put(48, "MENU");
        map.put(49, "GlobalValue");
        map.put(50, "AlterableValue");
        map.put(51, "FLAG");
        map.put(52, "VARGLOBAL_EXP");
        map.put(53, "AlterableValueExpression");
        map.put(54, "FLAG_EXP");
        map.put(55, "EXTENSION");
        map.put(56, "8DIRECTIONS");
        map.put(57, "MVT");
        map.put(58, "GlobalString");
        map.put(59, "STRINGGLOBAL_EXP");
        map.put(60, "PROGRAM2");
        map.put(61, "ALTSTRING");
        map.put(62, "ALTSTRING_EXP");
        map.put(63, "FILENAME");
        map.put(64, "FASTLOOPNAME");
        map.put(65, "CHAR_ENCODING_INPUT");
    }

    public static String getByID(int id) {
        return map.get(id);
    }
}
