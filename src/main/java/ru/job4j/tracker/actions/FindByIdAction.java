package ru.job4j.tracker.actions;

import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;

import java.util.Objects;

public class FindByIdAction implements UserAction {

    private final Output out;

    public FindByIdAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Find item by Id";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Find item by Id ===");
        int id = input.askInt(" Enter number of id for searching ");
        Item itemFind = tracker.findById(id);
        out.println(Objects.requireNonNullElse(itemFind, "Заявка с таким id не найдена"));
        return true;
    }
}
