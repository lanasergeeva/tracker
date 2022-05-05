package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.interfaces.react.Observe;
import ru.job4j.tracker.model.Item;

import java.util.List;
import java.util.function.Function;

public class HbmTracker implements Store, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        tx(session -> session.save(item));
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        item.setId(id);
        return tx(session -> {
            session.update(item);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return tx(session ->
                session.createQuery("delete from ru.job4j.tracker.model.Item "
                        + "as item where item.id=:id").
                        setParameter("id", id)
                        .executeUpdate() > 0);
    }

    @Override
    public List<Item> findAll() {
        return tx(session -> session.createQuery("from ru.job4j.tracker.model.Item").list());
    }

    @Override
    public List<Item> findByName(String name) {
        return tx(
                session -> session
                        .createQuery(
                                "from ru.job4j.tracker.model.Item as item where item.name=:name").
                                setParameter("name", name).list());
    }

    @Override
    public Item findById(int id) {
        return tx(session -> session.get(Item.class, id));
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void getByReact(Observe<Item> observe) {

    }
}
