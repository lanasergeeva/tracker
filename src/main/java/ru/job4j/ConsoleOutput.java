package ru.job4j;

import ru.job4j.tracker.interfaces.Output;

public class ConsoleOutput implements Output {

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }
}
