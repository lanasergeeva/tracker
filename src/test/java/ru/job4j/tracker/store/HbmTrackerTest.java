package ru.job4j.tracker.store;

import org.junit.Test;
import ru.job4j.tracker.model.Item;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import java.util.List;

public class HbmTrackerTest {

    @Test
    public void whenCreate() {
        HbmTracker hs = new HbmTracker();
        Item item = new Item("item1");
        hs.add(item);
        List<Item> all = hs.findAll();
        assertEquals(item, all.get(0));
    }

    @Test
    public void whenFindAll() {
        HbmTracker hs = new HbmTracker();
        Item one = new Item("item1");
        Item two = new Item("item2");
        hs.add(one);
        hs.add(two);
        assertEquals(List.of(one, two), hs.findAll());
    }

    @Test
    public void whenReplace() {
        HbmTracker hs = new HbmTracker();
        Item one = new Item("item1");
        Item two = new Item("item2");
        hs.add(one);
        hs.replace(one.getId(), two);
        assertEquals(hs.findAll().get(0), two);
    }

    @Test
    public void whenDelete() {
        HbmTracker hs = new HbmTracker();
        Item one = new Item("item1");
        hs.add(one);
        assertTrue(hs.delete(one.getId()));
        assertThat(hs.findById(one.getId()), is(nullValue()));
    }

    @Test
    public void whenFindById() {
        HbmTracker hs = new HbmTracker();
        Item one = new Item("item1");
        hs.add(one);
        assertEquals(hs.findById(one.getId()), one);
    }

    @Test
    public void whenFindByName() {
        HbmTracker hs = new HbmTracker();
        Item one = new Item("item1");
        hs.add(one);
        assertEquals(hs.findByName("item1"), List.of(one));
    }
}