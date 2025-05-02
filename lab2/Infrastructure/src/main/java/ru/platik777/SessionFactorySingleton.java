package ru.platik777;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.platik777.entity.Cat;
import ru.platik777.entity.Owner;

public class SessionFactorySingleton {
    private static volatile SessionFactory sessionFactory;

    private SessionFactorySingleton() { }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (SessionFactorySingleton.class) {
                if (sessionFactory == null) {
                    Configuration configuration = new Configuration().
                            addAnnotatedClass(Owner.class).
                            addAnnotatedClass(Cat.class);

                    sessionFactory = configuration.buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
