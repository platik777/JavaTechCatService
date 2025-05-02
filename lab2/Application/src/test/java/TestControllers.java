import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.platik777.controllers.CatController;
import ru.platik777.controllers.OwnerController;
import ru.platik777.dao.CatDAO;
import ru.platik777.dao.CatDAOImpl;
import ru.platik777.dao.OwnerDAO;
import ru.platik777.dao.OwnerDAOImpl;
import ru.platik777.entity.Cat;
import ru.platik777.entity.Owner;
import ru.platik777.service.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestControllers {
    private final CatDAO catDAO = Mockito.mock(CatDAOImpl.class);
    private final OwnerDAO ownerDAO = Mockito.mock(OwnerDAOImpl.class);
    private final Service service = new Service(catDAO, ownerDAO);
    private final CatController catController = new CatController(service);
    private final OwnerController ownerController = new OwnerController(service);


    @Test
    public void getAllCatsTest() {
        List<Cat> cats = new ArrayList<>(List.of(new Cat()));
        Mockito.when(catDAO.getAllCats()).thenReturn(cats);
        assertEquals(cats, catController.getAllCats());
    }

    @Test
    public void getAllOwnersTest() {
        List<Owner> owners = new ArrayList<>(List.of(new Owner()));
        Mockito.when(ownerDAO.getAllOwners()).thenReturn(owners);
        assertEquals(owners, ownerController.getAllOwners());
    }

    @Test
    public void getCatByIdTest() {
        Cat cat = new Cat();
        cat.setId(1);
        Mockito.when(catDAO.findById(1)).thenReturn(cat);
        assertEquals(cat, catController.findById(1));
    }

    @Test
    public void getOwnerByIdTest() {
        Owner owner = new Owner();
        owner.setId(1L);
        Mockito.when(ownerDAO.findById(1)).thenReturn(owner);
        assertEquals(owner, ownerController.findById(1));
    }
}
