package ru.job4j.tracker.store;

import ru.job4j.tracker.interfaces.react.Observe;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.interfaces.Store;

import java.util.ArrayList;
import java.util.List;

public class MemTracker implements Store {

    private final List<Item> items = new ArrayList<>();
    private int ids = 1;
    private int size = 0;

    @Override
    public void init() {

    }

    @Override
    public Item add(Item item) {
        item.setId(ids++);
        items.add(size++, item);
        return item;
    }

    @Override
    public Item findById(int id) {
        int index = indexOf(id);
        return index != -1 ? items.get(index) : null;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> itemName = new ArrayList<>();
        for (Item names : items) {
            if (names.getName().equals(key)) {
                itemName.add(names);
            }
        }
        return itemName;
    }

    @Override
    public List<Item> findAll() {
        return items;
    }

    private int indexOf(int id) {
        int rsl = -1;
        for (int index = 0; index < size; index++) {
            if (items.get(index).getId() == id) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }

    @Override
    public boolean replace(int id, Item item) {
        int index = indexOf(id);
        boolean rsl = index != -1;
        if (rsl) {
            item.setId(id);
            items.set(index, item);
        }
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        int index = indexOf(id);
        boolean rsl = index != -1;
        if (rsl) {
            items.remove(index);
            size--;
        }
        return rsl;
    }

    @Override
    public void getByReact(Observe<Item> observe) {
        List<Item> all = findAll();
        if (all.size() > 0) {
            for (Item item : all) {
                observe.receive(item);
            }
        }
    }

    @Override
    public void close() {

    }
}
