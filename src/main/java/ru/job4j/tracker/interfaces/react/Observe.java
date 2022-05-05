package ru.job4j.tracker.interfaces.react;

public interface Observe<T> {
    void receive(T model);
}
