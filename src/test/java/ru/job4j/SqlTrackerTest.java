package ru.job4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.store.SqlTracker;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SqlTrackerTest {

    private static Connection connection;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader()
                .getResourceAsStream("test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ITEMS")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("item"));
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void whenCreateItem() {
        SqlTracker tracker = new SqlTracker(connection);
        tracker.add(new Item("name"));
        assertThat(tracker.findByName("name").size(), is(1));
    }

    @Test
    public void whenReplaceItem() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("Name"));
        Item second = tracker.add(new Item("Leo"));
        Item check = new Item("Mario");
        tracker.replace(second.getId(), check);
        tracker.replace(first.getId(), check);
        assertThat(tracker.findById(second.getId()).getName(), is("Mario"));
        assertThat(tracker.findById(first.getId()).getName(), is("Mario"));
    }

    @Test
    public void whenDeleteItem() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("name"));
        assertTrue(tracker.delete(item.getId()));
        assertNull(tracker.findById(item.getId()));
    }

    @Test
    public void whenfindItemById() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("name"));
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void findItemByName() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("name"));
        Item second = tracker.add(new Item("Leo"));
        Item third = tracker.add(new Item("Leo"));
        assertThat(tracker.findByName("Leo"), is(List.of(second, third)));
    }

    @Test
    public void whenFindAll() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("name"));
        Item second = tracker.add(new Item("Leo"));
        assertThat(tracker.findAll(), is(List.of(first, second)));
    }

    @Test
    public void whenFindAllReact() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("name"));
        Item second = tracker.add(new Item("Leo"));
        List<String> list = new ArrayList<>();
        tracker.getByReact(item -> list.add(item.getName()));
        assertThat(List.of(first.getName(), second.getName()),
                is(list));
    }
}