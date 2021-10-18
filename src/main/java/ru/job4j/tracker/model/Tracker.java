package ru.job4j.tracker.model;

import java.util.Arrays;

public class Tracker {
    private final Item[] items = new Item[100];
    private int ids = 1;
    private int size = 0;

    public Item add(Item item) {
        item.setId(ids++);
        items[size++] = item;
        return item;
    }

    public Item findById(int id) {
        int index = indexOf(id);
        return index != -1 ? items[index] : null;
    }

    public Item[] findByName(String key) {
        Item[] itemsTwo = new Item[size];
        int temp = 0;
        for (int i = 0; i < this.size; i++) {
            Item item = items[i];
            if (item.getName().equals(key)) {
                itemsTwo[temp] = item;
                temp++;
            }
        }
        return Arrays.copyOf(itemsTwo, temp);
    }

    public Item[] findAll() {
        return Arrays.copyOf(items, size);
    }

    private int indexOf(int id) {
        int rsl = -1;
        for (int index = 0; index < size; index++) {
            if (items[index].getId() == id) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }

    public boolean replace(int id, Item item) {
        int index = indexOf(id);
        boolean rsl = index != -1;
        if (rsl) {
            item.setId(id);
            items[index] = item;
        }
        return rsl;
    }

    public boolean delete(int id) {
        int index = indexOf(id);
        int temp = size - index;
        boolean rsl = index != -1;
        if (rsl) {
            items[index] = null;
            System.arraycopy(items, index + 1, items, index, temp);
            size--;
        }
        return rsl;
    }
}
