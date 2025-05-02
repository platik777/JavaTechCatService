package ru.platik777;

import org.hibernate.SessionFactory;
import ru.platik777.SessionFactorySingleton;
import ru.platik777.controllers.CatController;
import ru.platik777.controllers.OwnerController;
import ru.platik777.dao.CatDAOImpl;
import ru.platik777.dao.OwnerDAOImpl;
import ru.platik777.service.Service;

public class InitApplication {
    private static CatController catController;
    private static OwnerController ownerController;

    public static void initApplication() {
        SessionFactory sessionFactory = SessionFactorySingleton.getSessionFactory();
        CatDAOImpl catDAOImpl = new CatDAOImpl(sessionFactory);
        OwnerDAOImpl ownerDAOImpl = new OwnerDAOImpl(sessionFactory);
        Service service = new Service(catDAOImpl, ownerDAOImpl);
        catController = new CatController(service);
        ownerController = new OwnerController(service);
    }

    public static CatController getCatController() {
        return catController;
    }

    public static OwnerController getOwnerController() {
        return ownerController;
    }
}

