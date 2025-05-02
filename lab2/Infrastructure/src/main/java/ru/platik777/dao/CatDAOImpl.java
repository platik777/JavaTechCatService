package ru.platik777.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.platik777.entity.Cat;

import java.util.List;

public class CatDAOImpl implements CatDAO {
    private final SessionFactory sessionFactory;

    public CatDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Cat> getAllCats() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Cat> cats = session.createQuery("from Cat", Cat.class).list();
            session.getTransaction().commit();
            session.close();
            return cats;
        }
    }

    public Cat findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Cat cat = session.get(Cat.class, id);
            session.getTransaction().commit();
            session.close();
            return cat;
        }
    }

    public void save(Cat cat) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(cat);
            session.getTransaction().commit();
        }
    }

    public void update(Cat cat) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(cat);
            session.getTransaction().commit();

        }
    }

    public void delete(Cat cat) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(cat);
            session.getTransaction().commit();
        }
    }

    public void makeFriendsById(int cat1Id, int cat2Id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Cat cat1 = session.get(Cat.class, cat1Id);
            Cat cat2 = session.get(Cat.class, cat2Id);
            cat1.addFriend(cat2);
            session.getTransaction().commit();
        }
    }
}
