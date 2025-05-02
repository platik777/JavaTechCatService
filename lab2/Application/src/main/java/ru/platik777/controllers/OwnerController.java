package ru.platik777.controllers;

import ru.platik777.entity.Owner;
import ru.platik777.service.Service;

import java.util.List;

public class OwnerController {
    private final Service service;

    public OwnerController(Service service) {
        this.service = service;
    }

    public List<Owner> getAllOwners() {
        return service.getAllOwners();
    }

    public Owner findById(int id) {
        return service.findOwnerById(id);
    }

    public void save(Owner owner) {
        service.save(owner);
    }

    public void update(Owner owner) {
        service.update(owner);
    }

    public void delete(Owner owner) {
        service.delete(owner);
    }
}
