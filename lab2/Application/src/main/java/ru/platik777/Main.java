package ru.platik777;


import ru.platik777.controllers.CatController;
import ru.platik777.controllers.OwnerController;
import ru.platik777.entity.Cat;
import ru.platik777.entity.Color;
import ru.platik777.entity.Owner;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        InitMigration.migrate();
        InitApplication.initApplication();
        CatController catController = InitApplication.getCatController();
        OwnerController ownerController = InitApplication.getOwnerController();
        Owner owner = new Owner("Lexa", LocalDate.of(2004, Calendar.FEBRUARY, 17));
        Cat cat = new Cat("Cat", LocalDate.of(2010, 11, 21), "Breed1", 14, Color.BLACK);
        cat.setOwner(owner);
        ownerController.save(owner);
        catController.save(cat);
    }
}
