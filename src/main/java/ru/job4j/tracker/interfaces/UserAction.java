package ru.job4j.tracker.interfaces;

public interface UserAction {
    String name();

    boolean execute(Input input, Store tracker);
}
