package ru.job4j.tracker.actions;

import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.model.Item;

import java.util.List;

public class DeleteManyItems implements UserAction {
    private final Output out;

    public DeleteManyItems(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Delete many items";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        int id;
        List<Item> all = tracker.findAll();
        if (all.size() <= 0) {
            out.println("Хранилище пустое");
            return true;
        }
        id = all.get(0).getId();
        out.println("=== Delete many items ====");
        for (int i = id; i < 10000000; i++) {
            tracker.delete(i);
        }
        return true;
    }
}
