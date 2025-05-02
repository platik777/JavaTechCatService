package ru.platik777.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.platik777.entity.Owner;

import java.util.List;

public class OwnerDAOImpl implements OwnerDAO {
    private final SessionFactory sessionFactory;

    public OwnerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Owner> getAllOwners() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Owner> owners = session.createQuery("from Owner", Owner.class).list();
            session.getTransaction().commit();
            session.close();
            return owners;
        }
    }

    public void save(Owner owner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(owner);
            session.getTransaction().commit();
        }
    }

    public Owner findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Owner.class, id);
        }
    }

    public void update(Owner owner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(owner);
            session.getTransaction().commit();
        }
    }

    public void delete(Owner owner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(owner);
            session.getTransaction().commit();
        }
    }
}
