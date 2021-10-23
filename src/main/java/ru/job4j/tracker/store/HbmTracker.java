package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.interfaces.Store;
import ru.job4j.tracker.model.Item;

import javax.persistence.Query;
import java.util.List;

public class HbmTracker implements Store, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(item);
            return item;
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public boolean replace(int id, Item item) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        String hql = "update ru.job4j.tracker.model.Item "
                + "SET name = :name "
                + ", description  = :description"
                + " where id = :idParam";
        try {
            Query query = session.createQuery(hql);
            query.setParameter("idParam", id);
            query.setParameter("name", item.getName());
            query.setParameter("description", item.getDescription());
            return query.executeUpdate() > 0;
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public boolean delete(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            return session
                    .createQuery("delete from ru.job4j.tracker.model.Item "
                            + "as item where item.id=:id").
                            setParameter("id", id)
                    .executeUpdate() > 0;
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            return session.createQuery("from ru.job4j.tracker.model.Item").list();
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public List<Item> findByName(String name) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            return session
                    .createQuery("from ru.job4j.tracker.model.Item as item where item.name=:name").
                            setParameter("name", name).list();
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public Item findById(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            return session.get(Item.class, id);
        } finally {
            tx.commit();
            session.close();
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Override
    public void init() {

    }
}
