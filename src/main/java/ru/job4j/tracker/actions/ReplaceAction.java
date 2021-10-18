package ru.job4j.tracker.actions;

import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;

public class ReplaceAction implements UserAction {

    private final Output out;

    public ReplaceAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "=== Edit item ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        int id = input.askInt("Enter id: ");
        String nameEnter = input.askStr(" Enter name: ");
        Item itemEdit = new Item(nameEnter);
        itemEdit.setName(nameEnter);
        if (tracker.replace(id, itemEdit)) {
           out.println("successful");
        } else {
            out.println("failed");
        }
        return true;
    }
}
