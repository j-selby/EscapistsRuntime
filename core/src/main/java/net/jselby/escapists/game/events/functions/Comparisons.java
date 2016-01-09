package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.Conditions;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * Comparison functions, that compare stored values with each other.
 */
public class Comparisons extends FunctionCollection {
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

    @Condition(subId = 7, id = -81)
    public boolean CompareCounter(int val1, int val2) {
        // TODO: Compare counters
        return true;
    }
}
