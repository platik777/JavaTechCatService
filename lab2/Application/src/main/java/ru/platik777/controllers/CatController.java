package ru.platik777.controllers;

import ru.platik777.entity.Cat;
import ru.platik777.service.Service;

import java.util.List;

public class CatController {
    private final Service service;

    public CatController(Service service) {
        this.service = service;
    }

    public List<Cat> getAllCats() {
        return service.getAllCats();
    }

    public Cat findById(int id) {
        return service.findCatById(id);
    }

    public void save(Cat cat) {
        service.save(cat);
    }

    public void update(Cat cat) {
        service.update(cat);
    }

    public void delete(Cat cat) {
        service.delete(cat);
    }

    public void makeFriendsById(int cat1Id, int cat2Id) {
        service.makeFriendsById(cat1Id, cat2Id);
    }
}
