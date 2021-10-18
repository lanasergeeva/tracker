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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void createItem() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        tracker.add(new Item("name"));
        assertThat(tracker.findByName("name").size(), is(1));
    }

    @Test
    public void replaceItem() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("name");
        Item item2 = new Item("Leo");
        Item rsl = new Item("Mario");
        tracker.add(item1);
        tracker.add(item2);
        tracker.replace(item2.getId(), rsl);
        assertThat(tracker.findById(item2.getId()).getName(), is("Mario"));
    }

    @Test
    public void deleteItem() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("name");
        Item item2 = new Item("Leo");
        tracker.add(item1);
        tracker.add(item2);
        assertTrue(tracker.delete(item2.getId()));
    }

    @Test
    public void findItemById() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("name");
        Item item2 = new Item("Leo");
        tracker.add(item1);
        tracker.add(item2);
        assertThat(tracker.findById(item2.getId()), is(item2));
    }

    @Test
    public void findItemByName() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("name");
        Item item2 = new Item("Leo");
        Item item3 = new Item("Leo");
        List<Item> list = new ArrayList<>(List.of(item2, item3));
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        assertThat(tracker.findByName("Leo"), is(list));
    }
}