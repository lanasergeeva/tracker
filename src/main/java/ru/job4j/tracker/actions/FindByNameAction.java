package ru.job4j.tracker.actions;

import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;

import java.util.List;

public class FindByNameAction implements UserAction {

    private final Output out;

    public FindByNameAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Find item by Name";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Find item by Name ===");
        String nameFind = input.askStr(" Enter name for searching ");
        List<Item> findName = tracker.findByName(nameFind);
        if (findName.size() > 0) {
            for (Item itemFindName : findName) {
                out.println(itemFindName);
            }
        } else {
            out.println("Заявка с таким id не найдена");
        }
        return true;
    }
}