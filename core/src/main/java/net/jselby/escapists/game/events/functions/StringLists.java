package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.FunctionCollection;

/**
 * StringList functions store and modify Lists, which contain a sequence of Strings.
 */
public class StringLists extends FunctionCollection {
    @Action(subId = 32, id = 85)
    public void ResetList() {
        for (ObjectInstance object : scope.getObjects()) {
            object.getListElements().clear();
            object.setSelectedLine(0);
        }
    }

    @Action(subId = 32, id = 89)
    public void SelectLine(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("Number must be positively indexed: " + num);
        }
        for (ObjectInstance object : scope.getObjects()) {
            if (num > object.getListElements().size()) {
                throw new IllegalArgumentException("Selected line exceeds list capacity: " + num);
            }
            object.setSelectedLine(num);
        }
    }

    @Action(subId = 34, id = 80)
    public void SplitString(String content, String delimiter) {
        String[] split = content.split(delimiter);
        ObjectInstance[] objects = scope.getObjects();
        for (ObjectInstance object : objects) {
            object.getListElements().clear();
        }

        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (str.length() == 0) {
                continue;
            }
            for (ObjectInstance object : objects) {
                object.getListElements().add(str);
                //object.setSelectedLine(object.getListElements().size() - 1);
            }
        }
    }

    @Action(subId = 32, id = 86)
    public void AddListElement(String content) {
        for (ObjectInstance object : scope.getObjects()) {
            object.getListElements().add(content);
            object.setSelectedLine(object.getListElements().size() - 1);
        }
    }
}
