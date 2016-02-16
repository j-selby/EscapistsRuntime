package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.Conditions;
import net.jselby.escapists.game.events.FunctionCollection;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Comparison functions, that compare stored values with each other.
 */
public class Comparisons extends FunctionCollection {
    @Condition(subId = -1, id = -3, conditionRequired = true)
    public boolean Compare(int comparison1, Object str1, int comparison2, Object str2) {
        if (str1 instanceof String) {
            switch (comparison2) {
                case 0:  // EQUAL
                    return str1.equals(str2);
                case 1:  // DIFFERENT
                    return !str1.equals(str2);
                default:
                    throw new IllegalArgumentException("Cannot parse comparsion.");
            }
        }

        double num1 = ((Number) str1).doubleValue();
        double num2 = ((Number) str2).doubleValue();

        // TODO: comparison1, what does it do?
        switch (comparison2) {
            case 0:  // EQUAL
                return num1 == num2;
            case 1:  // DIFFERENT
                return num1 != num2;
            case 2:  // LOWER_OR_EQUAL
                return num1 <= num2;
            case 3:  // LOWER
                return num1 < num2;
            case 4:  // GREATER_OR_EQUAL
                return num1 >= num2;
            case 5:  // GREATER
                return num1 > num2;
            default:
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
    public boolean CompareAlterableString(int key, String string) {
        for (ObjectInstance item : scope.getObjects()) {
            return string.equals(item.getAlterableStrings()[key]);
        }
        return false;
    }

    @Condition(subId = 61, id = -27)
    public boolean CompareAlterableValue(int key, double value) {
        for (ObjectInstance item : scope.getObjects()) {
            return value == item.getAlterableValues()[key];
        }
        return false;
    }

    @Condition(subId = 2, id = -42, conditionRequired = true)
    public boolean CompareAlterableValueInt(int id, int comparisonType, int value) {
        for (ObjectInstance item : scope.getObjects()) {
            switch (comparisonType) {
                case 0: // EQUAL
                    return value == item.getAlterableValues()[id];
                case 1: // DIFFERENT
                    return value != item.getAlterableValues()[id];
                case 2: // LOWER_OR_EQUAL
                    return value <= item.getAlterableValues()[id];
                case 3: // LOWER
                    return value < item.getAlterableValues()[id];
                case 4: // GREATER_OR_EQUAL
                    return value >= item.getAlterableValues()[id];
                case 5: // GREATER
                    return value > item.getAlterableValues()[id];
                default:
                    return false;
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
            switch (comparisonType) {
                case 0:  // EQUAL
                    return instance.getX() == x;
                case 1:  // DIFFERENT
                    return instance.getX() != x;
                case 2:  // LOWER_OR_EQUAL
                    return instance.getX() <= x;
                case 3:  // LOWER
                    return instance.getX() < x;
                case 4:  // GREATER_OR_EQUAL
                    return instance.getX() >= x;
                case 5:  // GREATER
                    return instance.getX() > x;
                default:
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
            switch (comparisonType) {
                case 0:  // EQUAL
                    return instance.getY() == y;
                case 1:  // DIFFERENT
                    return instance.getY() != y;
                case 2:  // LOWER_OR_EQUAL
                    return instance.getY() <= y;
                case 3:  // LOWER
                    return instance.getY() < y;
                case 4:  // GREATER_OR_EQUAL
                    return instance.getY() >= y;
                case 5:  // GREATER
                    return instance.getY() > y;
                default:
                    return false;
            }
        }
        return true;
    }

    @Condition(subId = 7, id = -81)
    public boolean CompareCounter(int val1, int val2) {
        // TODO: Compare counters
        throw new NotImplementedException("CompareCounter:" + val1 + ":" + val2);
    }
}
