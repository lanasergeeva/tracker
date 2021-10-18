package ru.job4j.tracker.actions;

import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.UserAction;
import ru.job4j.tracker.interfaces.Input;
import ru.job4j.tracker.interfaces.Output;

public class ExitAction implements UserAction {
    private final Output out;

    public ExitAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Exit";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Exit Program ====");
        return false;
    }
}
