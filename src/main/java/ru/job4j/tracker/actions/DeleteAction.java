package ru.job4j.tracker.actions;

import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;

public class DeleteAction implements UserAction {

    private final Output out;

    public DeleteAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "=== Delete item ===";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        int id = input.askInt(" Enter number of id for delete ");
        if (tracker.delete(id)) {
            out.println("successful");
        } else {
            out.println("failed");
        }
        return true;
    }
}
