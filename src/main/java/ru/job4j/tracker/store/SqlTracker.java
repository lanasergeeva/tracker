package ru.job4j.tracker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.react.Observe;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {

    public static final Logger LOGGER = LoggerFactory.getLogger(SqlTracker.class.getSimpleName());

    private Connection cn;

    public SqlTracker() {
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    @Override
    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement statement =
                     cn.prepareStatement("insert into items (name) values (?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in add", e);
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean result = false;
        try (PreparedStatement statement =
                     cn.prepareStatement("update items set name = ? where id = ?")) {
            statement.setString(1, item.getName());
            statement.setInt(2, id);
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("Exception in replace", e);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement statement =
                     cn.prepareStatement("delete from items where id = ?")) {
            statement.setInt(1, id);
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("Exception in delete", e);
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement statement =
                     cn.prepareStatement("select * from items order by id asc")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    itemList.add(new Item(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in delete", e);
        }
        return itemList;
    }

    @Override
    public void getByReact(Observe<Item> observe) {
        try (PreparedStatement statement =
                     cn.prepareStatement("select * from items order by id asc")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    observe.receive(getFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Exception in getByReact", e);
        }
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement statement =
                     cn.prepareStatement("select * from items where name like ?")) {
            statement.setString(1, key);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    itemList.add(new Item(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in findByName", e);
        }
        return itemList;
    }

    @Override
    public Item findById(int id) {
        Item rsl = null;
        try (PreparedStatement statement =
                     cn.prepareStatement("select * from items where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    rsl = new Item(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in findById", e);
        }
        return rsl;
    }

    private Item getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getInt("id"),
                resultSet.getString("name"));
    }

    public static void main(String[] args) {
        SqlTracker sqlTracker = new SqlTracker();
        sqlTracker.init();
        System.out.println(sqlTracker.findAll());
    }
}
