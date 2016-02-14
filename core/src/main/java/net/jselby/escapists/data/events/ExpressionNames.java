package net.jselby.escapists.data.events;

import java.util.HashMap;
import java.util.Map;

/**
 * The names of expressions.
 */
public class ExpressionNames {
    private final static Map<Integer, Map<Integer, String>> map = new HashMap<Integer, Map<Integer, String>>();

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
        tempMap.put(0, "Long");
        tempMap.put(3, "String");
        tempMap.put(23, "Double");
        tempMap.put(-1, "Parenthesis");
        tempMap.put(-3, "Virgule");
        tempMap.put(-2, "EndParenthesis");
        map.put(-1, tempMap);
    }


    public static String getByID(int categoryID, int itemID) {
        if (map.containsKey(categoryID) && map.get(categoryID).containsKey(itemID)) {
            return map.get(categoryID).get(itemID);
        } else {
            return null;
        }
    }
}
