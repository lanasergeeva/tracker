package ru.job4j.tracker.actions;

import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.model.Item;

public class ManyItemsActions implements UserAction {
    private final Output out;

    public ManyItemsActions(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Create many items";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Create many items ====");
        for (int i = 0; i < 100000000; i++) {
            tracker.add(new Item("Item"));
        }
        return true;
    }
}
